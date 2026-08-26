package com.hamropatro.validation;

import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.exceptions.ElectionNotActiveException;
import com.hamropatro.exceptions.ElectionNotFoundException;
import com.hamropatro.repository.VoteRepository;
import com.hamropatro.service.ElectionDetail;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@Named("VotesValidator")
class VotesValidator implements Validate<VotesDto> {
    private final ElectionDetail electionDetail;
    private final VoteRepository voteRepository;

    public VotesValidator(ElectionDetail electionDetail, VoteRepository voteRepository) {
        this.electionDetail = electionDetail;
        this.voteRepository = voteRepository;
    }

    @Override
    public void validate(VotesDto request) {
        var election = electionDetail.findById(request.electionId());

        if (election.isEmpty()) {
            throw new ElectionNotFoundException(
                    "Election not found"
            );
        }
        long now = System.currentTimeMillis();

        if (now < election.get().startAt()) {
            throw new ElectionNotActiveException(
                    "Election has not started yet"
            );
        }

        if (now >= election.get().endAt()) {
            throw new ElectionNotActiveException(
                    "Election has already ended"
            );
        }
        if (request.voteCount() <= 0) {
            throw new IllegalArgumentException(
                    "Vote count must be greater than zero"
            );
        }

        int currentTotal = voteRepository.getExistingVoteCount(
                request.userId(),
                request.electionId(),
                request.voteDate(),
                request.candidateId()
        );

        int newTotal = currentTotal + request.voteCount();

        if (newTotal > election.get().maxVotes()) {
            throw new IllegalArgumentException(
                    "Vote limit exceeded"
            );
        }
    }

    @Override
    public void validateUpdate(VotesDto request) {

    }
}
