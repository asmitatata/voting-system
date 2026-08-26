package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class VotesEntity {
    @PartitionKey(0)
    private UUID userId;
    @PartitionKey(1)
    private UUID electionId;
    @PartitionKey(2)
    private LocalDate voteDate;
    @PartitionKey(3)
    private UUID candidateId;


    private int voteCount;
    private long createdAt;
    private long updatedAt;

    public VotesEntity(UUID userId, UUID electionId, LocalDate voteDate, UUID candidateId, int voteCount, long createdAt, long updatedAt) {
        this.electionId = electionId;
        this.voteDate = voteDate;
        this.userId = userId;
        this.candidateId = candidateId;
        this.voteCount = voteCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
