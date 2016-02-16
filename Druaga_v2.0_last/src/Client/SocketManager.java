package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Socket Manager
 * A wrapper class that manages a socket
 */
public class SocketManager {
	private Socket socket;
	private Boolean active;
        
        Client client;
	
	public SocketManager(Socket socket, Client client) {
		this.socket = socket;
		active = true;
                
                this.client = client;
	}
        
        public void send(String msg){
            client.update(msg);
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
	
	/**
	 * 1. run socketWriter as a thread.
	 * 2. run socketReader as a thread.
	 */
	public void run() {
		try {
			
			// run socket reader as a thread
			Thread socketReader = new Thread(new SocketReader(this));
			socketReader.start();
			
			// wait the threads end
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