package com.hamropatro.mapper;

import com.hamropatro.Candidates;
import com.hamropatro.dto.CandidateDto;
import com.hamropatro.entity.CandidateEntity;
import jakarta.inject.Singleton;

import java.util.List;
@Singleton
public class CandidateMapper {
    public CandidateEntity fromCandidateDtotoCandidateEntity(CandidateDto candidateDto) {
        return new CandidateEntity(
                candidateDto.candidate_id(),
                candidateDto.election_id(),
                candidateDto.full_name(),
                candidateDto.created_at(),
                candidateDto.updated_at()
        );
    }

    public CandidateDto fromCandidateEntitytoCandidateDto(CandidateEntity candidateEntity) {
        return new CandidateDto(
                candidateEntity.getCandidate_id(),
                candidateEntity.getElection_id(),
                candidateEntity.getFull_name(),
                candidateEntity.getCreated_at(),
                candidateEntity.getUpdated_at()
        );
    }

    public static Candidates fromCandidateDtotoCandidates(CandidateDto candidateDto) {
        return Candidates.newBuilder()
                .setCandidateId(String.valueOf(candidateDto.candidate_id()))
                .setCandidateName(candidateDto.full_name())
                .setElectionId(String.valueOf(candidateDto.election_id()))
                .setCreatedAt(candidateDto.created_at())
                .setUpdatedAt(candidateDto.updated_at())
                .build();
    }

    public List<CandidateDto> fromCandidateEntitiesToCandidateDto(
            List<CandidateEntity> candidateEntities) {

        return candidateEntities.stream()
                .map(this::fromCandidateEntitytoCandidateDto)
                .toList();
    }

}
