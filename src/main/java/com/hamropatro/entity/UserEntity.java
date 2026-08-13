package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserEntity {
    @PartitionKey
    private UUID userId;

    private String email;
    private String name;
    private String password;
    private String role;
    private  boolean isActive;
    private long created_at;
    private long updated_at;

    public UserEntity(UUID userId, String email, String name, String password, String role,boolean isActive,long created_at,long updated_at) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
        this.password = password;
        this.isActive=isActive;
        this.created_at =created_at;
        this.updated_at =updated_at;
    }

    public UserEntity(String email, String password) {
        this.email=email;
        this.password=password;
    }

    public UserEntity(UUID userId, String email, String fullName) {
    }
}
