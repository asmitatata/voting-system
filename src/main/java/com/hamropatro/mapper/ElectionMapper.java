package com.hamropatro.mapper;

import com.hamropatro.Elections;
import com.hamropatro.dto.ElectionDto;
import com.hamropatro.entity.ElectionEntity;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ElectionMapper {
    public ElectionEntity fromElectionDtotoElectionEntity(ElectionDto electionDto) {
        return new ElectionEntity(
                electionDto.election_id(),
                electionDto.election_name(),
                electionDto.voting_type(),
                electionDto.max_votes(),
                electionDto.start_at(),
                electionDto.end_at(),
                electionDto.created_at(),
                electionDto.updated_at(),
                electionDto.status());
    }

    public ElectionDto fromElectionEntitytoElectionDto(ElectionEntity electionEntity){
        return new ElectionDto(
                electionEntity.getElection_id(),
                electionEntity.getElection_name(),
                electionEntity.getVoting_type(),
                electionEntity.getMax_votes(),
                electionEntity.getStart_at(),
                electionEntity.getEnd_at(),
                electionEntity.getCreated_at(),
                electionEntity.getUpdated_at(),
                electionEntity.getStatus()
        );
    }
    public static Elections fromElectionDtotoElections(ElectionDto electionDto) {
        return Elections.newBuilder()
                .setElectionId(String.valueOf(electionDto.election_id()))
                .setElectionName(electionDto.election_name())
                .setVotingType(electionDto.voting_type())
                .setMaxVotes(electionDto.max_votes())
                .setStartAt(electionDto.start_at())
                .setEndAt(electionDto.end_at())
                .setCreatedAt(electionDto.created_at())
                .setUpdatedAt(electionDto.updated_at())
                .setStatus(electionDto.status())
                .build();
    }

    public List<ElectionDto> fromElectionEntitiesToElectionDto(
            List<ElectionEntity> electionEntities) {

        return electionEntities.stream()
                .map(this::fromElectionEntitytoElectionDto)
                .toList();
    }

}
