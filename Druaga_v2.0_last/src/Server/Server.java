package Server;

import java.io.DataOutputStream;
import java.net.*;
import java.util.Date;

public class Server implements Runnable{
    
    private int port;
    private DataManager dm;
    
    private Date day = new Date();
    private long client_start;
    private long client_end;
    private long server_start;
    private long server_end;
    private long now;
    
    private boolean active;
    private Thread game;
    
    private int currentFloor;
    private int BossFloor;
    
    private SocketManager socketManager;
    public LogWriter lw;
    
    public Server(int port){
        this.port = port;
        dm = new DataManager();
        active = true;
    }
    
    public int getCurrentFloor(){ return currentFloor; }
    public int getBossFloor(){ return BossFloor; }
    
    public boolean isActive(){ return active; }

    @Override
    public void run() {
        System.out.println("[Server] start!"); 
        try {

            Socket socket = null;
            ServerSocket svsock = new ServerSocket(port);
            
            socket = svsock.accept();
            socketManager = new SocketManager(socket, this);
            game = new Thread(new Game(this));
            socketManager.run();
            socketManager.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void update(String msg){
        if(msg == null) return ;
        String[] data = msg.split(",");
        
        if(data.length < 5) return;
        
        switch(data[DataManager.TAG]){
            case "playStart":
                currentFloor = 0;
                BossFloor = Integer.parseInt(data[6]) - 1;
                client_start = Long.parseLong(data[DataManager.TIME]);
                lw = new LogWriter();
                lw.write("[Client] Play Start !! ");
                active = true;
                game = new Thread(new Game(this));
                game.start();
                break;
            case "playEnd":
                client_end = Long.parseLong(data[DataManager.TIME]);
                lw.write("[Client] Game Clear ! Time: " + (client_end - client_start)/1000L + " sec TimeLeft: " + data[6] + " sec Score:" + data[7]);
                System.out.println("clear");
                lw.close();
                active = false;
                break;
            case "gameOver":
                lw.write("[Client] Game Over...");
                lw.close();
                active = false;
            case "moveStart":
                //使わないかも
                break;
            case "moveEnd":
                int[] dx = {0,-1, 0, 1};
                int[] dy = {1, 0,-1, 0};
                int x = Integer.parseInt(data[6]) + dx[Integer.parseInt(data[8])];
                int y = Integer.parseInt(data[7]) + dy[Integer.parseInt(data[8])];
                lw.write("[Client] Entity(" + dm.findEntityNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID]))
                        + ") move {position (" + x + "," +  y  + ") -> (" + data[6] + ", " +  data[7] + ")}");
                break;
            case "attack":
                lw.write("[Client] Entity(" + dm.findEntityNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID]))+") attack. {"
                        + "attackPoint:" + data[8] + ", position(" + data[6] + ", " +  data[7] + ")");
                break;
            case "item":
                lw.write("[Client] Get Item {name: " + dm.findItemNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID])) 
                        + ", position(" + data[6] + ", " +  data[7] + ")");
                break;
            case "damage":
                lw.write("[Client] Entity(" + dm.findEntityNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID]))+") damage. {"
                        + "damagePoint:" + data[8] + ", position(" + data[6] + ", " +  data[7] + ")");
                break;
            case "dead":
                lw.write("[Client] Entity(" + dm.findEntityNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID]))+") dead.");
                break;
            case "summon":
                if(data[6].equals("entity")){
                    lw.write("[Client] Summon entity {name:" + dm.findEntityNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID])) + 
                            ", position(" + data[7] + ", " + data[8] + ")}");
                }else{
                    lw.write("[Client] summon item {name:" + dm.findItemNameByStaticId(Integer.parseInt(data[DataManager.STATIC_ID])) 
                        + " position (" + data[7]  + ", " + data[8] + ")}");
                }
                break;
            case "climb":
                int f = Integer.parseInt(data[DataManager.FLOOR]);
                currentFloor = f;
                lw.write("[Client] climb the floor " + f + " -> " + (f + 1));
                break;
            default:
                lw.write("[Client] unknown message. {tag: " + data[5] + "}");
                break;
        }
        
    }
    
    public void write(String msg){
        try{
            DataOutputStream out;
            out = socketManager.getOutputStream();
            
            if(msg.equals("stop")) socketManager.stop();
            else{
                out.writeBytes(msg + "\n");
                out.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
