/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Item.Item;
import Item.ItemManager;
import Main.MainPanel;
import Map.Map;
import System.ValueRange;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

import GameState.GameStateManager;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author takahito
 */
public class Player extends Entity{
    
    public ItemManager im;
    
    private Item weapon;
    private Item protect;
    
    int score;
    
    //ミリ秒
    long startTime;
    long maxtime;
    //秒
    long timeLeft;
    
    Font font;
    
    public Player(EntityManager em, int x, int y){
        super(em, 0,  x, y);
        score = 0;
        
        //制限時間　20分 + 調整分
        maxtime = 20 * 60 * 1000 + (9 * 1000 + 500);
        startTime = System.currentTimeMillis();
        
        font = new Font( "Century", Font.PLAIN, 10);
    }
    
    public int getScore(){ return score; }
    public long getTimeLeft(){ return timeLeft; }
    public void addScore(int s){ score += s; }
    
    public void setItemManager(ItemManager im){
        this.im = im;
    }
    
    public void setWeapon(Item w){
        this.weapon = w;
        weapon.setPos(0, Map.COL);
    }
    
    public void setProtect(Item p){
        this.protect = p;
        protect.setPos(1, Map.COL);
    }
    
    public void climb(){
    	
        em.getMapManager().climb();
        em.getClient().write("Client," + em.getClient().now()+ "," + getStaticId()
                + "," + getId() + "," + em.getMapManager().getCurrentFloor()
                + ",climb," + x + "," + y);
        em.init();
        im.init();
        
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/player.png"));
            image = icon.getImage();
            
            lifePoint = 500;
            maxLifePoint = 500;
            attackPoint = 10;
            defensePoint = 0;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.dir = DOWN;
        this.frame = 0;
        this.frameMax = 4;
        
        px = x * MainPanel.CELL_SIZE;
        py = y * MainPanel.CELL_SIZE;
        
        weapon = null;
        protect = null;
    }

    @Override
    public void update() {
        time();
        if(!freezing){
            if(moving) move();
            if(attacking) attack();
        }else{
            freezeCount--;
            if(freezeCount == 0)
                freezing = false;
        }
    }
    
    @Override
    public void draw(Graphics2D g){
    	
        super.draw(g);
        
        if(weapon != null) weapon.draw(g);
        if(protect != null) protect.draw(g);
        
        //残り時間
        g.setFont(font);
        g.setColor(timeLeft > 60 ? Color.GREEN : Color.RED);
        g.drawString(
                String.format("[ Time ]  %2d : %2d", timeLeft / 60, timeLeft % 60),
                Map.COL * MainPanel.CELL_SIZE / 2 + 10,
                Map.ROW *MainPanel.CELL_SIZE + 12);
        
        //スコア
        g.setColor(Color.GREEN);
        g.drawString(
                String.format("Score: %5d", score),
                Map.COL * MainPanel.CELL_SIZE / 2 - 100,
                Map.ROW *MainPanel.CELL_SIZE + 12);
    }

    public void keyPressed(int k) {
        if(!moving && !attacking){
            switch(k){
                case KeyEvent.VK_UP:
                    dir = UP;
                    moving = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    dir = RIGHT;
                    moving = true;
                    break;
                case KeyEvent.VK_DOWN:
                    dir = DOWN;
                    moving = true;
                    break;
                 case KeyEvent.VK_LEFT:
                    dir = LEFT;
                    moving = true;
                    break;   
                case KeyEvent.VK_Z:
                    attacking = true;
                    break;
                case KeyEvent.VK_X:
                    if(breakWall())
                        score = ValueRange.rangeOf(score - 100, 0, score);
                    break;
            }
        }
    }

    public void keyReleased(int k) {}
    
    @Override
    public void attack(){
        //目の前の敵を探す
        Entity e = em.findFoword(this);
        if(e != null) e.damage(weapon == null ? attackPoint : weapon.getPoint());
        System.out.println("player: attack!");
       
    	MainPanel.wave.stop("PlayerBattle");
    	MainPanel.wave.play("PlayerBattle");
        
        attacking = false;
        freezeCount = 15;
        freezing = true;
        
    }    

    @Override
    public void damage(int attackPoint) {
        int damage = attackPoint - (protect == null ? 0 : protect.getPoint());
        lifePoint -= ValueRange.rangeOf(damage, 0, maxLifePoint);
        //System.out.println("player:damage! (" + lifePoint +  ")");
        MainPanel.wave.stop("EnemyBattle");
        MainPanel.wave.play("EnemyBattle");
        if(lifePoint < 0)
            dead();
    }

    @Override
    public void dead() {
    	
    	MainPanel.wave.stop("Main");
    	MainPanel.wave.stop("Main2");
    	MainPanel.wave.stop("Boss");
    	
    	try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    	
    	MainPanel.gsm.setState(GameStateManager.GAMEOVER_STATE);
        //
        em.getClient().write("Client," + em.getClient().now()+ "," + getStaticId()
                + "," + getStaticId() + "," + em.getMapManager().getCurrentFloor()
                + ",gameOver");
    	
    }
    
    private void time(){
        //経過時間 = 現在時間ースタート時間
        long pass = System.currentTimeMillis() - startTime;
        
        //秒に直す
        timeLeft = (maxtime - pass) / 1000;
        if(timeLeft < 0) dead();
    }
    
    
}
