package Entity.Enemy;

import Entity.Magic.GreenMagic;
import Entity.*;
import Main.MainPanel;
import java.util.Random;
import javax.swing.ImageIcon;

public class DarkGreenSlime extends Entity {

    Random rand;

    public DarkGreenSlime(EntityManager em, int x, int y) {
        super(em, 5, x, y);
    }

    @Override
    public void init() {

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/darkgreenslime.png"));
            image = icon.getImage();

            lifePoint = 30;
            maxLifePoint = 30;
            attackPoint = 10;
            defensePoint = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.frame = 0;
        this.frameMax = 0;

        px = x * MainPanel.CELL_SIZE;
        py = y * MainPanel.CELL_SIZE;

        rand = new Random();

    }

    @Override
    public void update() {

        if (!freezing) {
            if (moving) {
                move();
            } else if (attacking) {
                attack();
            }

            int r = rand.nextInt(100);
            if (r < 4) {
                int tmp = rand.nextInt(4);
                if (!(em.getMap().isHit(getX(), getY(), tmp))) {
                    this.dir = tmp;
                    moving = true;
                }
            } else if (r < 10) {
                attacking = true;
            }

        } else {
            freezeCount--;
            if (freezeCount == 0) {
                freezing = false;
            }
        }

    }

    @Override
    protected void attack() {
        attackMessage();
        switch (rand.nextInt(4)) {
            case 0:
                em.addEntity(new GreenMagic(em, rand.nextInt(23) + 1, 23, Entity.UP));
                break;
            case 1:
                em.addEntity(new GreenMagic(em, 1, rand.nextInt(23) + 1, Entity.RIGHT));
                break;
            case 2:
                em.addEntity(new GreenMagic(em, rand.nextInt(23) + 1, 1, Entity.DOWN));
                break;
            case 3:
                em.addEntity(new GreenMagic(em, 23, rand.nextInt(23) + 1, Entity.LEFT));
                break;
        }

        MainPanel.wave.stop("EnemyMagic");
        MainPanel.wave.play("EnemyMagic");

        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
