package com.dh.apicard.client;

import com.dh.apicard.config.LoadBalancerConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(name="api-margins")
@LoadBalancerClient(value="api-margins", configuration= LoadBalancerConfiguration.class)
public interface IMarginsService {

    @GetMapping("/calculate/{documentType}/{documentValue}")
    Optional<CalificationDTO> getMargins (@PathVariable String documentType, @PathVariable String documentValue);

    @Getter
    @Setter
    class CalificationDTO {
        private BigDecimal totalMargin;
        private BigDecimal totalMarginAdditional;
    }

}
