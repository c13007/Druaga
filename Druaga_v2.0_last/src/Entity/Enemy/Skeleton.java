package Entity.Enemy;

import Entity.Entity;
import Entity.EntityManager;
import static Entity.Entity.DOWN;
import Main.MainPanel;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.ImageIcon;

public class Skeleton extends Entity {

    Random rand;

    private LinkedList path;
    private int currentPos;

    private int[][] mv = {{0, 3, 1, 2}, {1, 0, 2, 3}, {2, 3, 1, 0}, {3, 2, 0, 1}};

    public Skeleton(EntityManager em, int x, int y) {
        super(em, 16, x, y);
    }

    @Override
    public void init() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/skeleton.png"));
            image = icon.getImage();

            lifePoint = 460;
            maxLifePoint = 460;
            attackPoint = 170;
            defensePoint = 80;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dir = DOWN;
        this.frame = 0;
        this.frameMax = 0;
        this.currentPos = 0;

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
                    if (rand.nextInt(100) < 5) {

                        if (em.getMap().isHit(x, y, getDir())) {
                            for (int i = 0; i < 4; i++) {
                                if (!em.getMap().isHit(x, y, mv[this.dir][i]) && em.findFoword(this) == null) {
                                    this.dir = mv[this.dir][i];
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
    protected void attack() {
        attackMessage();
        System.out.println("slime: attack!");
        em.getPlayer().damage(attackPoint);
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }

}
