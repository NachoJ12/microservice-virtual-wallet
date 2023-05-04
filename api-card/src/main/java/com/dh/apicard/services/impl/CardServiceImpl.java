package com.dh.apicard.services.impl;

import com.dh.apicard.client.IMarginsService;
import com.dh.apicard.client.IWalletService;
import com.dh.apicard.controller.CreditCardController;
import com.dh.apicard.exceptions.CardException;
import com.dh.apicard.exceptions.MessageError;
import com.dh.apicard.model.CreditCard;
import com.dh.apicard.model.CreditCardMovement;
import com.dh.apicard.repository.CreditCardMovementRepository;
import com.dh.apicard.repository.CreditCardRepository;
import com.dh.apicard.services.ICardService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class CardServiceImpl implements ICardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMovementRepository creditCardMovementRepository;
    private final IMarginsService iMarginsService;
    private final IWalletService iWalletService;

    private final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    public CardServiceImpl(CreditCardRepository creditCardRepository, CreditCardMovementRepository creditCardMovementRepository, IMarginsService iMarginsService, IWalletService iWalletService) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMovementRepository = creditCardMovementRepository;
        this.iMarginsService = iMarginsService;
        this.iWalletService = iWalletService;
    }

    // Generate a 16 digit random number for the card.
    public static String generate16DigitRandomNumber() {
        Random random = new Random();
        String num = "";
        for (int i = 0; i < 16; i++) {
            num = num + random.nextInt(10);
        }
        return num;
    }

    @Override
    @Retry(name= "retryCreateCreditCard")
    @CircuitBreaker(name = "createCreditCard", fallbackMethod = "createCreditCardFallback")
    public void createCreditCard(String docType, String docNumber, String currency) throws CardException {
        // It remains to verify that the client by docType and docNumber exists in order to create a card. (to do)
        var margins = iMarginsService.getMargins(docType, docNumber);

        if(creditCardRepository.findByDocumentTypeAndDocumentNumber(docType,docNumber).isPresent()){
            throw new CardException(MessageError.CUSTOMER_WITH_CARD);
        }

        creditCardRepository.save(
                CreditCard.builder()
                        .documentType(docType)
                        .documentNumber(docNumber)
                        .cardNumber(generate16DigitRandomNumber())
                        .currency(currency)
                        .qualifiedLimit(margins.get().getTotalMargin())
                        .consumedLimit(new BigDecimal(0))
                        .availableLimit(margins.get().getTotalMargin())
                        .build()
        );
    }

    // As a fallback instead of throwing an exception, we'll save the request by providing card creation. When the api-margins does not work, we will momentarily give you margins of 0 so that they can be reviewed later
    public void createCreditCardFallback(String docType, String docNumber, String currency, Exception ex) throws CardException {
        logger.error("ERROR: " + ex);
        // It remains to verify that the client by docType and docNumber exists in order to create a card. (to do)

        if(creditCardRepository.findByDocumentTypeAndDocumentNumber(docType,docNumber).isPresent()){
            throw new CardException(MessageError.CUSTOMER_WITH_CARD);
        }

        creditCardRepository.save(
                CreditCard.builder()
                        .documentType(docType)
                        .documentNumber(docNumber)
                        .cardNumber(generate16DigitRandomNumber())
                        .currency(currency)
                        .qualifiedLimit(new BigDecimal(0))
                        .consumedLimit(new BigDecimal(0))
                        .availableLimit(new BigDecimal(0))
                        .build()
        );
    }

    @Override
    public CreditCard getByDocTypeAndDocNumber(String docType, String docNumber) throws CardException {
        return creditCardRepository.findByDocumentTypeAndDocumentNumber(docType, docNumber).orElseThrow(() -> new CardException(MessageError.CUSTOMER_NOT_HAVE_CARD));
    }

    @Override
    public List<CreditCard> getAllCards() {
        return creditCardRepository.findAll();
    }

    @Override
    public void debit(CreditCardMovement creditCardMovement) throws CardException{
        //CreditCard card = creditCardRepository.findByCardNumber(creditCardMovement.getCardNumber());
        BigDecimal totalAmount = creditCardMovement.getAmount().getValue();

        // review, I think it is wrong since I am bringing the recipient and not using the card of the person who is going to pay, which I think is what should be looked for
        var creditCard = creditCardRepository.findByDocumentTypeAndDocumentNumber(creditCardMovement.getDebtCollector().getDocumentType(), creditCardMovement.getDebtCollector().getDocumentNumber()).orElseThrow(() -> new CardException(MessageError.CUSTOMER_NOT_HAVE_CARD));

        if(creditCard ==null){
            throw new CardException(MessageError.CUSTOMER_NOT_HAVE_CARD);
        }

        // Verify that the card number entered by parameter is the same as the card number sought by document number
        if(!creditCard.getCardNumber().equals(creditCardMovement.getCardNumber())){
            throw new CardException(MessageError.CARD_NOT_MATCH);
        }

        // Verify that the card exists and has an available limit greater than or equal to the total amount to be debited
        if(!creditCard.getCardNumber().isEmpty()
                && creditCard.getAvailableLimit().compareTo(totalAmount) >= 0) {
            // Update the consumed limit (+) and the available limit (-)
            creditCard.setConsumedLimit(creditCard.getConsumedLimit().add(totalAmount));
            creditCard.setAvailableLimit(creditCard.getAvailableLimit().subtract(totalAmount));
            creditCardRepository.save(creditCard);
            creditCardMovementRepository.save(creditCardMovement);
        } else {
            // throw error (to do)
            throw new CardException(MessageError.EXCEEDS_AVAILABLE_LIMIT);
        }
    }

    @Override
    public void payCreditCard(CreditCardController.PayCreditCardDto payCreditCardDto) throws CardException {
        var creditCard = creditCardRepository.findByDocumentTypeAndDocumentNumber(payCreditCardDto.documentType(), payCreditCardDto.documentNumber()).orElseThrow(() -> new CardException(MessageError.CUSTOMER_NOT_HAVE_CARD));
        var consumed = creditCard.getConsumedLimit();

        var walletResult = iWalletService.getWalletByDocTypeAndDocNumberAndCode(payCreditCardDto.documentType(), payCreditCardDto.documentNumber(), creditCard.getCurrency());
        double balanceWallet = walletResult.get().getBalance();

        // Verify that the card number entered by parameter is the same as the card number sought by document number
        if(!creditCard.getCardNumber().equals(payCreditCardDto.cardNumber())){
            throw new CardException(MessageError.CARD_NOT_MATCH);
        }

        // Verify that the limit consumed is less than or equal to the balance of the wallet (available to pay). If there is no money available throw an error
        if(BigDecimal.valueOf(balanceWallet).compareTo(consumed) <= 0){
            throw new CardException(MessageError.CUSTOMER_NOT_HAVE_FUNDS);
        }

        creditCard.setConsumedLimit(BigDecimal.ZERO);
        creditCard.setAvailableLimit(creditCard.getAvailableLimit().add(consumed));
        creditCardRepository.save(creditCard);
        // Debit the money from the wallet (update balance).
        iWalletService.updateWallet(walletResult.get().getId(),walletResult.get().getBalance()-consumed.doubleValue());
        }

    }

