package com.hamropatro.kafka.publisher;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.hamropatro.dto.votesDto.VotesDto;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

@Singleton
@Slf4j
@Primary
public class TopicProtoPublisher<T extends GeneratedMessage> {

    private final Producer<String, byte[]> kafkaProducer;
    private final Producer<String, VotesDto> producer;

    public TopicProtoPublisher(
            @KafkaClient Producer<String, byte[]> kafkaProducer, Producer<String, VotesDto> producer
    ) {
        this.kafkaProducer = kafkaProducer;
        this.producer = producer;
    }

    public Future<RecordMetadata> publish(
            String topic,
            String key,
            T messageBody
    ) {
        log.info("Publishing to topic => {}", topic);

        byte[] messageBytes = messageBody.toByteArray();

        var record = new ProducerRecord<>(
                topic,
                key,
                messageBytes
        );

        return kafkaProducer.send(record);
    }
}
