package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.GoogleLoginDto;
import com.hamropatro.dto.LoginDto;
import com.hamropatro.dto.LoginResponseDto;
import com.hamropatro.dto.RegisterDto;
import com.hamropatro.jwt.JwtUtil;
import com.hamropatro.jwt.JwtValidator;
import com.hamropatro.repository.UserRepository;
import com.hamropatro.service.AuthServiceDetail;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.micronaut.grpc.annotation.GrpcService;

import java.util.UUID;

@GrpcService
public class AuthController extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthServiceDetail authServiceDetail;
    private final JwtUtil jwtUtil;
    private final JwtValidator jwtValidator;
    private final UserRepository userRepository;

    public AuthController(AuthServiceDetail authServiceDetail, JwtUtil jwtUtil, JwtValidator jwtValidator, UserRepository userRepository) {
        this.authServiceDetail = authServiceDetail;
        this.jwtUtil = jwtUtil;
        this.jwtValidator = jwtValidator;
        this.userRepository = userRepository;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        long now = System.currentTimeMillis();
        RegisterDto dto = new RegisterDto(
                null,
                request.getEmail(),
                request.getName(),
                request.getPassword(),
                "Voter",
                true,
                now,
                now);
        authServiceDetail.register(dto);
        RegisterResponse response = RegisterResponse.newBuilder()
                .setMessage("User Registered sucessfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        var dto = new LoginDto(
                request.getEmail(),
                request.getPassword()
        );
        LoginResponseDto tokenResponse = authServiceDetail.login(dto);
        var response = LoginResponse.newBuilder().setAccessToken(tokenResponse.accessToken()).setRefreshToken(tokenResponse.refreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loginRefresh(RefreshRequest request, StreamObserver<LoginResponse> responseObserver) {
        String refreshToken = request.getRefreshToken();

        Claims claims = jwtValidator.validateRefreshToken(refreshToken);

        String userId = claims.getSubject();
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        var user = userRepository.findById(UUID.fromString(userId));

        var accessToken = jwtUtil.generateAccessToken(user);
        LoginResponseDto tokenResponse = authServiceDetail.refresh(user);
        var response = LoginResponse.newBuilder().setAccessToken(tokenResponse.accessToken()).setRefreshToken(tokenResponse.refreshToken())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void googleLogin(GoogleLoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        var dto = new GoogleLoginDto(request.getIdToken());

        LoginResponseDto loginResponse = authServiceDetail.googleLogin(dto);

        LoginResponse response = LoginResponse.newBuilder()
                .setAccessToken(loginResponse.accessToken())
                .setRefreshToken(loginResponse.refreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

