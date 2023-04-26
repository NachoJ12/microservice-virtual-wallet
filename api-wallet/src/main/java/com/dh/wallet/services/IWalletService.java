package com.dh.wallet.services;

import com.dh.wallet.dto.WalletDTO;
import com.dh.wallet.exception.WalletException;
import com.dh.wallet.model.Wallet;

import java.util.List;

public interface IWalletService {
    void createWallet(Wallet wallet) throws WalletException;
    void updateWallet(WalletDTO walletDTO) throws Exception;
    Wallet getWalletByCurrency(String documentType, String document, String code) throws Exception;
    List<Wallet> getBalanceByDocument(String documentType, String document);
    void updateBalance(Long id, Double balance) throws WalletException;
}
