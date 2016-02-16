package Item;

import javax.swing.ImageIcon;

public class Stairs extends Item{
    
    public Stairs(ItemManager im, int x, int y){
        super(0, x, y);
        this.im = im;
        init();
    }

    @Override
    public void init() {
        try{
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/item/stairs.png"));
            image = icon.getImage();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        this.point = 0;
    }

    @Override
    public void effect() {
        im.getPlayer().climb();
        used = true;
    }
}
