package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.VotesDto;
import com.hamropatro.mapper.VotesMapper;
import com.hamropatro.service.VotesDetail;
import io.grpc.stub.StreamObserver;
import io.micronaut.grpc.annotation.GrpcService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@GrpcService
public class VotingController extends VotingSystemServiceGrpc.VotingSystemServiceImplBase {
    private final VotesDetail votesDetail;

    public VotingController(VotesDetail votesDetail) {
        this.votesDetail = votesDetail;
    }
    long now = Instant.now().toEpochMilli();

    @Override
    public void castVote(CastVoteRequest request, StreamObserver<Response> responseStreamObserver) {
        var dto = new VotesDto(
                UUID.fromString(request.getUserId()),
                UUID.fromString(request.getElectionId()),
                null,
                UUID.fromString(request.getCandidateId()),
                request.getVoteCount(),
                now,
                0l
        );
        votesDetail.castVote(dto);
        Response response =
                Response.newBuilder()
                        .setMessage("Vote for " + request.getCandidateId() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    public void updateVote(UpdateVoteRequest request, StreamObserver<Response> responseStreamObserver) {
        var voteId=UUID.fromString(request.getVoteId());
        var dto = new VotesDto(
                UUID.fromString(request.getUserId()),
                UUID.fromString(request.getElectionId()),
                UUID.fromString(request.getVoteId()),
                UUID.fromString(request.getCandidateId()),
                request.getVoteCount(),
                0l,
                0l
        );
        votesDetail.updateVote(voteId,dto);
        Response response =
                Response.newBuilder()
                        .setMessage("Vote for " + request.getCandidateId() + " saved successfully")
                        .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void hasVoted(VoteExistsRequest request, StreamObserver<VoteExistsResponse> responseStreamObserver) {
        UUID electionId = UUID.fromString(request.getElectionId());
        UUID userId = UUID.fromString(request.getUserId());

        boolean hasVoted = votesDetail.hasVoted(electionId,userId);

        VoteExistsResponse response =
                VoteExistsResponse.newBuilder()
                        .setExists(hasVoted)
                        .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getVotesByElectionId(VotesByElectionIdRequest request, StreamObserver<getVotesByElectionIdResponse> responseStreamObserver) {
        var electionId=UUID.fromString(request.getElectionId());
//        var userId=UUID.fromString(request.getUserId());
        List<VotesDto> dto = votesDetail.findVotesByElection(electionId);

        List<VotesByElectionId> votes = dto.stream()
                .map(VotesMapper::fromVotesDtotoVotesByElectionId)
                .toList();

        getVotesByElectionIdResponse response = getVotesByElectionIdResponse.newBuilder().addAllVotes(votes).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void votesCountByElectionId(VotesByElectionIdRequest request , StreamObserver<VoteCountResponse> responseStreamObserver){
        var electionId = UUID.fromString(request.getElectionId());
        long count = votesDetail.countByElectionId(electionId);
        VoteCountResponse response = VoteCountResponse.newBuilder()
                .setCount(count)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void votesCountByCandidateId(VotesByCandidateIdRequest request , StreamObserver<VoteCountResponse> responseStreamObserver){
        var candidateId = UUID.fromString(request.getCandidateId());
        long count = votesDetail.countByCandidateId(candidateId);
        VoteCountResponse response = VoteCountResponse.newBuilder()
                .setCount(count)
                .build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

}
