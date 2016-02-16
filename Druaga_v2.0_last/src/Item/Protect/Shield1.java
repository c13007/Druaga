package Item.Protect;

import Item.Item;
import Item.ItemManager;
import javax.swing.ImageIcon;

public class Shield1 extends Item{
    
    public Shield1(ItemManager im, int x, int y){
        super(101, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/shield_1.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 20;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().setProtect(this);
        used = true;
    }
}
