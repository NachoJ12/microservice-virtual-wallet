package com.dh.wallet.services.impl;

import com.dh.wallet.client.ICustomerClient;
import com.dh.wallet.dto.WalletDTO;
import com.dh.wallet.exception.MessageError;
import com.dh.wallet.exception.WalletException;
import com.dh.wallet.model.Currency;
import com.dh.wallet.model.Wallet;
import com.dh.wallet.repository.IWalletRepository;
import com.dh.wallet.services.IWalletService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements IWalletService {

    @Autowired
    private IWalletRepository walletRepository;

    @Autowired
    private ICustomerClient customerFeignClient;


    @Override
    @CircuitBreaker(name="customer", fallbackMethod="getCustomerFallbackValue")
    @Retry(name = "customer")
    public void createWallet(Wallet wallet) throws WalletException {
        customerFeignClient.getCustomer(wallet.getDocumentType(), wallet.getDocument()).orElseThrow(() -> new WalletException(MessageError.CUSTOMER_NOT_FOUND));

        /* this if is return false because ever return empty */
        if(walletRepository.findByDocumentAndCode(wallet.getDocumentType(), wallet.getDocument(), wallet.getCurrency().getCode()).isPresent()){
            throw new WalletException(MessageError.WALLET_EXISTS);
        }

        walletRepository.save(wallet);
    }

    /* This implementation is only to test the retry and circuit breaker mechanism, however, what it does in the fallback could be improved since it doesn't make much sense. */
    private void getCustomerFallbackValue(WalletException ex) {
        walletRepository.save(new Wallet(666L, "dniTypeError", "dniError", new Currency(1L, "errorCode", "descriptionError"), 0.0));
    }

    @Override
    public void updateWallet(WalletDTO walletDTO) throws Exception {
        Wallet wallet = walletRepository.findById(walletDTO.getId()).orElseThrow(() -> new WalletException(MessageError.WALLET_NOT_FOUND));
        wallet.setBalance(walletDTO.getBalance());
        walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletByCurrency(String documentType, String document, String code) throws Exception{
        return walletRepository.findByDocumentAndCode(documentType, document, code).orElseThrow(() -> new WalletException(MessageError.WALLET_NOT_FOUND));
    }

    @Override
    public List<Wallet> getBalanceByDocument(String documentType, String document) {
        return walletRepository.findByDocument(documentType, document);
    }

    @Override
    public void updateBalance(Long id, Double balance) throws WalletException {
        var wallet = walletRepository.findById(id);
        wallet.get().setBalance(balance);
        walletRepository.save(wallet.get());
    }
}
