package com.dh.apicard.client;

import com.dh.apicard.config.LoadBalancerConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

@FeignClient(name="api-wallet")
@LoadBalancerClient(value="api-wallet", configuration= LoadBalancerConfiguration.class)
public interface IWalletService {

    @GetMapping("/wallet/{documentType}/{documentValue}")
    Optional<WalletDTO> getWallet (@PathVariable String documentType, @PathVariable String documentValue);

    @GetMapping("/wallet/{documentType}/{documentValue}/{code}")
    Optional<WalletDTO> getWalletByDocTypeAndDocNumberAndCode(@PathVariable String documentType, @PathVariable String documentValue, @PathVariable String code);

    @PutMapping("/wallet/{id}/{balance}")
    void updateWallet (@PathVariable Long id, @PathVariable Double balance);


    @Getter
    @Setter
    class WalletDTO {
        private Long id;
        private String documentType;
        private String document;
        private Double balance;
    }
}
