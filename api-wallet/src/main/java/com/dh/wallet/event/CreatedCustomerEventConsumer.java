package com.dh.wallet.event;

import com.dh.wallet.config.RabbitMQConfig;
import com.dh.wallet.model.Currency;
import com.dh.wallet.model.Wallet;
import com.dh.wallet.services.impl.WalletServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatedCustomerEventConsumer {
    @Autowired
    WalletServiceImpl walletService;
    @RabbitListener(queues = RabbitMQConfig.QUEUE_CREATED_CUSTOMER)
    public void listen(CreatedCustomerEventConsumer.Data message) throws Exception {
        Wallet wallet=new Wallet();
        Currency currency = new Currency();

        wallet.setDocumentType(message.documentType);
        wallet.setDocument(message.documentNumber);
        wallet.setBalance(5.0);

        /*currency.setCode("eth");
        currency.setDescription("Bitcoin");*/
        wallet.setCurrency(new Currency(1L, null, null));

        walletService.createWallet(wallet);
        System.out.print("CREATED CUSTOMER DOCTYPE: "+ message.documentType + ", DOCNUMBER: " + message.documentNumber);

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Data{
        private String documentType;
        private String documentNumber;
    }
}
