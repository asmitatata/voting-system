package com.hamropatro.service;

import com.hamropatro.dto.VotesDto;

import java.util.List;
import java.util.UUID;

public interface VotesDetail {
    void castVote(VotesDto voteDto);

    void updateVote(UUID voteId,VotesDto voteDto);

    boolean hasVoted(UUID electionId, UUID userId);

    List<VotesDto> findVotesByElection(UUID electionId);

    long countByElectionId(UUID electionId);

    long countByCandidateId(UUID candidateId);
}
