package com.dh.apicard.controller;

import com.dh.apicard.model.CreditCard;
import com.dh.apicard.model.CreditCardMovement;
import com.dh.apicard.services.impl.CardServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CreditCardController {

    private final CardServiceImpl cardService;

    public CreditCardController(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/{documentType}/{documentNumber}/{currency}")
    public void saveCard(@PathVariable String documentType, @PathVariable String documentNumber, @PathVariable String currency){
        cardService.createCreditCard(documentType, documentNumber, currency);
    }

    @GetMapping("/{documentType}/{documentNumber}")
    public CreditCard getByDocTypeAndDocNumber (@PathVariable String documentType, @PathVariable String documentNumber){
        return cardService.getByDocTypeAndDocNumber(documentType, documentNumber);
    }

    @GetMapping()
    public List<CreditCard> getAll(){
        return cardService.getAllCards();
    }

    /* Debit. All movement data must be passed, and internally makes the debit */
    @PostMapping("/debit")
    public void debit(@RequestBody CreditCardMovement creditCardMovement){
        cardService.debit(creditCardMovement);
    }

    @PostMapping("/pay/{cardNumber}/{documentType}/{documentNumber}")
    public void pay(@PathVariable String cardNumber, @PathVariable String documentType, @PathVariable String documentNumber){
        /*POST(Pay card. Card number, docType and docNumber):
            1) Api-wallet is consulted if it is available to pay
            2) The money is debited from api-wallet
            3) The available limit is returned
            4) If it is not available, an error is thrown.*/
    }
}
