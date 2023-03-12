package com.jmp.service;

import com.google.protobuf.Timestamp;
import com.jmp.task1.proto.PingPongServiceGrpc;
import com.jmp.task1.proto.PingRequest;
import com.jmp.task1.proto.PongResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class PingPongServiceImpl extends PingPongServiceGrpc.PingPongServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(PingPongServiceImpl.class);

    @Override
    public void getResponse(PingRequest request, StreamObserver<PongResponse> responseObserver) {
        logger.info("Server has been received a request with message: {} and timestamp: {}",
                request.getMessage(), Instant.ofEpochSecond(request.getRequestDate().getSeconds(), request.getRequestDate().getNanos()));
        Instant now = Instant.now();


        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        PongResponse response = PongResponse.newBuilder()
                .setResponseDate(timestamp)
                .setMessage("Pong")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
