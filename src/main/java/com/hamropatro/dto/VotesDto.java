package com.hamropatro.dto;

import java.time.Instant;
import java.util.UUID;


public record VotesDto(UUID user_id,
                       UUID election_id,
                       UUID vote_id,
                       UUID candidate_id,
                       int vote_count,
                       long created_at,
                       long updated_at) {
}
