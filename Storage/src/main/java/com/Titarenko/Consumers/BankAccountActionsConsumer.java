package com.Titarenko.Consumers;

import com.Titarenko.Models.BalanceModifyEvent;
import com.Titarenko.Models.BankAccountCreationEvent;
import com.Titarenko.Repositories.AccountCreationRepository;
import com.Titarenko.Repositories.BalanceEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KafkaListener(
        topics = "account-topic",
        groupId = "bank-account-group",
        containerFactory = "bankAccountKafkaListenerContainerFactory"
)
public class BankAccountActionsConsumer {
    private final AccountCreationRepository accountCreationRepository;
    private final BalanceEventRepository balanceEventRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public BankAccountActionsConsumer(AccountCreationRepository accountCreationRepository,
                                      BalanceEventRepository balanceEventRepository) {
        this.accountCreationRepository = accountCreationRepository;
        this.balanceEventRepository = balanceEventRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "account-topic", groupId = "bank-account-group")
    public void consume(ConsumerRecord<Long, String> record) {
        Long key = record.key();
        String json = record.value();

        try {
            BankAccountCreationEvent creationMessage = objectMapper.readValue(json, BankAccountCreationEvent.class);
            if (creationMessage.getOwnerLogin() != null && creationMessage.getDate() != null) {
                BankAccountCreationEvent event = new BankAccountCreationEvent(
                        creationMessage.getOwnerLogin(),
                        creationMessage.getDate()
                );
                accountCreationRepository.save(event);
                return;
            }
        } catch (IOException ignored) {
        }

        try {
            BalanceModifyEvent balanceMessage = objectMapper.readValue(json, BalanceModifyEvent.class);
            if (balanceMessage.getOperation() != null && balanceMessage.getAmount() != null && balanceMessage.getDate() != null) {
                BalanceModifyEvent event = new BalanceModifyEvent(
                        balanceMessage.getOperation(),
                        balanceMessage.getAmount(),
                        key,
                        balanceMessage.getDate()
                );
                balanceEventRepository.save(event);
                return;
            }
        } catch (IOException ignored) {
        }
    }
}
