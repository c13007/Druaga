package Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketManager {

    private Socket socket;
    private Boolean active;

    Server server;

    public SocketManager(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        active = true;
    }

    public void send(String msg) {
        server.update(msg);
    }

    public boolean isActive() {
        return active;
    }

    public void stop() {
        active = false;
    }

    public void close() {
        try {
            socket.close();
            System.out.println("--connection closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Thread socketReader = new Thread(new SocketReader(this));
            socketReader.start();

            socketReader.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getOutputStream() throws Exception {
        return new DataOutputStream(socket.getOutputStream());
    }

    public BufferedReader getInputStream() throws Exception {
        return new BufferedReader(new InputStreamReader(
                new DataInputStream(socket.getInputStream())));
    }
}
