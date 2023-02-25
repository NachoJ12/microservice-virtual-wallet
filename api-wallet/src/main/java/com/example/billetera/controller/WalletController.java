package com.example.billetera.controller;

import com.example.billetera.dto.WalletDTO;
import com.example.billetera.model.Wallet;
import com.example.billetera.services.impl.WalletServiceImpl;
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
    public Optional<Wallet> getWallet (@PathVariable String documentType,
                                       @PathVariable String document,
                                       @PathVariable String code){
        return walletService.getWalletByCurrency(documentType, document, code);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveWallet (@RequestBody Wallet wallet){
        return ResponseEntity.ok(walletService.createWallet(wallet));
    }

    @GetMapping("/{documentType}/{document}")
    public List<Wallet> getBalance (@PathVariable String documentType,
                                     @PathVariable String document){
        return walletService.getBalanceByDocument(documentType, document);
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateWallet (@RequestBody WalletDTO walletDTO){
        return ResponseEntity.ok(walletService.updateWallet(walletDTO));
    }
}
