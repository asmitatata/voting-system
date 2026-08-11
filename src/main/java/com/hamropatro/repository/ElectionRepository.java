package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.ElectionEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class ElectionRepository {
    private final CqlSession session;

    public ElectionRepository(CqlSession session) {
        this.session = session;
    }

    public void save(ElectionEntity election) {
        String query =
                "INSERT INTO voting_system.election(election_id,election_name,voting_type,max_votes,start_at,end_at,created_at,updated_at,status) VALUES (?,?,?,?,?,?,?,?,?)";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        election.getElection_id(),
                        election.getElection_name(),
                        election.getVoting_type(),
                        election.getMax_votes(),
                        Instant.ofEpochMilli(election.getStart_at()),
                        Instant.ofEpochMilli(election.getEnd_at()),
                        Instant.ofEpochMilli(election.getCreated_at()),
                        Instant.ofEpochMilli(election.getUpdated_at()),
                        election.getStatus()
                )
        );
    }

    public ElectionEntity findById(UUID electionId) {
        String query =
                "SELECT * FROM voting_system.election WHERE election_id=? ";
        Row row = session.execute(
                SimpleStatement.newInstance(query, electionId)
        ).one();

        if (row == null) {
            return null;
        }

        return new ElectionEntity(
                row.getUuid("election_id"),

                row.getString("election_name"),
                row.getString("voting_type"),
                row.getInt("max_votes"),
                row.getInstant("start_at").toEpochMilli(),
                row.getInstant("end_at").toEpochMilli(),
                row.getInstant("created_at").toEpochMilli(),
                row.getInstant("updated_at").toEpochMilli(),
                row.getString("status")
        );
    }

    public List<ElectionEntity> findAll() {
        String query =
                "SELECT * FROM voting_system.election ";
        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query)
        ).all();

        List<ElectionEntity> elections = new ArrayList<>();

        for (Row row : rows) {
            long startAt = row.getInstant("start_at").toEpochMilli();
            long endAt = row.getInstant("end_at").toEpochMilli();
            long createdAt = row.getInstant("created_at").toEpochMilli();
            long updatedAt = row.getInstant("updated_at").toEpochMilli();

            elections.add(
                    ElectionEntity.builder()
                            .election_id(row.getUuid("election_id"))
                            .election_name(row.getString("election_name"))
                            .voting_type(row.getString("voting_type"))
                            .max_votes(row.getInt("max_votes"))
                            .start_at(startAt)
                            .end_at(endAt)
                            .created_at(createdAt)
                            .updated_at(updatedAt)
                            .status(row.getString("status"))
                            .build()
            );
        }

        return elections;
    }

    public void update(UUID electionId, ElectionEntity election) {
        String query =
                "UPDATE voting_system.election SET election_name=?, voting_type=?, max_votes=?, start_at=?, end_at=?, updated_at=?, status=? WHERE election_id=?;";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        election.getElection_name(),
                        election.getVoting_type(),
                        election.getMax_votes(),
                        Instant.ofEpochMilli(election.getStart_at()),
                        Instant.ofEpochMilli(election.getEnd_at()),
                        Instant.ofEpochMilli(election.getUpdated_at()),
                        election.getStatus(),
                        electionId
                )
        );
    }

    public void delete(UUID electionId) {
        String query =
                "DELETE FROM voting_system.election WHERE election_id=? ";
        session.execute(
                SimpleStatement.newInstance(query, electionId)
        ).one();
    }

    public boolean exists(UUID electionId) {
        String query =
                "SELECT election_id FROM voting_system.election WHERE election_id=?";

        Row row = session.execute(
                SimpleStatement.newInstance(query, electionId)
        ).one();

        return row != null;
    }
}
