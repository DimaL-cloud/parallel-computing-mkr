package ua.dmytrolutsiuk;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class Client {

  private static final String SERVER_HOST = "18.158.58.205";
  private static final int SERVER_PORT = 16647;
  private static final String FIRST_NAME = "Dmytro";
  private static final String LAST_NAME = "Lutsiuk";
  private static final byte VARIANT = 14;
  private static final int MAX_QUESTIONS_AMOUNT = 4;
  private static final String ANSWER_TEMPLATE = "test answer";

  public static void main(String[] args) {
    try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
      ProtocolService protocolService = new ProtocolService(socket);

      int connectionId = protocolService.readInt();
      log.info("Connected to server, connection ID: {}", connectionId);
      protocolService.writeInt(connectionId);

      log.info("Server: {}", protocolService.readMessage());

      protocolService.sendMessage(FIRST_NAME);
      log.info("Server: {}", protocolService.readMessage());

      protocolService.sendMessage(LAST_NAME);
      protocolService.writeByte(VARIANT);
      log.info("Server: {}", protocolService.readMessage());

      for (int i = 1; i <= MAX_QUESTIONS_AMOUNT; i++) {
        protocolService.writeByte(i);
        String question = protocolService.readMessage();
        log.info("Question {}: {}", i, question);
        protocolService.sendMessage(ANSWER_TEMPLATE);
        log.info("Server: {}", protocolService.readMessage());
      }

      log.info("Server: {}", protocolService.readMessage());

    } catch (IOException e) {
      log.error("Error connecting to server", e);
    }
  }
}
