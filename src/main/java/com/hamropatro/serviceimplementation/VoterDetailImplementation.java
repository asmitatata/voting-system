package com.hamropatro.serviceimplementation;

import com.hamropatro.dto.VoterDto;
import com.hamropatro.entity.UserEntity;
import com.hamropatro.exceptions.VoterNotFoundException;
import com.hamropatro.mapper.AuthUserMapper;
import com.hamropatro.repository.VoterRepository;
import com.hamropatro.service.VoterDetail;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class VoterDetailImplementation implements VoterDetail {
    private final AuthUserMapper userMapper;
    private final VoterRepository voterRepository;

    public VoterDetailImplementation(AuthUserMapper userMapper, VoterRepository voterRepository) {
        this.userMapper = userMapper;
        this.voterRepository = voterRepository;
    }

    @Override
    public VoterDto findById(UUID userId) {
        UserEntity entity = voterRepository.findById(userId);
        if (entity == null) {
            throw new VoterNotFoundException("Voter not found with ID: " + userId);
        }
        var voters = userMapper.fromUserEntityToVoterDto(entity);
        return voters;
    }

    @Override
    public List<VoterDto> findAll() {
        List<UserEntity> entity = voterRepository.findAll();
        return (userMapper.fromUserEntitiesToVoterDto(entity));

    }

    @Override
    public boolean exists(UUID userId) {
        return voterRepository.exists(userId);
    }
}
