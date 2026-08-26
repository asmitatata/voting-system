package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.votesDto.VotesDto;
import com.hamropatro.interceptor.AuthContext;
import com.hamropatro.kafka.publisher.VotePublisher;
import com.hamropatro.service.VotesDetail;
import com.hamropatro.validation.Validate;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

@GrpcService
@Slf4j
public class VotingController extends VotingServiceGrpc.VotingServiceImplBase {
    private final VotesDetail votesDetail;
    private final VotePublisher kafkaVoteProducer;
    private final Validate<VotesDto> votesValidator;

    public VotingController(VotesDetail votesDetail, VotePublisher kafkaVoteProducer,
                            @Named("VotesValidator") Validate<VotesDto> votesValidator) {
        this.votesDetail = votesDetail;
        this.kafkaVoteProducer = kafkaVoteProducer;
        this.votesValidator = votesValidator;
    }

    @Override
    public void castVote(CastVoteRequest request, StreamObserver<Response> responseStreamObserver) {
        AuthContext.requireAdminOrVoter();
        var dto = VotesDto.builder()
                .userId(UUID.fromString(request.getUserId()))
                .electionId(UUID.fromString(request.getElectionId()))
                .voteDate(LocalDate.now())
                .candidateId(UUID.fromString(request.getCandidateId()))
                .voteCount(request.getVoteCount())
                .build();
        votesValidator.validate(dto);
        kafkaVoteProducer.publishVote(dto);

        Response response =
                Response.newBuilder()
                        .setMessage("Vote saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void votesCountByElectionId(VotesByElectionIdRequest request, StreamObserver<VoteCountResponse> responseStreamObserver) {
        AuthContext.requireAdmin();
        var electionId = UUID.fromString(request.getElectionId());
        long count = votesDetail.countByElectionId(electionId);
        VoteCountResponse response = VoteCountResponse.newBuilder()
                .setCount(count)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void votesCountByCandidateId(VotesByCandidateIdRequest request, StreamObserver<VoteCountResponse> responseStreamObserver) {
        AuthContext.requireAdmin();
        var candidateId = UUID.fromString(request.getCandidateId());
        var electionId = UUID.fromString(request.getElectionId());
        long count = votesDetail.countByCandidateId(electionId, candidateId);
        VoteCountResponse response = VoteCountResponse.newBuilder()
                .setCount(count)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}
