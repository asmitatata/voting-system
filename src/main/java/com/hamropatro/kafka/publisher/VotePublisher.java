package com.hamropatro.kafka.publisher;

import com.hamropatro.VoteEvent;
import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.kafka.topic.KafkaTopic;
import jakarta.inject.Singleton;

@Singleton
public class VotePublisher {

    private final TopicProtoPublisher<VoteEvent> topicProtoPublisher;

    public VotePublisher(
            TopicProtoPublisher<VoteEvent> topicProtoPublisher
    ) {
        this.topicProtoPublisher = topicProtoPublisher;
    }

    public void publishVote(VotesDto dto) {

        VoteEvent event = VoteEvent.newBuilder()
                .setUserId(dto.userId().toString())
                .setElectionId(dto.electionId().toString())
                .setVoteDate(dto.voteDate().toString())
                .setCandidateId(dto.candidateId().toString())
                .setVoteCount(dto.voteCount())
                .setCreatedAt(dto.createdAt())
                .setUpdatedAt(dto.updatedAt())
                .build();

        topicProtoPublisher.publish(
                KafkaTopic.VOTE_CAST,
                dto.userId().toString(),
                event
        );
    }
}