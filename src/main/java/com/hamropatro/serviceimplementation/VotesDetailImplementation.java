package com.hamropatro.serviceimplementation;

import com.hamropatro.dto.VotesDto;
import com.hamropatro.entity.CandidateEntity;
import com.hamropatro.entity.ElectionEntity;
import com.hamropatro.entity.VotesEntity;
import com.hamropatro.exceptions.CandidateNotFoundException;
import com.hamropatro.exceptions.ElectionNotFoundException;
import com.hamropatro.mapper.VotesMapper;
import com.hamropatro.repository.VotesRepository;
import com.hamropatro.service.VotesDetail;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class VotesDetailImplementation implements VotesDetail {
    private final VotesMapper votesMapper;
    private final VotesRepository votesRepository;

    public VotesDetailImplementation(VotesMapper votesMapper, VotesRepository votesRepository) {
        this.votesMapper = votesMapper;
        this.votesRepository = votesRepository;
    }

    @Override
    public void castVote(VotesDto voteDto) {
        VotesEntity entity = votesMapper.fromVotesDtoToVotesEntity(voteDto);
        entity.setVoteId(UUID.randomUUID());
        votesRepository.castVote(entity);
    }

    @Override
    public void updateVote(UUID voteId, VotesDto voteDto) {
        var entity = votesMapper.fromVotesDtoToVotesEntity(voteDto);

        entity.setVoteId(voteId);
        entity.setUpdatedAt(System.currentTimeMillis());
        votesRepository.updateVote(voteId, entity);
    }

    @Override
    public boolean hasVoted(UUID electionId, UUID userId) {
        return votesRepository.hasVoted(electionId, userId);
    }

    @Override
    public List<VotesDto> findVotesByElection(UUID electionId) {
        List<VotesEntity> entity = votesRepository.findVotesByElection(electionId);
        return (votesMapper.fromVotesEntitiesToVotesDto(entity));
    }

    @Override
    public long countByElectionId(UUID electionId) {
        return votesRepository.countByElectionId(electionId);
    }

    @Override
    public long countByCandidateId(UUID candidateId) {
        return votesRepository.countByCandidateId(candidateId);
    }
}
