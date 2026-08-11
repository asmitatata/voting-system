package com.hamropatro.dto;

import java.util.UUID;

public record CandidateDto(UUID candidate_id,
                           UUID election_id,
                           String full_name,
                           long created_at,
                           long updated_at) {
}
