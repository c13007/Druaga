package Item.Weapon;

import Item.Item;
import Item.ItemManager;
import javax.swing.ImageIcon;

public class Sword5 extends Item{

    public Sword5(ItemManager im, int x, int y){
        super(205, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/sword_5.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 150;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().setWeapon(this);
        used = true;
    }
    
}
