package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import jnr.ffi.annotations.In;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class VotesEntity {
    @PartitionKey
    private UUID electionId;

    @ClusteringColumn
    private UUID voteId;

    private UUID userId;
    private UUID candidateId;
    private long createdAt;
    private long updatedAt;

    public VotesEntity(UUID electionId, UUID voteId, UUID candidateId, UUID userId, long createdAt,long updatedAt){
        this.electionId=electionId;
        this.voteId=voteId;
        this.userId=userId;
        this.candidateId=candidateId;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }
}
