package com.Titarenko.Consumers;

import com.Titarenko.Models.FriendListModifyEvent;
import com.Titarenko.Models.UserCreationEvent;
import com.Titarenko.Repositories.FriendListModifyRepository;
import com.Titarenko.Repositories.UserCreationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(
        topics = "client-topic",
        groupId = "user-actions-group",
        containerFactory = "userKafkaListenerContainerFactory"
)
public class UserActionsConsumer {
    private final UserCreationRepository userCreationRepository;
    private final FriendListModifyRepository friendListModifyRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserActionsConsumer(UserCreationRepository userCreationRepository,
                               FriendListModifyRepository friendListModifyRepository) {
        this.userCreationRepository = userCreationRepository;
        this.friendListModifyRepository = friendListModifyRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "client-topic", groupId = "user-actions-group")
    public void listen(ConsumerRecord<String, String> record) {
        String key = record.key();
        String messageValue = record.value();

        try {
            UserCreationEvent userCreationMessage = objectMapper.readValue(messageValue, UserCreationEvent.class);

            if (userCreationMessage.getName() != null) {
                UserCreationEvent event = new UserCreationEvent(
                        userCreationMessage.getId(),
                        userCreationMessage.getLogin(),
                        userCreationMessage.getName(),
                        userCreationMessage.getAge(),
                        userCreationMessage.getGender(),
                        userCreationMessage.getHairColour(),
                        userCreationMessage.getDate()
                );
                userCreationRepository.save(event);
                return;
            }
        } catch (Exception e) {
            try {
                FriendListModifyEvent friendListModifyMessage = objectMapper.readValue(messageValue,
                        FriendListModifyEvent.class);
                FriendListModifyEvent event = new FriendListModifyEvent(
                        friendListModifyMessage.getOperation(),
                        friendListModifyMessage.getFriendLogin(),
                        key,
                        friendListModifyMessage.getDate()
                );
                friendListModifyRepository.save(event);
            } catch (Exception ex) {
                return;
            }
        }
    }
}
