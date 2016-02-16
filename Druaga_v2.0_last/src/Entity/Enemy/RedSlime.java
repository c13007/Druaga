package Entity.Enemy;

import Entity.Magic.WhiteMagic;
import Entity.*;
import Main.MainPanel;

import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author takahito
 */
public class RedSlime extends Entity{
    
    Random rand;
    
    public RedSlime(EntityManager em, int x, int y){
        super(em,15,  x, y);
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/redslime.png"));
            image = icon.getImage();
            
            lifePoint = 160;
            maxLifePoint = 160;
            attackPoint = 50;
            defensePoint = 40;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.frame = 0;
        this.frameMax = 0;
        
        px = x * MainPanel.CELL_SIZE;
        py = y * MainPanel.CELL_SIZE;
        
        rand = new Random();
    }

    @Override
    public void update() {
    	
    	if(!freezing){
            if(moving) move();
            else if(attacking) attack();
            
        	int r = rand.nextInt(100);
            if(r < 4){
                int tmp = rand.nextInt(4);
                if(!(em.getMap().isHit(getX(), getY(), tmp))) {
                	this.dir = tmp;
                	moving = true;
                }
            }else if(r < 10){
            	attacking = true;
            }
            
            
        }else{
            freezeCount--;
            if(freezeCount == 0)
                freezing = false;
        }
    }

    @Override
    protected void attack() {
        attackMessage();
    	if(!em.getMap().isHit(x, y, this.dir)){
        	em.addEntity(new WhiteMagic(em,getX(),getY(),this.dir));
                attackMessage();
    	}
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
