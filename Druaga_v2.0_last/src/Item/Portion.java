package Item;

import javax.swing.ImageIcon;

public class Portion extends Item{
    
    public Portion(ItemManager im, int x, int y){
        super(10, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/portion.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 70;
    }

    @Override
    public void effect() {
        super.effect();
        im.getPlayer().addLifePoint(point);
        used = true;
    }
    
}
