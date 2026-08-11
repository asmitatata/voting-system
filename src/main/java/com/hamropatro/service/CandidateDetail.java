package com.hamropatro.service;

import com.hamropatro.dto.CandidateDto;

import java.util.List;
import java.util.UUID;

public interface CandidateDetail {
    void save(CandidateDto candidateDto);

    CandidateDto findById(UUID candidateId);

    List<CandidateDto> findAll();

    List<CandidateDto> findByElectionId(UUID electionId);

    void update(UUID candidateId, CandidateDto candidateDto);

    void delete(UUID candidateId);

    boolean exists(UUID candidateId);
}
