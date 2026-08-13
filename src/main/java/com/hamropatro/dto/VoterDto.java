package com.hamropatro.dto;

import java.util.UUID;

public record VoterDto (UUID userId,
                        String name,
                        String email){
}
