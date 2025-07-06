package Producers;

import Mesages.BalanceModifyMessage;
import Mesages.BankAccountCreationMessage;
import Models.BankAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Aspect
@Component
public class BankAccountActionsProducer {
    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String topicName = "account-topic";

    @Autowired
    public BankAccountActionsProducer(KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterReturning(
            pointcut = "execution(* Services.BankAccountService.CreateAccount(..))",
            returning = "bankAccount")
    public void UserCreated(BankAccount bankAccount) throws JsonProcessingException {
        BankAccountCreationMessage message = new BankAccountCreationMessage(
                bankAccount.getOwner().getLogin(),
                Instant.now()
        );

        kafkaTemplate.send(topicName, bankAccount.getId(), objectMapper.writeValueAsString(message));
    }

    @AfterReturning(
            pointcut = "execution(* Services.BankAccountService.Deposit(..)) ||" +
                    " execution(* Services.BankAccountService.Withdraw(..)) &&" +
                    " args(id, amount)", argNames = "joinPoint,id,amount")
    public void BalanceModify(JoinPoint joinPoint, Long id, BigDecimal amount) throws JsonProcessingException {
        String methodName = joinPoint.getSignature().getName();

        BalanceModifyMessage message = new BalanceModifyMessage(
                methodName.toLowerCase(),
                amount,
                Instant.now()
        );

        kafkaTemplate.send(topicName, id, objectMapper.writeValueAsString(message));
    }

    @AfterReturning(
            pointcut = "execution(* Services.BankAccountService.Transfer(..)) &&" +
                    " args(homeAccountId, targetAccountId, amount)", argNames = "homeAccountId,targetAccountId,amount")
    public void BalanceModify(Long homeAccountId, Long targetAccountId, BigDecimal amount) throws JsonProcessingException {
        BalanceModifyMessage homeAccountMessage = new BalanceModifyMessage(
                "transfer-homeAccount",
                amount,
                Instant.now()
        );
        BalanceModifyMessage targetAccountMessage = new BalanceModifyMessage(
                "transfer-targetAccount",
                amount,
                Instant.now()
        );

        kafkaTemplate.send(topicName, homeAccountId, objectMapper.writeValueAsString(homeAccountMessage));
        kafkaTemplate.send(topicName, targetAccountId, objectMapper.writeValueAsString(targetAccountMessage));
    }
}
