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
    private static final String RESPONSE_MESSAGE = "pong";

    @Override
    public void getResponse(PingRequest request, StreamObserver<PongResponse> responseObserver) {
        logger.info("Server has been received a request with message: {} and timestamp: {}",
                request.getMessage(), Instant.ofEpochSecond(request.getRequestDate().getSeconds(), request.getRequestDate().getNanos()));

        logger.info("Sending one response...");
        PongResponse response = createResponse(RESPONSE_MESSAGE);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMultipleResponses(PingRequest request, StreamObserver<PongResponse> responseObserver) {
        logger.info("Server has been received a request with message: {} and timestamp: {}",
                request.getMessage(), Instant.ofEpochSecond(request.getRequestDate().getSeconds(), request.getRequestDate().getNanos()));

        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.error("Thread sleep interrupted exception");
            }
            PongResponse response = createResponse(RESPONSE_MESSAGE + i);
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    private static PongResponse createResponse(String message) {
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        return PongResponse.newBuilder()
                .setResponseDate(timestamp)
                .setMessage(message)
                .build();
    }
}
