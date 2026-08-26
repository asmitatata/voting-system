package com.hamropatro.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hamropatro.CreateElectionRequest;
import com.hamropatro.kafka.topic.KafkaTopic;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.extern.slf4j.Slf4j;

@KafkaListener(
        groupId = "notification-service"
)
@Slf4j
public class NotificationConsumer {

    @Topic(KafkaTopic.ELECTION_CREATED)
    public void consume(byte[] message) {

        try {
            CreateElectionRequest request =
                    CreateElectionRequest.parseFrom(message);

            log.info(
                    "Received election created: {}",
                    request.getElectionName()
            );

        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to deserialize election event", e);
        }
    }
}
