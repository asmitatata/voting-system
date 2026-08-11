package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.CandidateDto;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.service.CandidateDetail;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@GrpcService
public class CandidateController extends CandidateServiceGrpc.CandidateServiceImplBase {
    private final CandidateDetail candidateDetail;

    public CandidateController(CandidateDetail candidateDetail) {
        this.candidateDetail = candidateDetail;
    }
    long now = Instant.now().toEpochMilli();
    @Override
    public void createCandidate(CreateCandidateRequest request, StreamObserver<Response> responseStreamObserver) {
        var dto = new CandidateDto(
                null,
                UUID.fromString(request.getElectionId()),
                request.getCandidateName(),
                now,
                0l
        );
        candidateDetail.save(dto);
        Response response =
                Response.newBuilder()
                        .setMessage("Candidate  " + request.getCandidateName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getCandidate(CandidateIdRequest request, StreamObserver<getCandidateResponse> responseObserver) {
        CandidateDto dto = candidateDetail.findById(UUID.fromString(request.getCandidateId()));
        candidateDetail.findById(dto.candidate_id());
        getCandidateResponse response = getCandidateResponse.newBuilder()
                .setCandidateId(String.valueOf(dto.candidate_id()))
                .setElectionId(String.valueOf(dto.election_id()))
                .setCandidateName(dto.full_name())
                .setCreatedAt(dto.created_at())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listCandidate(Empty request, StreamObserver<getAllCandidateResponse> responseStreamObserver) {
        List<CandidateDto> dto = candidateDetail.findAll();

        List<Candidates> candidates = dto.stream()
                .map(CandidateMapper::fromCandidateDtotoCandidates)
                .toList();

        getAllCandidateResponse response = getAllCandidateResponse.newBuilder().addAllCandidate(candidates).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void listCandidateByElectionId(CandidateByElectionIdRequest request, StreamObserver<getAllCandidateResponse> responseStreamObserver) {
        List<CandidateDto> dto = candidateDetail.findByElectionId(UUID.fromString(request.getElectionId()));

        List<Candidates> candidates = dto.stream()
                .map(CandidateMapper::fromCandidateDtotoCandidates)
                .toList();

        getAllCandidateResponse response = getAllCandidateResponse.newBuilder().addAllCandidate(candidates).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateCandidate (UpdateCandidateRequest request, StreamObserver<Response> responseStreamObserver) {
        var candidateId = UUID.fromString(request.getCandidateId());
        var dto = new CandidateDto(
                UUID.fromString(request.getCandidateId()),
                UUID.fromString(request.getElectionId()),
                request.getCandidateName(),
                0l,
                0l
        );
        candidateDetail.update(candidateId,dto);
        Response response =
                Response.newBuilder()
                        .setMessage("Candidate  " + request.getCandidateName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }


    @Override
    public void deleteCandidate(CandidateIdRequest request, StreamObserver<Response> responseStreamObserver) {

        var candidateId = UUID.fromString(request.getCandidateId());
        candidateDetail.delete(candidateId);
        var response =
                Response.newBuilder()
                        .setMessage("Candidate deleted successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void candidateExists(CandidateIdRequest request, StreamObserver<CandidateExistResponse> responseStreamObserver) {
        UUID candidateId = UUID.fromString(request.getCandidateId());

        boolean exists = candidateDetail.exists(candidateId);

        CandidateExistResponse response =
                CandidateExistResponse.newBuilder()
                        .setExists(exists)
                        .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
