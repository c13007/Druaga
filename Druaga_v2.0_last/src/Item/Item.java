/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Item;

import Main.MainPanel;
import java.awt.Image;

/**
 *
 * @author c13007
 */
public abstract class Item {
    
    public ItemManager im;
    
    private int staticId;
    private int id;
    
    protected int x;
    protected int y;
    protected int px;
    protected int py;
    
    protected boolean used;
    
    public Image image;
    
    protected int point;

    public Item(int staticId, int x, int y) {
        this.staticId = staticId;
        setPos(x, y);
        this.used = false;
    }
    
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getPoint(){ return point; }
    public int getStaticId(){ return staticId; }
    public int getId(){ return id; }
    public boolean used(){ return used; }
    
    public abstract void init();
    
    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
        
        this.px = x * MainPanel.CELL_SIZE;
        this.py = y * MainPanel.CELL_SIZE;
    }
    
    public void draw(java.awt.Graphics2D g){
        g.drawImage(image,
                px, py,
                null);
    }
     
    public  void effect(){
        im.getClient().write("Client," + im.getClient().now()+ "," + getStaticId()
                + "," + getId() + "," + 0
                + ",item," + x + "," + y);
    }
    
    
}
