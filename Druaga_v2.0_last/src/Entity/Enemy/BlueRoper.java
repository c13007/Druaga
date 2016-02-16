package Entity.Enemy;

import Entity.Entity;
import Entity.EntityManager;
import Main.MainPanel;
import System.ValueRange;
import java.awt.Color;
import java.util.Random;
import javax.swing.ImageIcon;

public class BlueRoper extends Entity {

    Random rand;

    public BlueRoper(EntityManager em, int x, int y) {
        super(em, 3, x, y);
    }

    @Override
    public void init() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/blueroper.png"));
            image = icon.getImage();

            lifePoint = 120;
            maxLifePoint = 120;
            attackPoint = 40;
            defensePoint = 25;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dir = DOWN;
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
            }
            if (attacking) {
                attack();
            } else {
                if (em.findFoword(this) == em.getPlayer()) {
                    attacking = true;
                } else {
                    if (rand.nextInt(200) < 2) {
                        if (em.getMap().isHit(x, y, getDir())) {
                            while (true) {
                                int tmp = rand.nextInt(4);
                                if (!em.getMap().isHit(x, y, tmp) && em.findFoword(this) == null) {
                                    this.dir = tmp;
                                    break;
                                }
                            }
                        }
                        moving = true;
                    }
                }
            }
        } else {
            freezeCount--;
            if (freezeCount == 0) {
                freezing = false;
            }
        }
    }

    @Override
    public void draw(java.awt.Graphics2D g) {
        g.drawImage(image,
                px, py,
                px + MainPanel.CELL_SIZE, py + MainPanel.CELL_SIZE,
                0, frame * MainPanel.CELL_SIZE,
                MainPanel.CELL_SIZE, frame * MainPanel.CELL_SIZE + MainPanel.CELL_SIZE,
                null);

        if (showLifePoint) {
            back.setRect(px - 1, py - 3, 18, 4);
            g.setColor(Color.BLACK);
            g.fill(back);

            int length = ValueRange.rangeOf((int) (16 * (lifePoint / (double) maxLifePoint)), 0, maxLifePoint);
            life.setRect(px, py - 2, length, 2);
            g.setColor(length <= 4 ? Color.RED : Color.GREEN);
            g.fill(life);
        }

    }

    @Override
    protected void attack() {
        attackMessage();
        em.getPlayer().damage(attackPoint);
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
