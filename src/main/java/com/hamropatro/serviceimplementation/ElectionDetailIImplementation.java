package com.hamropatro.serviceimplementation;

import com.hamropatro.dto.ElectionDto;
import com.hamropatro.entity.ElectionEntity;
import com.hamropatro.enums.ElectionStatus;
import com.hamropatro.exceptions.ElectionNotFoundException;
import com.hamropatro.mapper.ElectionMapper;
import com.hamropatro.repository.ElectionRepository;
import com.hamropatro.service.ElectionDetail;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Singleton
public class ElectionDetailIImplementation implements ElectionDetail {

    private final ElectionMapper electionMapper;
    private final ElectionRepository electionRepository;
    private ElectionStatus status;

    public ElectionDetailIImplementation(ElectionMapper electionMapper, ElectionRepository electionRepository) {
        this.electionMapper = electionMapper;
        this.electionRepository = electionRepository;
    }

    @Override
    public void save(ElectionDto electionDto) {
        ElectionEntity entity = electionMapper.fromElectionDtotoElectionEntity(electionDto);
        entity.setElection_id(UUID.randomUUID());
        entity.setStatus(ElectionStatus.SCHEDULED.name());

        electionRepository.save(entity);

    }

    @Override
    public ElectionDto findById(UUID electionId) {
        ElectionEntity entity = electionRepository.findById(electionId);
        if (entity == null) {
            throw new ElectionNotFoundException("Election not found with ID: " + electionId);
        }
        var elections = electionMapper.fromElectionEntitytoElectionDto(entity);
        return elections;
    }

    @Override
    public List<ElectionDto> findAll() {
        List<ElectionEntity> entity = electionRepository.findAll();
        return (electionMapper.fromElectionEntitiesToElectionDto(entity));
    }

    @Override
    public void update(UUID electionId, ElectionDto electionDto) {
        if (!electionRepository.exists(electionId)) {
            throw new ElectionNotFoundException(
                    "Election with id " + electionId + " does not exist"
            );
        }

        ElectionEntity entity = electionMapper.fromElectionDtotoElectionEntity(electionDto);

        entity.setElection_id(electionId);
        entity.setUpdated_at(System.currentTimeMillis());
        electionRepository.update(electionId, entity);
    }

    @Override
    public void delete(UUID electionId) {
        ElectionEntity entity = electionRepository.findById(electionId);
        if (entity == null) {
            throw new ElectionNotFoundException("Please Enter a valid id: " + electionId);
        }
        electionRepository.delete(electionId);
    }

    @Override
    public boolean exists(UUID electionId) {

        return electionRepository.exists(electionId);
    }
//
//    @Override
//    public void updateStatus(UUID electionId, String status) {
//
//    }

//    @Override
//    public void updateStatus(UUID electionId, String status) {
//
//       electionRepository.updateStatus(electionId,status);
//    }

}
