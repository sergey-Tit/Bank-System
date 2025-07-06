package Producers;

import Mesages.FriendListModifyMessage;
import Mesages.UserCreationMessage;
import Models.User;
import Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
public class UserActionsProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String topicName = "client-topic";

    @Autowired
    public UserActionsProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterReturning(
            pointcut = "execution(* Services.UserService.CreateUser(..))",
            returning = "user")
    public void UserCreated(User user) throws JsonProcessingException {
        UserCreationMessage message = new UserCreationMessage(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColour().toString(),
                Instant.now()
        );

        kafkaTemplate.send(topicName, user.getLogin(), objectMapper.writeValueAsString(message));
    }

    @AfterReturning(
            pointcut = "(execution(* Services.UserService.AddFriend(..)) || execution(* Services.UserService.RemoveFriend(..))) && args(login)", argNames = "joinPoint,login")
    public void FriendModified(JoinPoint joinPoint, String login) throws JsonProcessingException {
        String methodName = joinPoint.getSignature().getName();
        String operation = GetOperation(methodName);

        UserService userService = (UserService) joinPoint.getTarget();
        String currentLogin = userService.GetCurrentUserLogin();

        FriendListModifyMessage message = new FriendListModifyMessage(
                operation,
                login,
                Instant.now()
        );

        kafkaTemplate.send(topicName, currentLogin, objectMapper.writeValueAsString(message));
    }

    private String GetOperation(String methodName) {
        return switch (methodName) {
            case "AddFriend" -> "add";
            case "RemoveFriend" -> "remove";
            default -> throw new IllegalStateException("Unexpected value: " + methodName);
        };
    }
}
