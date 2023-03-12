package com.jmp;

import com.jmp.service.PingPongServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
    private static final int PORT_NUMBER = 8080;

    private Server server;

    public static void main(String[] args) throws InterruptedException {
        ServerMain serverMain = new ServerMain();
        serverMain.startServer();
        serverMain.awaitUntilShutdown();
    }

    public void startServer() {
        try {
            server = ServerBuilder.forPort(PORT_NUMBER)
                    .addService(new PingPongServiceImpl())
                    .build()
                    .start();
            logger.info("Ping pong Server started on port: {}", PORT_NUMBER);

            shutdownServerInCaseJVMShutdown();
        } catch (IOException exception) {
            logger.error("Server did not start: {}", exception.getMessage());
        }
    }

    private void shutdownServerInCaseJVMShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Clean server shutdown in case JVM was shutdown");
            stopServer();
        }));
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.shutdown().awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("Server shutdown interrupted: {}", e.getMessage());
            }
        }
    }

    public void awaitUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
