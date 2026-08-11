package com.hamropatro.dto;

import java.time.Instant;
import java.util.UUID;


public record VotesDto(UUID election_id,
                       UUID vote_id,
                       UUID user_id,
                       UUID candidate_id,
                       long created_at,
                       long updated_at) {
}
