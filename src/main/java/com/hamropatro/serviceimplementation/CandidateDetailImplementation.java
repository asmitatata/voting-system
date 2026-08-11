package com.hamropatro.serviceimplementation;

import com.hamropatro.dto.CandidateDto;
import com.hamropatro.entity.CandidateEntity;
import com.hamropatro.entity.ElectionEntity;
import com.hamropatro.enums.ElectionStatus;
import com.hamropatro.exceptions.CandidateNotFoundException;
import com.hamropatro.exceptions.ElectionNotFoundException;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.repository.CandidateRepository;
import com.hamropatro.service.CandidateDetail;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class CandidateDetailImplementation implements CandidateDetail {
    private final CandidateMapper candidateMapper;
    private final CandidateRepository candidateRepository;

    public CandidateDetailImplementation(CandidateMapper candidateMapper, CandidateRepository candidateRepository) {
        this.candidateMapper = candidateMapper;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void save(CandidateDto candidateDto) {
        CandidateEntity entity = candidateMapper.fromCandidateDtotoCandidateEntity(candidateDto);
        entity.setCandidate_id(UUID.randomUUID());
        candidateRepository.save(entity);
    }

    @Override
    public CandidateDto findById(UUID candidateId) {
        CandidateEntity entity = candidateRepository.findById(candidateId);
        if (entity == null) {
            throw new CandidateNotFoundException("Candidate not found with ID: " + candidateId);
        }
        var candidates = candidateMapper.fromCandidateEntitytoCandidateDto(entity);
        return candidates;
    }

    @Override
    public List<CandidateDto> findAll() {
        List<CandidateEntity> entity = candidateRepository.findAll();
        return (candidateMapper.fromCandidateEntitiesToCandidateDto(entity));

    }

    @Override
    public List<CandidateDto> findByElectionId(UUID electionId) {
        List<CandidateEntity> entity = candidateRepository.findByElectionId(electionId);
        return (candidateMapper.fromCandidateEntitiesToCandidateDto(entity));

    }

    @Override
    public void update(UUID candidateId, CandidateDto candidateDto) {
        if (!candidateRepository.exists(candidateId)) {
            throw new CandidateNotFoundException(
                    "Candidate with id " + candidateId + " does not exist"
            );
        }

        CandidateEntity entity = candidateMapper.fromCandidateDtotoCandidateEntity(candidateDto);

        entity.setCandidate_id(candidateId);
        entity.setUpdated_at(System.currentTimeMillis());
        candidateRepository.update(candidateId, entity);
    }

    @Override
    public void delete(UUID candidateId) {
        CandidateEntity entity = candidateRepository.findById(candidateId);
        if (entity == null) {
            throw new CandidateNotFoundException("Please Enter a valid id: " + candidateId);
        }
        candidateRepository.delete(candidateId);
    }

    @Override
    public boolean exists(UUID candidateId) {
        return candidateRepository.exists(candidateId);
    }
}
