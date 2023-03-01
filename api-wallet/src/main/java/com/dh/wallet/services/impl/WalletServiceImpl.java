package com.dh.wallet.services.impl;

import com.dh.wallet.dto.WalletDTO;
import com.dh.wallet.model.Wallet;
import com.dh.wallet.repository.IWalletRepository;
import com.dh.wallet.services.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements IWalletService {

    @Autowired
    private IWalletRepository walletRepository;

    @Override
    public Wallet createWallet(Wallet wallet) {
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet updateWallet(WalletDTO walletDTO) {
        Optional<Wallet> wallet = walletRepository.findById(walletDTO.getId());
        wallet.get().setBalance(walletDTO.getBalance());
        walletRepository.save(wallet.get());
        return wallet.get();
    }

    @Override
    public Optional<Wallet> getWalletByCurrency(String documentType, String document, String code) {
        return walletRepository.findByDocumentAndCode(documentType, document, code);
    }

    @Override
    public List<Wallet> getBalanceByDocument(String documentType, String document) {
        return walletRepository.findByDocument(documentType, document);
    }
}
