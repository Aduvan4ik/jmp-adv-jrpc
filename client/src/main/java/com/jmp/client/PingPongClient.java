package com.jmp.client;

import com.google.protobuf.Timestamp;
import com.jmp.task1.proto.PingPongServiceGrpc;
import com.jmp.task1.proto.PingRequest;
import com.jmp.task1.proto.PongResponse;
import io.grpc.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Iterator;

public class PingPongClient {
    private static final Logger logger = LoggerFactory.getLogger(PingPongClient.class);

    private final PingPongServiceGrpc.PingPongServiceBlockingStub pingPongServiceBlockingStub;

    public PingPongClient(Channel channel) {
        this.pingPongServiceBlockingStub = PingPongServiceGrpc.newBlockingStub(channel);
    }

    public void getResponse() {
        PingRequest pingRequest = createRequest();

        logger.info("Sending request to receive one response...");

        PongResponse response = pingPongServiceBlockingStub.getResponse(pingRequest);

        logger.info("Client has been received a response with message: {} and timestamp: {}",
                response.getMessage(), Instant.ofEpochSecond(response.getResponseDate().getSeconds(), response.getResponseDate().getNanos()));
    }

    public void getMultipleResponses() {

        PingRequest pingRequest = createRequest();
        logger.info("Sending request to receive multiple responses...");

        Iterator<PongResponse> responses = pingPongServiceBlockingStub.getMultipleResponses(pingRequest);

        while (responses.hasNext()) {
            PongResponse response = responses.next();
            logger.info("Client has been received a response with message: {} and timestamp: {}",
                    response.getMessage(), Instant.ofEpochSecond(response.getResponseDate().getSeconds(), response.getResponseDate().getNanos()));
        }
    }

    private static PingRequest createRequest() {
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
        return PingRequest.newBuilder()
                .setRequestDate(timestamp)
                .setMessage("ping")
                .build();
    }
}
