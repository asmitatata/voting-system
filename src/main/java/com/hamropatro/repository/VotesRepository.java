package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.VotesEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class VotesRepository {
    private final CqlSession session;

    public VotesRepository(CqlSession session) {
        this.session = session;
    }

    public void castVote(VotesEntity vote) {
        String query =
                "INSERT INTO voting_system.votes(user_id, election_id, vote_id, candidate_id,vote_count, created_at, updated_at) VALUES (?,?,?,?,?,?,?)";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        vote.getUserId(),
                        vote.getElectionId(),
                        vote.getVoteId(),
                        vote.getCandidateId(),
                        vote.getVoteCount(),
                        Instant.ofEpochMilli(vote.getCreatedAt()),
                        Instant.ofEpochMilli(vote.getUpdatedAt())
                )
        );
        String counterQuery =
                "UPDATE voting_system.votes_by_candidate SET vote_count = vote_count + ? WHERE election_id = ? AND candidate_id = ?";

        session.execute(
                SimpleStatement.newInstance(
                        counterQuery,
                        (long)vote.getVoteCount(),
                        vote.getElectionId(),
                        vote.getCandidateId()
                )
        );
    }

    public void updateVote(UUID voteId, VotesEntity vote) {
        String findQuery =
                "SELECT candidate_id, vote_count FROM voting_system.votes WHERE user_id = ? AND election_id = ? AND vote_id = ?";

        Row existingVote = session.execute(
                SimpleStatement.newInstance(
                        findQuery,
                        vote.getUserId(),
                        vote.getElectionId(),
                        voteId
                )
        ).one();

        if (existingVote == null) {
            throw new IllegalArgumentException(
                    "Vote not found: " + voteId
            );
        }

        UUID oldCandidateId =
                existingVote.getUuid("candidate_id");

        int oldVoteCount =
                existingVote.getInt("vote_count");

        String updateQuery =
                "UPDATE voting_system.votes SET candidate_id = ?,vote_count =?, updated_at = ? WHERE user_id = ? AND election_id = ? AND vote_id = ?";
        session.execute(
                SimpleStatement.newInstance(
                        updateQuery,
                        vote.getCandidateId(),
                        vote.getVoteCount(),
                        Instant.ofEpochMilli(vote.getUpdatedAt()),
                        vote.getUserId(),
                        vote.getElectionId(),
                        voteId
                )
        );
        /*
         * Remove the old vote count from the old candidate.
         */
        String decrementQuery =
                "UPDATE voting_system.votes_by_candidate SET vote_count = vote_count - ? WHERE election_id = ? AND candidate_id = ?";

        session.execute(
                SimpleStatement.newInstance(
                        decrementQuery,
                        oldVoteCount,
                        vote.getElectionId(),
                        oldCandidateId
                )
        );


        /*
         * Add the new vote count to the new candidate.
         */
        String incrementQuery =
                "UPDATE voting_system.votes_by_candidate SET vote_count = vote_count + ? WHERE election_id = ? AND candidate_id = ?";

        session.execute(
                SimpleStatement.newInstance(
                        incrementQuery,
                        vote.getUserId(),
                        vote.getVoteCount(),
                        vote.getElectionId(),
                        vote.getCandidateId()
                )
        );
    }

    public boolean hasVoted(UUID electionId, UUID userId) {
        String query =
                "SELECT vote_id FROM voting_system.votes WHERE user_id = ? AND election_id = ? LIMIT 1";
        Row row = session.execute(
                SimpleStatement.newInstance(query, userId, electionId)
        ).one();

        return row != null;
    }

    public List<VotesEntity> findVotesByElection(UUID electionId) {
        String query =
                "SELECT * FROM voting_system.votes_by_candidate WHERE election_id=?";

        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query, electionId)
        ).all();

        List<VotesEntity> votes = new ArrayList<>();

        for (Row row : rows) {
            votes.add(
                    VotesEntity.builder()
                            .electionId(row.getUuid("election_id"))
                            .candidateId(row.getUuid("candidate_id"))
                            .voteCount((int) row.getLong("vote_count"))
                            .build()
            );
        }
        return votes;
    }

    public long countByElectionId(UUID electionId) {

        String query =
                "SELECT vote_count FROM voting_system.votes_by_candidate WHERE election_id = ?";

        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query, electionId)
        ).all();

        long total = 0;

        for (Row row : rows) {
            total += row.getLong("vote_count");
        }

        return total;
    }

    public long countByCandidateId(UUID candidateId) {
        var statement = SimpleStatement.builder(
                        "SELECT vote_count FROM voting_system.votes_by_candidate WHERE candidate_id = ?;"
                )
                .addPositionalValue(candidateId)
                .build();

        var row = session.execute(statement).one();

        return row != null ? row.getLong(0) : 0L;
    }


}
