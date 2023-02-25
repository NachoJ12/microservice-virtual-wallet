package com.example.billetera.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDTO {
    public Long id;
    public Double balance;
}
