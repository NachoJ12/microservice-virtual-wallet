package com.dh.wallet.controller;

import com.dh.wallet.dto.WalletDTO;
import com.dh.wallet.exception.WalletException;
import com.dh.wallet.model.Wallet;
import com.dh.wallet.services.impl.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletServiceImpl walletService;

    @GetMapping("/{documentType}/{document}/{code}")
    public Wallet getWallet (@PathVariable String documentType,
                                       @PathVariable String document,
                                       @PathVariable String code) throws Exception {
        return walletService.getWalletByCurrency(documentType, document, code);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveWallet (@RequestBody Wallet wallet) throws WalletException {
        walletService.createWallet(wallet);
        return ResponseEntity.ok("Wallet saved");
    }

    @GetMapping("/{documentType}/{document}")
    public List<Wallet> getBalance (@PathVariable String documentType,
                                     @PathVariable String document){
        return walletService.getBalanceByDocument(documentType, document);
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateWallet (@RequestBody WalletDTO walletDTO) throws Exception {
        walletService.updateWallet(walletDTO);
        return ResponseEntity.ok("Wallet update");
    }
}
