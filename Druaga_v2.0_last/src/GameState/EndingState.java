package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

import Main.MainPanel;

public class EndingState extends GameState{
    

    private Image ret;
    Rectangle rect;
    
    private Font font;
    
    
    public EndingState(GameStateManager gsm){
    	
        this.gsm = gsm;
        
        try{
        	
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/system/return.png"));
            ret = icon.getImage();
            
            font = new Font(
                    "Century",
                    Font.PLAIN,
                    15);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        rect = new Rectangle();
        rect.setRect(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);
        
    }
    
    public void init(){
    	MainPanel.wave.stop("Main");
    	MainPanel.wave.stop("Main2");
    	MainPanel.wave.stop("Boss");
    	MainPanel.wave.stop("Ending");
    	MainPanel.wave.loopPlay("Ending",50);
    }
    public void update(){}
    
    public void draw(Graphics2D g){
    	
    	g.setColor(Color.BLACK);
        g.fill(rect);
        
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString("CONGRATURATIONS!!", 115, 70);
        
        g.drawString(" Push Enter MainMenu ", 110, 260);
        g.drawImage(ret, 278, 246, null);
        
    
    }
    
    
    public void keyPressed(int k){
    	if(k == KeyEvent.VK_ENTER){
            MainPanel.wave.stop("KReturn");
        	MainPanel.wave.play("KReturn");
        	MainPanel.wave.stop("Ending");
        	gsm.setState(GameStateManager.MENU_STATE);
        	MainGameState.startPanel = 0;
        }
    }
    
    public void keyReleased(int k){}
}
