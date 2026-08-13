package com.hamropatro.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.hamropatro.dto.UserDto;
import com.hamropatro.entity.UserEntity;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class UserRepository {

    private final CqlSession session;

    public UserRepository(CqlSession session) {
        this.session = session;
    }

    public void register(UserEntity user) {
        String query =
                "INSERT INTO voting_system.users(user_id,email,full_name,role,password,is_active,created_at,updated_at) VALUES (?,?,?,?,?,?,?,?)";

        session.execute(
                SimpleStatement.newInstance(
                        query,
                        user.getUserId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole(),
                        user.getPassword(),
                        user.isActive(),
                        user.getCreated_at(),
                        user.getUpdated_at()
                )
        );
    }

    public UserDto findByEmail(String email) {

        String query =
                "SELECT * FROM voting_system.users WHERE email = ?";

        Row row = session.execute(
                SimpleStatement.newInstance(query, email)
        ).one();

        if (row == null) {
            return null;
        }
        return new UserDto(
                row.getUuid("user_id"),
                row.getString("email"),
                row.getString("full_name"),
                row.getString("password"),
                row.getString("role"),
                row.getBoolean("is_active"),
                row.getInstant("created_at").toEpochMilli(),
                row.getInstant("updated_at").toEpochMilli()
        );
    }

    public UserDto findById(UUID userId) {

        String query =
                "SELECT * FROM voting_system.users WHERE user_id = ?";

        Row row = session.execute(
                SimpleStatement.newInstance(query, userId)
        ).one();

        if (row == null) {
            return null;
        }
        return new UserDto(
                row.getUuid("user_id"),
                row.getString("email"),
                row.getString("full_name"),
                row.getString("password"),
                row.getString("role"),
                row.getBoolean("is_active"),
                row.getInstant("created_at").toEpochMilli(),
                row.getInstant("updated_at").toEpochMilli()
        );
    }
}
