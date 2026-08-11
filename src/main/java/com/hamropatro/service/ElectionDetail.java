package com.hamropatro.service;

import com.hamropatro.dto.ElectionDto;

import java.util.List;
import java.util.UUID;

public interface ElectionDetail {
    void save(ElectionDto electionDto);

    ElectionDto findById(UUID electionId);

    List<ElectionDto> findAll();

    void update(UUID electionId, ElectionDto electionDto);

    void delete(UUID electionId);

    boolean exists(UUID electionId);
}

