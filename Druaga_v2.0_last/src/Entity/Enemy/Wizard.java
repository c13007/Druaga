package Entity.Enemy;

import Entity.Magic.GreenMagic;
import Entity.*;
import Main.MainPanel;
import System.ValueRange;
import java.awt.Color;
import java.util.Random;
import javax.swing.ImageIcon;

//未完成  ::  hide()
public class Wizard extends Entity {

    Random rand;

    private boolean display;

    public Wizard(EntityManager em, int x, int y) {
        super(em, 18, x, y);
    }

    @Override
    public void init() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/wizard.png"));
            image = icon.getImage();

            lifePoint = 300;
            maxLifePoint = 300;
            attackPoint = 150;
            defensePoint = 75;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.frame = 0;
        this.frameMax = 0;
        this.display = false;

        px = x * MainPanel.CELL_SIZE;
        py = y * MainPanel.CELL_SIZE;

        rand = new Random();
    }

    @Override
    public void update() {

        int[] mask = {2, -2, -2, 2};

        if (rand.nextInt(400) < 2 && !display) {
            int plx = em.getPlayer().getX();
            int ply = em.getPlayer().getY();
            if (!(plx >= 22 || ply >= 22 || plx <= 2 || ply <= 2)) {
                display = true;
                int s = rand.nextInt(4);
                if (s == 0 || s == 2) {
                    x = ValueRange.rangeOf(rand.nextInt(plx + 1) + (plx - 1), 1, 23);
                    y = ValueRange.rangeOf(ply + mask[s], 1, 23);

                } else {
                    x = ValueRange.rangeOf(plx + mask[s], 1, 23);
                    y = ValueRange.rangeOf(rand.nextInt(ply + 1) + (ply - 1), 1, 23);
                }
                this.dir = s;
                px = x * MainPanel.CELL_SIZE;
                py = y * MainPanel.CELL_SIZE;
            }
        }

        if (display) {
            if (rand.nextInt(100) < 2) {
                attack();
            }
        }

        if (rand.nextInt(500) < 2) {
            display = false;
            dead();
        }
    }

    @Override
    public void draw(java.awt.Graphics2D g) {
        if (display) {
            g.drawImage(image,
                    px, py,
                    px + MainPanel.CELL_SIZE, py + MainPanel.CELL_SIZE,
                    dir * MainPanel.CELL_SIZE, frame * MainPanel.CELL_SIZE,
                    dir * MainPanel.CELL_SIZE + MainPanel.CELL_SIZE, frame * MainPanel.CELL_SIZE + MainPanel.CELL_SIZE,
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
