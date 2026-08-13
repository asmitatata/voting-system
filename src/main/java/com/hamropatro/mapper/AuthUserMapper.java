package com.hamropatro.mapper;

import com.hamropatro.Candidates;
import com.hamropatro.Voters;
import com.hamropatro.dto.LoginDto;
import com.hamropatro.dto.RegisterDto;
import com.hamropatro.dto.UserDto;
import com.hamropatro.dto.VoterDto;
import com.hamropatro.entity.UserEntity;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AuthUserMapper {

    public UserEntity fromRegisterDtoToUserEntity(RegisterDto registerDto) {
        return new UserEntity(
                registerDto.userId(),
                registerDto.email(),
                registerDto.name(),
                registerDto.password(),
                registerDto.role(),
                registerDto.isActive(),
                registerDto.createdAt(),
                registerDto.updatedAt()
        );
    }

    public RegisterDto fromUserEntityToRegisterDto(UserEntity userEntity) {
        return new RegisterDto(
                userEntity.getUserId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.isActive(),
                userEntity.getCreated_at(),
                userEntity.getUpdated_at()
        );
    }

    public LoginDto fromUserEntityToLoginDto(UserEntity userEntity) {
        return new LoginDto(
                userEntity.getEmail(),
                userEntity.getPassword()
        );
    }

    public UserEntity fromLoginDtoToUserEntity(LoginDto loginDto) {
        return new UserEntity(
                loginDto.email(),
                loginDto.password()
        );
    }

    public UserEntity fromUserDtoToUserEntity(UserDto userDto) {
        return new UserEntity(
                userDto.userId(),
                userDto.email(),
                userDto.name(),
                userDto.role(),
                userDto.password(),
                userDto.isActive(),
                userDto.created_at(),
                userDto.updated_at()
        );
    }

    public UserDto fromUserEntityToUserDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getUserId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getRole(),
                userEntity.getPassword(),
                userEntity.isActive(),
                userEntity.getCreated_at(),
                userEntity.getUpdated_at()
        );
    }

    public VoterDto fromUserEntityToVoterDto(UserEntity userEntity) {
        return new VoterDto(
                userEntity.getUserId(),
                userEntity.getEmail(),
                userEntity.getName()
        );
    }

    public static Voters fromVoterDtoToVoters(VoterDto voterDto) {
        return Voters.newBuilder()
                .setVoterId(String.valueOf(voterDto.userId()))
                .setVoterEmail(voterDto.email())
                .setVoterName(voterDto.name())
                .build();
    }

    public List<VoterDto> fromUserEntitiesToVoterDto(
            List<UserEntity> userEntities) {

        return userEntities.stream()
                .map(this::fromUserEntityToVoterDto)
                .toList();
    }

}
