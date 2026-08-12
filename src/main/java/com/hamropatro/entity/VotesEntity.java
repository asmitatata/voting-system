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
    @PartitionKey(0)
    private UUID userId;
    @PartitionKey(1)
    private UUID electionId;

    @ClusteringColumn
    private UUID voteId;


    private UUID candidateId;
    private int voteCount;
    private long createdAt;
    private long updatedAt;

    public VotesEntity(UUID userId, UUID electionId, UUID voteId, UUID candidateId, int voteCount,long createdAt,long updatedAt){
        this.electionId=electionId;
        this.voteId=voteId;
        this.userId=userId;
        this.candidateId=candidateId;
        this.voteCount=voteCount;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }
}
