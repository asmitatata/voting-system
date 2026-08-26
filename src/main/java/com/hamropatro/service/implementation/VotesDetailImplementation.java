package com.hamropatro.service.implementation;

import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.entity.VotesEntity;
import com.hamropatro.exceptions.ElectionNotFoundException;
import com.hamropatro.mapper.VotesMapper;
import com.hamropatro.repository.VoteRepository;
import com.hamropatro.service.ElectionDetail;
import com.hamropatro.service.VotesDetail;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class VotesDetailImplementation implements VotesDetail {
    private final VotesMapper votesMapper;
    private final VoteRepository voteRepository;
    private final ElectionDetail electionDetail;

    public VotesDetailImplementation(VotesMapper votesMapper, VoteRepository voteRepository, ElectionDetail electionDetail) {
        this.votesMapper = votesMapper;
        this.voteRepository = voteRepository;
        this.electionDetail = electionDetail;
    }

    @Override
    public void castVote(VotesDto voteDto) {
        var election = electionDetail.findById(voteDto.electionId());
        if (election.isEmpty()) {
            throw new ElectionNotFoundException("Election not found");
        }
        var voteCount = voteDto.voteCount();
        if (voteCount <= 0) {
            throw new IllegalArgumentException(
                    "Vote count must be greater than zero"
            );
        }
        int currentTotal = voteRepository.getExistingVoteCount(
                voteDto.userId(),
                voteDto.electionId(),
                voteDto.voteDate(),
                voteDto.candidateId()
        );
        int newTotal = currentTotal + voteCount;

        if (newTotal > election.get().maxVotes()) {
            throw new IllegalArgumentException("Vote limit exceeded");
        }

        VotesEntity entity = votesMapper.fromVotesDtoToVotesEntity(voteDto);

        voteRepository.saveVotes(entity, newTotal);

        voteRepository.updateVoteCountByCandidate(
                voteDto.electionId(),
                voteDto.candidateId(),
                voteCount
        );

        voteRepository.updateVoteCountByElection(
                voteDto.electionId(),
                voteCount
        );
    }

    @Override
    public long countByElectionId(UUID electionId) {
        return voteRepository.getElectionCount(electionId);
    }

    @Override
    public long countByCandidateId(UUID electionId, UUID candidateId) {
        return voteRepository.getCandidateCount(electionId, candidateId);
    }
}