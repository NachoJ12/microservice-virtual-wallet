package com.dh.wallet.model;


import javax.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String documentType;
    public String document;
    @ManyToOne
    @JoinColumn(name = "idCurrency", nullable = false)
    public Currency currency;
    public Double balance;

    public Wallet() {
    }

    public Wallet(Long id, String documentType, String document, Currency currency, Double balance) {
        this.id = id;
        this.documentType = documentType;
        this.document = document;
        this.currency = currency;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}


