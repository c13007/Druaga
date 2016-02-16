/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Magic;

import Entity.Entity;
import Entity.EntityManager;
import java.awt.Color;
import java.util.Random;

import javax.swing.ImageIcon;

import Main.MainPanel;
import Map.Map;
import System.ValueRange;

/**
 *
 * @author takahito
 */
public class RedMagic extends Entity {

    private Entity en;
    Random rand;
    
    private int direction;
    
    public RedMagic(EntityManager em,int x,int y,int dir) {
        super(em,103,x,y);
        this.direction = dir;
    }

    @Override
    public void init() {
    	
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/magicred.png"));
            image = icon.getImage();

            lifePoint = 1;
            maxLifePoint = 1;
            attackPoint = 20;
            defensePoint = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        px = x * MainPanel.CELL_SIZE;
        py = y * MainPanel.CELL_SIZE;
        
        this.frame = 0;
        this.frameMax = 0;
        this.showLifePoint = false;
        
        rand = new Random();
        
    }

    @Override
    public void update() {
    	
    	if(x == em.getPlayer().getX() && y == em.getPlayer().getY()) {
    		if(rand.nextInt(50) < 2) attack();
		}
    	
    	if(rand.nextInt(1000) < 2){
    		dead();
    	}
    	
    }
    
    
    @Override
    public void draw(java.awt.Graphics2D g){
        g.drawImage(image,
                px, py, 
                px + MainPanel.CELL_SIZE, py + MainPanel.CELL_SIZE,
                0, 0,
                MainPanel.CELL_SIZE,MainPanel.CELL_SIZE,
                null);
        
    }
    

    @Override
    protected void attack() {
    	System.out.println("redmagic : attack");
        em.getPlayer().damage(attackPoint);
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }

    @Override
    public void damage(int attackPoint) {
    	lifePoint -= attackPoint;
        System.out.println("fire damage! (" + lifePoint +  ")");
        if(lifePoint <= 0)
            dead();
    }

    @Override
    public void dead() {
        dead = true;
    }
}
