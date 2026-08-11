package com.hamropatro.mapper;

import com.hamropatro.Votes;
import com.hamropatro.dto.VotesDto;
import com.hamropatro.entity.VotesEntity;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class VotesMapper {

    public VotesEntity fromVotesDtoToVotesEntity(VotesDto votesDto){
        return new VotesEntity(
                votesDto.election_id(),
                votesDto.vote_id(),
                votesDto.user_id(),
                votesDto.candidate_id(),
                votesDto.created_at(),
                votesDto.updated_at()
        );
    }

    public VotesDto fromVotesEntitytoVotesDto(VotesEntity votesEntity){
        return new VotesDto(
                votesEntity.getElectionId(),
                votesEntity.getVoteId(),
                votesEntity.getUserId(),
                votesEntity.getCandidateId(),
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
                .setCreatedAt(votesDto.created_at())
                .setUpdatedAt(votesDto.updated_at())
                .build();
    }

    public List<VotesDto> fromVotesEntitiesToVotesDto(
            List<VotesEntity> votesEntities) {

        return votesEntities.stream()
                .map(this::fromVotesEntitytoVotesDto)
                .toList();
    }

}
