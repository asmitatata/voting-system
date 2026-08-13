package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.VoterDto;
import com.hamropatro.mapper.AuthUserMapper;
import com.hamropatro.service.VoterDetail;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
public class VoterController extends VoterServiceGrpc.VoterServiceImplBase {
    private final VoterDetail voterDetail;

    public VoterController(VoterDetail voterDetail) {
        this.voterDetail = voterDetail;
    }

    @Override
    public void getVoter(VoterIdRequest request, StreamObserver<getVoterResponse> responseObserver) {
        VoterDto dto = voterDetail.findById(UUID.fromString(request.getVoterId()));
        voterDetail.findById(dto.userId());
        getVoterResponse response = getVoterResponse.newBuilder()
                .setVoterId(String.valueOf(dto.userId()))
                .setVoterEmail(String.valueOf(dto.email()))
                .setVoterName(dto.name())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listVoter(Empty request, StreamObserver<getAllVoter> responseStreamObserver) {
        List<VoterDto> dto = voterDetail.findAll();

        List<Voters> voters = dto.stream()
                .map(AuthUserMapper::fromVoterDtoToVoters)
                .toList();

        getAllVoter response = getAllVoter.newBuilder().addAllVoter(voters).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
