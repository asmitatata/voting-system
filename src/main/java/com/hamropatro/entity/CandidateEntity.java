package com.hamropatro.entity;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CandidateEntity {
    @PartitionKey
    private UUID candidate_id;

    @ClusteringColumn
    private UUID election_id;

    private String full_name;
    private long created_at;
    private long updated_at;


    public CandidateEntity(UUID candidate_id,UUID election_id,String full_name,long created_at,long updated_at){
        this.candidate_id=candidate_id;
        this.full_name=full_name;
        this.election_id=election_id;
        this.created_at=created_at;
        this.updated_at=updated_at;
    }
}
