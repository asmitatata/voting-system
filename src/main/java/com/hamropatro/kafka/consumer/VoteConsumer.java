package com.hamropatro.kafka.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hamropatro.VoteEvent;
import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.kafka.topic.KafkaTopic;
import com.hamropatro.service.VotesDetail;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

@KafkaListener(groupId = "vote-processing-service")
@Slf4j
public class VoteConsumer {

    private final VotesDetail votesDetail;

    public VoteConsumer(VotesDetail votesDetail) {
        this.votesDetail = votesDetail;
    }

    @Topic(KafkaTopic.VOTE_CAST)
    public void consume(byte[] message) {

        try {
            VoteEvent event = VoteEvent.parseFrom(message);

            log.info(
                    "Received vote event: user={}, election={}, candidate={}, votes={}",
                    event.getUserId(),
                    event.getElectionId(),
                    event.getCandidateId(),
                    event.getVoteCount()
            );
            VotesDto dto = new VotesDto(
                    UUID.fromString(event.getUserId()),
                    UUID.fromString(event.getElectionId()),
                    LocalDate.parse(event.getVoteDate()),
                    UUID.fromString(event.getCandidateId()),
                    event.getVoteCount(),
                    event.getCreatedAt(),
                    event.getUpdatedAt()
            );
            votesDetail.castVote(dto);

        } catch (InvalidProtocolBufferException e) {

            log.error("Failed to deserialize VoteEvent", e);

            throw new RuntimeException(
                    "Invalid VoteEvent protobuf message",
                    e
            );

        } catch (Exception e) {

            log.error("Failed to process vote", e);

            throw new RuntimeException(e);
        }
    }
}