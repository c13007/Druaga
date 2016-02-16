package Item.Protect;

import Item.Item;
import Item.ItemManager;
import javax.swing.ImageIcon;

public class Shield5 extends Item{
    
    public Shield5(ItemManager im, int x, int y){
        super(105, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/shield_5.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 100;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().setProtect(this);
        used = true;
    }
}
