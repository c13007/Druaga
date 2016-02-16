package Server;

import GameState.Option;
import Map.Map;
import java.util.Random;

/**
 *
 * @author c13007
 */
public class Game implements Runnable{
    
    Server server;
    Random rand;
    
    int[]  p = {0, 1, 3};
    int dif;
    
    int[][] list = {
        {2,5,6,7,11},
        {3,4,8,17},
        {13,14,15},
        {9,10,12,18},
        {1,16,19}};
    
    public Game(Server server){
        this.server = server;
        rand = new Random();
    }
    
    @Override
    public void run(){
        System.out.println("server:game start");
        dif = Option.getGameMode();
        while(server.isActive()){
            try{
                if(rand.nextInt(10) < p[dif]){
                    int e = server.getBossFloor() != server.getCurrentFloor() ?
                            list[server.getCurrentFloor()][rand.nextInt(list[server.getCurrentFloor()].length)]:
                            rand.nextInt(18) - 1;
                    String msg = "Server," + 0 + "," + e + ","
                            + 0 + ",0,summon,entity," + (rand.nextInt(Map.COL - 2) + 1)
                            + "," + (rand.nextInt(Map.ROW - 2) + 1);
                    server.write(msg);
                    server.lw.write("[Server] send message[" + msg + "]");
                }
                Thread.sleep(1000);
            }catch(Exception e){
            }
        }
        System.out.println("game stop");
    }
    
}
