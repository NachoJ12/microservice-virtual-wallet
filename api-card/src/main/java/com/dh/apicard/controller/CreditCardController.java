package com.dh.apicard.controller;

import com.dh.apicard.exceptions.CardException;
import com.dh.apicard.model.CreditCard;
import com.dh.apicard.model.CreditCardMovement;
import com.dh.apicard.services.impl.CardServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/card")
public class CreditCardController {

    private final CardServiceImpl cardService;

    public CreditCardController(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/{documentType}/{documentNumber}/{currency}")
    public void create(@PathVariable String documentType, @PathVariable String documentNumber, @PathVariable String currency) throws CardException{
        cardService.createCreditCard(documentType, documentNumber, currency);
    }

    @GetMapping("/{documentType}/{documentNumber}")
    public CreditCard getByDocTypeAndDocNumber(@PathVariable String documentType, @PathVariable String documentNumber) throws CardException{
        return cardService.getByDocTypeAndDocNumber(documentType, documentNumber);
    }

    @GetMapping()
    public List<CreditCard> getAll(){
        return cardService.getAllCards();
    }

    /* Debit. All movement data must be passed, and internally makes the debit */
    @PutMapping("/debit")
    public void debit(@RequestBody CreditCardMovement creditCardMovement) throws CardException {
        cardService.debit(creditCardMovement);
    }

    @PutMapping("/pay")
    public void pay(@RequestBody @Valid PayCreditCardDto payCreditCardDto) throws CardException{
        cardService.payCreditCard(payCreditCardDto);
    }

    public record PayCreditCardDto(
            @NotNull
            String cardNumber,
            @NotNull
            String documentType,
            @NotNull
            String documentNumber
    ) {
    }

}
