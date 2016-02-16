package Item.Protect;

import Item.Item;
import Item.ItemManager;
import javax.swing.ImageIcon;

public class Shield2 extends Item{
    
    public Shield2(ItemManager im, int x, int y){
        super(102, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/shield_2.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 40;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().setProtect(this);
        used = true;
    }
}
