package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.entity.UserEntity;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class VoterRepository {
    private final CqlSession session;

    public VoterRepository(CqlSession session) {
        this.session = session;
    }

    public UserEntity findById(UUID userId) {
        String query =
                "SELECT * FROM voting_system.users WHERE user_id=? ";

        Row row = session.execute(
                SimpleStatement.newInstance(query, userId)
        ).one();

        if (row == null) {
            return null;
        }

        return new UserEntity(
                row.getUuid("user_id"),
                row.getString("email"),
                row.getString("full_name")
        );
    }

    public List<UserEntity> findAll() {
        String query =
                "SELECT * FROM voting_system.users ";
        List<Row> rows = session.execute(
                SimpleStatement.newInstance(query)
        ).all();

        List<UserEntity> voters = new ArrayList<>();

        for (Row row : rows) {
            voters.add(
                    UserEntity.builder()
                            .userId(row.getUuid("user_id"))
                            .name(row.getString("full_name"))
                            .email(row.getString("email"))
                            .build()
            );
        }

        return voters;
    }

    public boolean exists(UUID userId) {
        String query =
                "SELECT user_id FROM voting_system.users WHERE user_id=?";

        Row row = session.execute(
                SimpleStatement.newInstance(query, userId)
        ).one();

        return row != null;
    }
}
