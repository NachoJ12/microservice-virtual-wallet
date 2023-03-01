package com.dh.wallet.services;

import com.dh.wallet.dto.WalletDTO;
import com.dh.wallet.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface IWalletService {

    Wallet createWallet(Wallet wallet);

    Wallet updateWallet(WalletDTO walletDTO);
    Optional<Wallet> getWalletByCurrency(String documentType, String document, String code);

    List<Wallet> getBalanceByDocument(String documentType, String document);
}
