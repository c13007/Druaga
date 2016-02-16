package Map;

import Main.MainPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import GameState.MainGameState;

public class MapManager {
    private List<Map> map;
    private List<Position> goal;
    
    private int currentFloor;
    private int maxFloor;
    private boolean useMapFile;
    
    public static int musicflg;
    
    Font font;
    
    Random rand;
    
    
    public MapManager(int maxFloor, boolean useMapFile){
        this.maxFloor = maxFloor;
        this.useMapFile = useMapFile;
        
        map = new ArrayList<>();
        goal = new ArrayList<>();
        rand = new Random();
        
        font = new Font( "Century", Font.PLAIN, 10);
        
        init();
    }
    
    public Map getMap(){ return map.get(currentFloor); }
    public int getCurrentFloor(){ return currentFloor; }
    public int getMaxFloor(){ return maxFloor; }
    public Position findGoal(){ return goal.get(currentFloor); }
    
    public void init(){
        currentFloor = 0;
        
        for(int i = 0; i < maxFloor; i++)
            if(i == maxFloor - 1)
                map.add(new BossMap(useMapFile));
            else
                map.add(new Map(useMapFile));
        
        //1階の階段の位置は固定
        goal.add(new Position(20, 20));
        for(int i = 1; i < maxFloor - 1; i++){
            int x = 1 + rand.nextInt(Map.COL - 2);
            int y = 1 + rand.nextInt(Map.ROW - 2);
            goal.add(new Position(x, y));
        }
        
    }
    
    public void draw(Graphics2D g){
        map.get(currentFloor).draw(g);
        
        g.setFont(font);
        g.setColor(Color.GREEN);
        g.drawString(
                "Floor " + (currentFloor + 1) + " / " + maxFloor,
                Map.COL * MainPanel.CELL_SIZE - 60,
                Map.ROW *MainPanel.CELL_SIZE + 12);
    }
    
    
    
    public void climb(){
    	
    	MainPanel.wave.stop("Main");
    	MainPanel.wave.stop("Main2");
    	MainPanel.wave.stop("Boss");
    	    	
    	MainPanel.wave.play("FloorClear1");
    	MainGameState.startPanel = 0;
    	
    	try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    	
        currentFloor++;
        if(this.currentFloor == this.maxFloor-1) musicflg = 2;
        MainPanel.wave.stop("FloorClear1");
        
    }
    
    
}
