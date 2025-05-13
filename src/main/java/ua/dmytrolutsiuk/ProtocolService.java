package ua.dmytrolutsiuk;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ProtocolService {

  private final DataInputStream in;
  private final DataOutputStream out;

  public ProtocolService(Socket socket) throws IOException {
    this.in = new DataInputStream(socket.getInputStream());
    this.out = new DataOutputStream(socket.getOutputStream());
  }

  public int readInt() {
    try {
      return in.readInt();
    } catch (IOException e) {
      log.error("Error reading int from server", e);
      throw new RuntimeException(e);
    }
  }

  public void writeInt(int value) {
    try {
      out.writeInt(value);
      out.flush();
    } catch (IOException e) {
      log.error("Error writing int to server", e);
      throw new RuntimeException(e);
    }
  }

  public void writeByte(int value) {
    try {
      out.writeByte(value);
      out.flush();
    } catch (IOException e) {
      log.error("Error writing byte to server", e);
      throw new RuntimeException(e);
    }
  }

  public String readMessage() {
    try {
      int length = in.readInt();
      byte[] data = in.readNBytes(length);
      return new String(data);
    } catch (IOException e) {
      log.error("Error reading message from server", e);
      throw new RuntimeException(e);
    }
  }

  public void sendMessage(String message) {
    try {
      byte[] data = message.getBytes();
      out.writeInt(data.length);
      out.write(data);
      out.flush();
    } catch (IOException e) {
      log.error("Error sending message to server", e);
      throw new RuntimeException(e);
    }
  }
}
