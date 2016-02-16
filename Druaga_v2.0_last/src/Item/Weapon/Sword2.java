package Item.Weapon;

import Item.Item;
import Item.ItemManager;
import javax.swing.ImageIcon;

public class Sword2 extends Item{

    public Sword2(ItemManager im, int x, int y){
        super(202, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/sword_2.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 40;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().setWeapon(this);
        used = true;
    }
    
}
