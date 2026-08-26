package com.hamropatro.dto.userDto;

import java.util.UUID;

public record VoterDto(UUID userId,
                       String email,
                       String name) {
}
