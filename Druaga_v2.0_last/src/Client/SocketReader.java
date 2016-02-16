package Client;

import java.io.BufferedReader;


class SocketReader implements Runnable {
	private SocketManager socketManager;
	
	public SocketReader(SocketManager socketManager) {
		this.socketManager = socketManager;
	}
	
	public void run() {
		try {
			BufferedReader br =
				socketManager.getInputStream();
		
			while (socketManager.isActive()) {
				String line = br.readLine();
				socketManager.send(line);
	    		if ("bye".equals(line))
					socketManager.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}