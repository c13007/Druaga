package Entity.Enemy;

import Entity.Entity;
import Entity.EntityManager;
import static Entity.Entity.DOWN;
import Main.MainPanel;
import java.util.Random;
import javax.swing.ImageIcon;

public class GreenSlime extends Entity{
    
    Random rand;
    
    public GreenSlime(EntityManager em, int x, int y){
        super(em, 10, x, y);
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/greenslime.png"));
            image = icon.getImage();
            
            lifePoint = 200;
            maxLifePoint = 200;
            attackPoint = 100;
            defensePoint = 60;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.dir = DOWN;
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
            if(attacking) attack();
            else{
                if(em.findFoword(this) == em.getPlayer())
                    attacking = true;
                else{
                    if(rand.nextInt(100) < 2){
                        moving = true;
                        dir = rand.nextInt(4); 
                    }
                }  
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
        em.getPlayer().damage(attackPoint);
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
