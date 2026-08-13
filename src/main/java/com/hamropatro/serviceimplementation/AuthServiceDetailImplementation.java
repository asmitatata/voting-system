package com.hamropatro.serviceimplementation;

import com.hamropatro.dto.*;
import com.hamropatro.entity.UserEntity;
import com.hamropatro.exceptions.InvalidCredentialException;
import com.hamropatro.jwt.GoogleVerifier;
import com.hamropatro.jwt.JwtUtil;
import com.hamropatro.mapper.AuthUserMapper;
import com.hamropatro.repository.UserRepository;
import com.hamropatro.service.AuthServiceDetail;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

@Singleton
public class AuthServiceDetailImplementation implements AuthServiceDetail {
    private final AuthUserMapper authUserMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final GoogleVerifier googleVerifier;

    public AuthServiceDetailImplementation(AuthUserMapper authUserMapper, UserRepository userRepository, JwtUtil jwtUtil, GoogleVerifier googleVerifier) {
        this.authUserMapper = authUserMapper;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.googleVerifier = googleVerifier;
    }

    @Override
    public void register(RegisterDto registerDto) {
        var entity =
                authUserMapper.fromRegisterDtoToUserEntity(registerDto);
        entity.setUserId(UUID.randomUUID());
        entity.setPassword(
                BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt())
        );

        userRepository.register(entity);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        var user = userRepository.findByEmail(loginDto.email());
//        System.out.println(user);
        if (user == null) {
            throw new InvalidCredentialException("Invalid email or password");
        }

        if (!BCrypt.checkpw(loginDto.password(), user.password())) {
            throw new InvalidCredentialException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public LoginResponseDto refresh(UserDto userDto) {
        var user = userRepository.findById(userDto.userId());

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public LoginResponseDto googleLogin(GoogleLoginDto googleLoginDto) {
        try {
            var googleToken = googleVerifier.verify(googleLoginDto.idToken());
            if (googleToken == null) {
                throw new InvalidCredentialException("Invalid Google ID token");
            }
            var payload = googleToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            var user = userRepository.findByEmail(email);

            if (user == null) {

                UserEntity entity = new UserEntity(
                        UUID.randomUUID(),
                        email,
                        name,
                        "",
                        "Voter",
                        true,
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                );

                userRepository.register(entity);

                user = userRepository.findByEmail(email);
            }

            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            return new LoginResponseDto(accessToken, refreshToken);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Google token verification failed", e);
        }
    }
}
