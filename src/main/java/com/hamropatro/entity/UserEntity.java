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

}
