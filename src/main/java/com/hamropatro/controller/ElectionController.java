package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.CandidateDto;
import com.hamropatro.dto.ElectionDto;
import com.hamropatro.enums.ElectionStatus;
import com.hamropatro.mapper.CandidateMapper;
import com.hamropatro.mapper.ElectionMapper;
import com.hamropatro.service.CandidateDetail;
import com.hamropatro.service.ElectionDetail;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@GrpcService
public class ElectionController extends ElectionServiceGrpc.ElectionServiceImplBase {
    private final ElectionDetail electionDetail;
    private final CandidateDetail candidateDetail;

    public ElectionController(ElectionDetail electionDetail, CandidateDetail candidateDetail) {
        this.electionDetail = electionDetail;
        this.candidateDetail = candidateDetail;
    }

    long now = Instant.now().toEpochMilli();

    @Override
    public void createElection(CreateElectionRequest request, StreamObserver<Response> responseStreamObserver) {

        if (request.getStartAt() >= request.getEndAt()) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }
        var dto = new ElectionDto(
                null,
                request.getElectionName(),
                request.getVotingType(),
                request.getMaxVotes(),
                request.getStartAt(),
                request.getEndAt(),
                now,
                0l,
                ElectionStatus.SCHEDULED.name()
        );
        electionDetail.save(dto);
        Response response =
                Response.newBuilder()
                        .setMessage("Election  " + request.getElectionName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getElection(ElectionIdRequest request, StreamObserver<getElectionResponse> responseObserver) {
        ElectionDto dto = electionDetail.findById(UUID.fromString(request.getElectionId()));
        electionDetail.findById(dto.election_id());
        getElectionResponse response = getElectionResponse.newBuilder()
                .setElectionId(String.valueOf(dto.election_id()))
                .setElectionName(dto.election_name())
                .setVotingType(dto.voting_type())
                .setMaxVotes(dto.max_votes())
                .setStartAt(dto.start_at())
                .setEndAt(dto.end_at())
                .setCreatedAt(dto.created_at())
                .setStatus(dto.status())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listElection(Empty request, StreamObserver<getAllElectionResponse> responseStreamObserver) {
        List<ElectionDto> dto = electionDetail.findAll();

//        List<Elections> elections = dto.stream()
//                .map(ElectionMapper::fromElectionDtotoElections)
//                .toList();

        List<Elections> elections = dto.stream()
                .map(election -> {

                    Elections.Builder electionBuilder =
                            ElectionMapper
                                    .fromElectionDtotoElections(election)
                                    .toBuilder();

                    List<CandidateDto> candidates =
                            candidateDetail.findByElectionId(election.election_id());

                    List<Candidates> candidateProtos = candidates.stream()
                            .map(CandidateMapper::fromCandidateDtotoCandidates)
                            .toList();

                    electionBuilder.addAllCandidate(candidateProtos);

                    return electionBuilder.build();
                })
                .toList();
        getAllElectionResponse response = getAllElectionResponse.newBuilder().addAllElections(elections).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateElection(UpdateElectionRequest request, StreamObserver<Response> responseStreamObserver) {
        var electionId = UUID.fromString(request.getElectionId());
        var dto = new ElectionDto(
                UUID.fromString(request.getElectionId()),
                request.getElectionName(),
                request.getVotingType(),
                request.getMaxVotes(),
                request.getStartAt(),
                request.getEndAt(),
                0L,
                0L,
                request.getStatus()
        );
        electionDetail.update(electionId, dto);
        Response response =
                Response.newBuilder()
                        .setMessage("Election  " + request.getElectionName() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void deleteElection(ElectionIdRequest request, StreamObserver<Response> responseStreamObserver) {

        var electionId = UUID.fromString(request.getElectionId());
        electionDetail.delete(electionId);
        var response =
                Response.newBuilder()
                        .setMessage("Election deleted successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void electionExists(ElectionIdRequest request, StreamObserver<ElectionExistsResponse> responseStreamObserver) {
        UUID electionId = UUID.fromString(request.getElectionId());

        boolean exists = electionDetail.exists(electionId);

        ElectionExistsResponse response =
                ElectionExistsResponse.newBuilder()
                        .setExists(exists)
                        .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
