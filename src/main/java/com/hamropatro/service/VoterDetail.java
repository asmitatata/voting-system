package com.hamropatro.service;

import com.hamropatro.dto.UserDto;
import com.hamropatro.dto.VoterDto;

import java.util.List;
import java.util.UUID;

public interface VoterDetail {
    VoterDto findById(UUID userId);

    List<VoterDto> findAll();

    boolean exists(UUID userId);
}
