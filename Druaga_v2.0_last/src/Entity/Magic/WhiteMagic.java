/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Magic;

import Entity.Entity;
import Entity.EntityManager;
import Entity.Player;
import java.awt.Color;

import javax.swing.ImageIcon;

import Main.MainPanel;
import Map.Map;
import System.ValueRange;

/**
 *
 * @author takahito
 */
public class WhiteMagic extends Entity {

    private int direction;
    private Entity en;
    
    public WhiteMagic(EntityManager em,int x,int y,int dir) {
        super(em,104,x,y);
        this.direction = dir;
    }

    @Override
    public void init() {
    	
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/magicwhite.png"));
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
        
        
    }

    @Override
    public void update() {
    	
    	moving = true;
    	dir = this.direction;
    	
		if(moving){
			move();
			if(em.findFoword(this) instanceof Player) {
				attack();
				dead();
			}
			if(!(!em.getMap().isHit(x, y, dir)) || em.findFoword(this) != null){
				dead();
			}
		}
    	
    }
    
    @Override
    protected void move(){
    	
        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        //移動先の座標の指定
        int nextX = ValueRange.rangeOf(x + dx[dir], 0, Map.COL);
        int nextY = ValueRange.rangeOf(y + dy[dir], 0, Map.ROW);

        //移動可能なときだけ
        if (!em.getMap().isHit(x, y, dir) && em.findFoword(this) == null) {
            //描画座標の更新
            px = ValueRange.rangeOf(px + 2 * dx[dir], 0, MainPanel.WIDTH - MainPanel.CELL_SIZE);
            py = ValueRange.rangeOf(py + 2 * dy[dir], 0, MainPanel.HEIGHT - MainPanel.CELL_SIZE);

            //移動量の更新
            moveLength += 2;
            frame++;
            if(frame > frameMax) frame = 0;

            //移動完了時の処理
            if (moveLength >= MainPanel.CELL_SIZE) {

                //マップ座標の更新
                x = nextX;
                y = nextY;

                //描画座標の更新
                px = x * MainPanel.CELL_SIZE;
                py = y * MainPanel.CELL_SIZE;

                //移動状態の解消
                moving = false;
                moveLength = 0;
                frame = 0;
            }
        } else {
            moving = false;
            px = x * MainPanel.CELL_SIZE;
            py = y * MainPanel.CELL_SIZE;
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
    	System.out.println("magic : attack");
        em.getPlayer().damage(attackPoint);
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }

    @Override
    public void damage(int attackPoint) {
    }

    @Override
    public void dead() {
        dead = true;
    }
}
