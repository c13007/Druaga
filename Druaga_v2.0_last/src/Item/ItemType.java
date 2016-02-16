package Item;

import Item.Weapon.*;
import Item.Protect.*;
import Map.Map;
import java.util.Random;

public class ItemType {
    
    static Random rand = new Random();
    
    public static Item summonSword(ItemManager im, int curentFloor){
        
        int x = rand.nextInt(Map.COL - 3) + 1;
        int y = rand.nextInt(Map.ROW - 3) + 1;
        Item i;
        
        switch(curentFloor){
            case 0: i = new Sword1(im, x, y); break;
            case 1: i = new Sword2(im, x, y); break;
            case 2: i = new Sword3(im, x, y); break;
            case 3: i = new Sword4(im, x, y); break;
            case 4: i = new Sword5(im, x, y); break;
            default: return null;
        }
        return i;
    }
    
    public static Item summonShield(ItemManager im, int curentFloor){
        
        int x = rand.nextInt(Map.COL - 3) + 1;
        int y = rand.nextInt(Map.ROW - 3) + 1;
        Item i;
        
        switch(curentFloor){
            case 0: i = new Shield1(im, x, y); break;
            case 1: i = new Shield2(im, x, y); break;
            case 2: i = new Shield3(im, x, y); break;
            case 3: i = new Shield4(im, x, y); break;
            case 4: i = new Shield5(im, x, y); break;
            default: return null;
        }
        return i;
    }
}
