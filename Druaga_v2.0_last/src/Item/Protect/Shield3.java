package Item.Protect;

import Item.Item;
import Item.ItemManager;
import javax.swing.ImageIcon;

public class Shield3 extends Item{
    
    public Shield3(ItemManager im, int x, int y){
        super(103, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/shield_3.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 60;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().setProtect(this);
        used = true;
    }
}
