package Item;

import Client.Client;
import Entity.Entity;
import Entity.Player;
import GameState.Option;
import Map.Map;
import Map.MapManager;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ItemManager {
    
    private Player player;
    private MapManager mm;
    private ArrayList<Item> items;
    
    private Client client;
    
    
    public ItemManager(MapManager mm, Entity player){
        this.mm = mm;
        this.player = (Player)player;
        this.player.setItemManager(this);
        
        items = new ArrayList<>();
    }
    
    public Player getPlayer(){ return player; }
    public Item getItem(int index){ return items.get(index); }
    public int getMaxId(){
        int max = 0;
        for(Item i: items) max = max > i.getId() ? max : i.getId();
        return max;
    }
    
    public Client getClient(){ return client;}
    public void setClient(Client c){ this.client = c;}
    
    public void init(){
        items.clear();
        if(mm.getCurrentFloor() != mm.getMaxFloor() - 1){
            items.add(new Stairs(this, mm.findGoal().getX(), mm.findGoal().getY()));
            items.add(ItemType.summonSword(this, mm.getCurrentFloor()));
            items.add(ItemType.summonShield(this, mm.getCurrentFloor()));
        }
        //ポーション追加
        int[] count = {3, 6, 12};
        Random rand = new Random();
        int x, y;
        for(int i = 0; i < count[Option.getItemMount()]; i++){
            x = 1 + rand.nextInt(Map.COL - 2);
            y = 1 + rand.nextInt(Map.ROW - 2);
            items.add(new Portion(this, x, y));
        }
        
        items.forEach(i ->{
                client.write("Client," + client.now()+ "," 
                        + i.getStaticId() + "," + i.getId() + 
                        ", 0,summon,item," + i.getX() + "," + i.getY());
        });
        
    }
    
    public void removeUsedItem(){
        Iterator<Item> i = items.iterator();
        while(i.hasNext()){
            if(i.next().used() == true)
                i.remove();
        }
    }
    
    public void update(){
        removeUsedItem();
    }
    
    public void draw(Graphics2D g){
        items.forEach(i -> i.draw(g));
    }
    
    public Item find(Entity me){
        
        for(Item i: items){
            if(me.getX() == i.getX() && me.getY() == i.getY())
                return i;
        }
        return null;
    }
    
    public Item find(int id){
        for(Item i: items){
            if(i.getId() == id)
                return i;
        }
        return null;
    }
}
