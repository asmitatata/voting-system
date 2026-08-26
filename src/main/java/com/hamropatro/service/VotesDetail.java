package com.hamropatro.service;

import com.hamropatro.dto.votesDto.VotesDto;

import java.util.UUID;

public interface VotesDetail {
    void castVote(VotesDto voteDto);

    long countByElectionId(UUID electionId);

    long countByCandidateId(UUID electionId, UUID candidateId);
}
