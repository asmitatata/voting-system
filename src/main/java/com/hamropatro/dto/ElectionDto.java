package com.hamropatro.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ElectionDto(UUID election_id,
                          String election_name,
                          String voting_type,
                          int max_votes,
                          long start_at,
                          long end_at,
                          long created_at,
                          long updated_at,
                          String status) {
}
