package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.CandidateEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class CandidateRepository {
    private final CqlSession session;

    public CandidateRepository(CqlSession session) {
        this.session = session;
    }

    public void save(CandidateEntity candidate) {
        String query =
                "INSERT INTO voting_system.candidate(candidate_id,election_id,full_name,created_at,updated_at) VALUES (?,?,?,?,?)";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        candidate.getCandidate_id(),
                        candidate.getElection_id(),
                        candidate.getFull_name(),
                        Instant.ofEpochMilli(candidate.getCreated_at()),
                        Instant.ofEpochMilli(candidate.getUpdated_at())
                )
        );
    }

    public CandidateEntity findById(UUID candidateId) {
        String query =
                "SELECT * FROM voting_system.candidate WHERE candidate_id=? ";
        Row row = session.execute(
                SimpleStatement.newInstance(query, candidateId)
        ).one();

        if (row == null) {
            return null;
        }

        return new CandidateEntity(
                row.getUuid("candidate_id"),
                row.getUuid("election_id"),
                row.getString("full_name"),
                row.getInstant("created_at").toEpochMilli(),
                row.getInstant("updated_at").toEpochMilli()
        );
    }

    public List<CandidateEntity> findAll() {
        String query =
                "SELECT * FROM voting_system.candidate ";
        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query)
        ).all();

        List<CandidateEntity> candidates = new ArrayList<>();

        for (Row row : rows) {
            long createdAt = row.getInstant("created_at").toEpochMilli();
            long updatedAt = row.getInstant("updated_at").toEpochMilli();
            candidates.add(
                    CandidateEntity.builder()
                            .candidate_id(row.getUuid("candidate_id"))
                            .election_id(row.getUuid("election_id"))
                            .full_name(row.getString("full_name"))
                            .created_at(createdAt)
                            .updated_at(updatedAt)
                            .build()
            );
        }

        return candidates;
    }

    public List<CandidateEntity> findByElectionId(UUID electionId) {
        String query =
                "SELECT * FROM voting_system.candidate WHERE election_id=? ";

        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query,electionId)
        ).all();

        List<CandidateEntity> candidates = new ArrayList<>();

        for (Row row : rows) {
            long createdAt = row.getInstant("created_at").toEpochMilli();
            long updatedAt = row.getInstant("updated_at").toEpochMilli();
            candidates.add(
                    CandidateEntity.builder()
                            .candidate_id(row.getUuid("candidate_id"))
                            .election_id(row.getUuid("election_id"))
                            .full_name(row.getString("full_name"))
                            .created_at(createdAt)
                            .updated_at(updatedAt)
                            .build()
            );
        }
        return candidates;
    }

    public void update(UUID candidateId, CandidateEntity candidate) {
        String query =
                "UPDATE voting_system.candidate SET full_name=?, election_id=?, updated_at=? WHERE candidate_id=?;";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        candidate.getFull_name(),
                        candidate.getElection_id(),
                        Instant.ofEpochMilli(candidate.getUpdated_at()),
                        candidateId
                )
        );
    }

    public void delete(UUID candidateId) {
        String query =
                "DELETE FROM voting_system.candidate WHERE candidate_id=? ";
        session.execute(
                SimpleStatement.newInstance(query, candidateId)
        ).one();
    }

    public boolean exists(UUID candidateId) {
        String query =
                "SELECT candidate_id FROM voting_system.candidate WHERE candidate_id=?";

        Row row = session.execute(
                SimpleStatement.newInstance(query, candidateId)
        ).one();

        return row != null;
    }
}
