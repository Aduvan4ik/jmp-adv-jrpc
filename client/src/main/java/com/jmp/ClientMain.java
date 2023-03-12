package com.jmp;

import com.jmp.client.PingPongClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientMain {

    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);
    private static final String SERVER_URL = "localhost:8080";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(SERVER_URL)
                .usePlaintext()
                .build();

        PingPongClient pingPongClient = new PingPongClient(channel);
        pingPongClient.getResponse();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Clean channel shutdown in case JVM was shutdown");
            try {
                channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("Channel shutdown interrupted: {}", e.getMessage());
            }
        }));
    }
}
