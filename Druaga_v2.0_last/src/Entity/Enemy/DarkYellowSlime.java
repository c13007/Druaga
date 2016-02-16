package Entity.Enemy;

import Entity.Magic.GreenMagic;
import Entity.*;
import Main.MainPanel;
import java.util.Random;
import javax.swing.ImageIcon;

public class DarkYellowSlime extends Entity {

    Random rand;

    public DarkYellowSlime(EntityManager em, int x, int y) {
        super(em, 6, x, y);
    }

    @Override
    public void init() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/darkyellowslime.png"));
            image = icon.getImage();

            lifePoint = 50;
            maxLifePoint = 50;
            attackPoint = 15;
            defensePoint = 10;

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
        em.addEntity(new GreenMagic(em, getX(), getY(), this.dir));
        MainPanel.wave.stop("EnemyMagic");
        MainPanel.wave.play("EnemyMagic");
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
