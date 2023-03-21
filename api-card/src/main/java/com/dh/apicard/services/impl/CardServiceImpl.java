package com.dh.apicard.services.impl;

import com.dh.apicard.client.IMarginsService;
import com.dh.apicard.client.IWalletService;
import com.dh.apicard.model.CreditCard;
import com.dh.apicard.model.CreditCardMovement;
import com.dh.apicard.repository.CreditCardRepository;
import com.dh.apicard.services.ICardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class CardServiceImpl implements ICardService {

    private final CreditCardRepository creditCardRepository;
    private final IMarginsService iMarginsService;
    private final IWalletService iWalletService;

    public CardServiceImpl(CreditCardRepository creditCardRepository, IMarginsService iMarginsService, IWalletService iWalletService) {
        this.creditCardRepository = creditCardRepository;
        this.iMarginsService = iMarginsService;
        this.iWalletService = iWalletService;
    }

    @Override
    public void createCreditCard(String docType, String docNumber, String currency) {
        var margins = iMarginsService.getMargins(docType, docNumber);

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
    public CreditCard getByDocTypeAndDocNumber(String docType, String docNumber) {
        return creditCardRepository.findByDocumentTypeAndDocumentNumber(docType, docNumber);
    }

    @Override
    public List<CreditCard> getAllCards() {
        return creditCardRepository.findAll();
    }

    @Override
    public void debit(CreditCardMovement creditCardMovement) {
        CreditCard card = creditCardRepository.findByCardNumber(creditCardMovement.getCardNumber());
        BigDecimal totalAmount = creditCardMovement.getAmount().getValue();

        // Verify that the card exists and has an available limit greater than or equal to the total amount to be debited
        if(!card.getCardNumber().isEmpty() && card.getAvailableLimit().compareTo(totalAmount) >= 0){
            // Update the consumed limit (+) and the available limit (-)
            creditCardRepository.save(
                    CreditCard.builder()
                            .documentType(card.getDocumentType())
                            .documentNumber(card.getDocumentNumber())
                            .cardNumber(card.getCardNumber())
                            .currency(card.getCurrency())
                            .qualifiedLimit(card.getQualifiedLimit())
                            .consumedLimit(card.getConsumedLimit().add(totalAmount))
                            .availableLimit(card.getAvailableLimit().subtract(totalAmount))
                            .build()
            );
        } else {
            // throw error (to do)
            System.out.println("The amount exceeds the available limit");
        }
    }

    // TO DO
    @Override
    public void payCreditCard(String cardNumber, String docType, String docNumber) {
        var wallet = iWalletService.getWallet(docType, docNumber);
        double balanceWallet = wallet.get().getBalance();

        var card = this.getByDocTypeAndDocNumber(docType, docNumber);

        // Verify that the card number entered by parameter is the same as the card number sought by document number
        // Verify that the limit consumed is less than or equal to the balance of the wallet (available to pay)
        if(card.getCardNumber() == cardNumber &&
                card.getConsumedLimit().compareTo(BigDecimal.valueOf(balanceWallet)) <= 0){
            // Debit the money from the wallet.
            // Return the available limit
        }

        //If there is no money available throw an error

    }
}
