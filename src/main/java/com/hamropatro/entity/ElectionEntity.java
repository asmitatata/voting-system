package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ElectionEntity {
    @PartitionKey
    private UUID election_id;

    private String election_name;
    private String voting_type;
    private int max_votes;
    private long start_at;
    private long end_at;
    private long created_at;
    private long updated_at;
    private String status;

    public ElectionEntity(UUID election_id, String election_name, String voting_type, int max_votes, long start_at, long end_at, long created_at, long updated_at, String status){
        this.election_id=election_id;
        this.election_name=election_name;
        this.voting_type=voting_type;
        this.max_votes=max_votes;
        this.start_at=start_at;
        this.end_at=end_at;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.status=status;
    }
}
