package com.example.billetera.services;

import com.example.billetera.dto.WalletDTO;
import com.example.billetera.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface IWalletService {

    Wallet createWallet(Wallet wallet);

    Wallet updateWallet(WalletDTO walletDTO);
    Optional<Wallet> getWalletByCurrency(String documentType, String document, String code);

    List<Wallet> getBalanceByDocument(String documentType, String document);
}
