package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.VotesEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static com.hamropatro.query.VotesQuery.*;

@Singleton
public class VoteRepository {
    private final CqlSession session;

    public VoteRepository(CqlSession session) {
        this.session = session;
    }

    public void saveVotes(VotesEntity vote, int newTotal) {
        session.execute(
                SimpleStatement.newInstance(
                        SAVE_VOTE,
                        newTotal,
                        Instant.ofEpochMilli(vote.getCreatedAt()),
                        Instant.ofEpochMilli(vote.getUpdatedAt()),
                        vote.getUserId(),
                        vote.getElectionId(),
                        vote.getVoteDate(),
                        vote.getCandidateId()
                )
        );
    }

    public boolean updateUserVote(
            VotesEntity vote,
            int newTotal,
            int currentTotal
    ) {
        var statement = SimpleStatement.newInstance(
                UPDATE_USER_VOTE,
                newTotal,
                vote.getUpdatedAt(),
                vote.getUserId(),
                vote.getElectionId(),
                vote.getVoteDate(),
                vote.getCandidateId(),
                currentTotal
        );

        return session.execute(statement).wasApplied();
    }

    public void updateVoteCountByCandidate(UUID electionId, UUID candidateId, long voteCount) {
        session.execute(
                SimpleStatement.newInstance(
                        UPDATE_VOTE_COUNT_BY_CANDIDATE_QUERY,
                        voteCount,
                        electionId,
                        candidateId
                )
        );

    }

    public void updateVoteCountByElection(UUID electionId, long voteCount) {
        session.execute(
                SimpleStatement.newInstance(
                        UPDATE_VOTE_COUNT_BY_ELECTION_QUERY,
                        voteCount,
                        electionId
                )
        );
    }

    public long getCandidateCount(UUID electionId, UUID candidateId) {
        Row row = session.execute(SimpleStatement.newInstance(GET_VOTE_COUNT_BY_CANDIDATE_QUERY, electionId, candidateId)).one();

        return row == null ? 0L : row.getLong("vote_count");
    }

    public long getElectionCount(UUID electionId) {
        Row row = session.execute(SimpleStatement.newInstance(GET_VOTE_COUNT_BY_ELECTION_QUERY, electionId)).one();

        return row == null ? 0L : row.getLong("vote_count");
    }

    public int getExistingVoteCount(UUID userId, UUID electionId, LocalDate voteDate, UUID candidateId) {
        var statement = SimpleStatement.newInstance(
                GET_USER_VOTE_COUNT_QUERY,
                userId,
                electionId,
                voteDate,
                candidateId
        );

        Row row = session.execute(statement).one();
        return (row != null) ? row.getInt("vote_count") : 0;
    }

}
