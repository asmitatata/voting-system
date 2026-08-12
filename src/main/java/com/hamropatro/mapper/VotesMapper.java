package com.hamropatro.mapper;

import com.hamropatro.Votes;
import com.hamropatro.VotesByElectionId;
import com.hamropatro.dto.VotesDto;
import com.hamropatro.entity.VotesEntity;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class VotesMapper {

    public VotesEntity fromVotesDtoToVotesEntity(VotesDto votesDto){
        return new VotesEntity(
                votesDto.user_id(),
                votesDto.election_id(),
                votesDto.vote_id(),
                votesDto.candidate_id(),
                votesDto.vote_count(),
                votesDto.created_at(),
                votesDto.updated_at()
        );
    }

    public VotesDto fromVotesEntitytoVotesDto(VotesEntity votesEntity){
        return new VotesDto(
                votesEntity.getUserId(),
                votesEntity.getElectionId(),
                votesEntity.getVoteId(),
                votesEntity.getCandidateId(),
                votesEntity.getVoteCount(),
                votesEntity.getCreatedAt(),
                votesEntity.getUpdatedAt()
        );
    }

    public static Votes fromVotesDtotoVotes(VotesDto votesDto) {
        return Votes.newBuilder()
                .setElectionId(String.valueOf(votesDto.election_id()))
                .setVoteId(String.valueOf(votesDto.vote_id()))
                .setUserId(String.valueOf(votesDto.user_id()))
                .setCandidateId(String.valueOf(votesDto.candidate_id()))
                .setVoteCount(votesDto.vote_count())
                .setCreatedAt(votesDto.created_at())
                .setUpdatedAt(votesDto.updated_at())
                .build();
    }
    public static VotesByElectionId fromVotesDtotoVotesByElectionId(VotesDto votesDto) {
        return VotesByElectionId.newBuilder()
                .setElectionId(String.valueOf(votesDto.election_id()))
                .setCandidateId(String.valueOf(votesDto.candidate_id()))
                .setVoteCount(votesDto.vote_count())
                .build();
    }

    public List<VotesDto> fromVotesEntitiesToVotesDto(
            List<VotesEntity> votesEntities) {

        return votesEntities.stream()
                .map(this::fromVotesEntitytoVotesDto)
                .toList();
    }

}
