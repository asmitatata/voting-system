package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.CandidateEntity;
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
                "INSERT INTO voting_system.votes(election_id,vote_id,user_id,candidate_id,created_at,updated_at) VALUES (?,?,?,?,?,?)";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        vote.getElectionId(),
                        vote.getVoteId(),
                        vote.getUserId(),
                        vote.getCandidateId(),
                        Instant.ofEpochMilli(vote.getCreatedAt()),
                        Instant.ofEpochMilli(vote.getUpdatedAt())
                )
        );
    }

    public void updateVote(UUID voteId, VotesEntity vote) {
        String query =
                "UPDATE voting_system.votes SET user_id=?, candidate_id=?, updated_at=? WHERE election_id = ? AND vote_id = ?;";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        vote.getUserId(),
                        vote.getCandidateId(),
                        Instant.ofEpochMilli(vote.getUpdatedAt()),
                        vote.getElectionId(),
                        voteId
                )
        );
    }

    public boolean hasVoted(UUID electionId, UUID userId) {
        String query =
                "SELECT vote_id FROM voting_system.votes_by_user WHERE election_id=? AND user_id=?";

        Row row = session.execute(
                SimpleStatement.newInstance(query, electionId,userId)
        ).one();

        return row != null;
    }

    public List<VotesEntity> findVotesByElection(UUID electionId) {
        String query =
                "SELECT * FROM voting_system.votes WHERE election_id=? ";

        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query,electionId)
        ).all();

        List<VotesEntity> votes = new ArrayList<>();

        for (Row row : rows) {
            long createdAt = row.getInstant("created_at").toEpochMilli();
            long updatedAt = row.getInstant("updated_at").toEpochMilli();
            votes.add(
                    VotesEntity.builder()
                            .electionId(row.getUuid("election_id"))
                            .voteId(row.getUuid("vote_id"))
                            .userId(row.getUuid("user_id"))
                            .candidateId(row.getUuid("candidate_id"))
                            .createdAt(createdAt)
                            .updatedAt(updatedAt)
                            .build()
            );
        }
        return votes;
    }

    public long countByElectionId(UUID electionId) {
        var statement = SimpleStatement.builder(
                        "SELECT COUNT(*) FROM voting_system.votes WHERE election_id = ?"
                )
                .addPositionalValue(electionId)
                .build();

        var row = session.execute(statement).one();

        return row != null ? row.getLong(0) : 0L;
    }

    public long countByCandidateId(UUID candidateId) {
        var statement = SimpleStatement.builder(
                        "SELECT COUNT(*) FROM voting_system.votes_by_candidate WHERE candidate_id = ?"
                )
                .addPositionalValue(candidateId)
                .build();

        var row = session.execute(statement).one();

        return row != null ? row.getLong(0) : 0L;
    }


}
