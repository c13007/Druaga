package Entity.Enemy;

import Entity.Entity;
import Entity.EntityManager;
import static Entity.Entity.DOWN;
import Main.MainPanel;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author takahito
 */
public class BlackSlime extends Entity{
    
    Random rand;
    
    public BlackSlime(EntityManager em, int x, int y){
        super(em, 1, x, y);
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/blackslime.png"));
            image = icon.getImage();
            
            lifePoint = 350;
            maxLifePoint = 350;
            attackPoint = 150;
            defensePoint = 80;
            
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
