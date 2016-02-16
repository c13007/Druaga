package Entity.Enemy;

import Entity.Magic.RedMagic;
import Entity.Magic.GreenMagic;
import Entity.Magic.BlueMagic;
import Entity.*;
import Main.MainPanel;
import Map.Map;
import System.ValueRange;
import java.awt.Image;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;

public class Druaga extends Entity {

    Random rand;

    private boolean dflg;
    private Image image2;

    public Druaga(EntityManager em, int x, int y) {
        super(em, 20, x, y);
    }

    @Override
    protected void attack() {
    }

    private class GreenMagicD extends GreenMagic {

        public GreenMagicD(EntityManager em, int x, int y, int dir) {
            super(em, x, y, dir);

        }

        @Override
        protected void move() {

            //移動先に使う座標
            int[] dx = {1, 1, -1, -1};
            int[] dy = {-1, 1, 1, -1};

            //移動先の座標の指定
            int nextX = ValueRange.rangeOf(x + dx[dir], 0, Map.COL);
            int nextY = ValueRange.rangeOf(y + dy[dir], 0, Map.ROW);

            //描画座標の更新
            px = ValueRange.rangeOf(px + 2 * dx[dir], 0, MainPanel.WIDTH - MainPanel.CELL_SIZE);
            py = ValueRange.rangeOf(py + 2 * dy[dir], 0, MainPanel.HEIGHT - MainPanel.CELL_SIZE);

            //移動量の更新
            moveLength += 2;
            frame++;
            if (frame > frameMax) {
                frame = 0;
            }

            //移動完了時の処理
            if (moveLength >= MainPanel.CELL_SIZE) {

                //マップ座標の更新
                x = nextX;
                y = nextY;

                //描画座標の更新
                px = x * MainPanel.CELL_SIZE;
                py = y * MainPanel.CELL_SIZE;

                //移動状態の解消
                moving = false;
                moveLength = 0;
                frame = 0;

            }

        }
    }

    @Override
    public void init() {

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/dragon.png"));
            image = icon.getImage();
            icon = new ImageIcon(getClass().getResource("/images/entity/dragonpowerdown.png"));
            image2 = icon.getImage();

            lifePoint = 1000;
            maxLifePoint = 1000;
            attackPoint = 50;
            defensePoint = 500;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.frame = 0;
        this.frameMax = 0;

        this.dflg = false;

        px = x * MainPanel.CELL_SIZE;
        py = y * MainPanel.CELL_SIZE;

        rand = new Random();

    }

    @Override
    public void update() {

        if (!freezing) {

            if (!dflg) {
                if (checkOtherEntity()) {
                    this.defensePoint = 10;
                    this.image = image2;
                    dflg = true;
                }
            }

            if (moving) {
                move();
            } else if (attacking) {
                attack1();
                if (rand.nextInt(3) < 2) {
                    attack2();
                }
                if (rand.nextInt(3) < 2) {
                    attack3();
                }
            }

            int r = rand.nextInt(100);
            if (r < 2) {
                x = ValueRange.rangeOf(rand.nextInt(22) + 2, 1, 23);
                y = ValueRange.rangeOf(rand.nextInt(22) + 2, 1, 23);
                px = x * MainPanel.CELL_SIZE;
                py = y * MainPanel.CELL_SIZE;
            } else if (r < 10) {
                int tmp = rand.nextInt(4);
                if (!(em.getMap().isHit(getX(), getY(), tmp))) {
                    this.dir = tmp;
                    moving = true;
                }
            } else if (r < 15) {
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
    protected void move() {
        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        //移動先の座標の指定
        int nextX = ValueRange.rangeOf(x + dx[dir], 0, Map.COL);
        int nextY = ValueRange.rangeOf(y + dy[dir], 0, Map.ROW);

        //移動可能なときだけ
        if (!em.getMap().isHit(x, y, dir) && em.findFoword(this) == null) {
            //描画座標の更新
            px = ValueRange.rangeOf(px + 4 * dx[dir], 0, MainPanel.WIDTH - MainPanel.CELL_SIZE);
            py = ValueRange.rangeOf(py + 4 * dy[dir], 0, MainPanel.HEIGHT - MainPanel.CELL_SIZE);

            //移動量の更新
            moveLength += 4;
            frame++;
            if (frame > frameMax) {
                frame = 0;
            }

            //移動完了時の処理
            if (moveLength >= MainPanel.CELL_SIZE) {

                //マップ座標の更新
                x = nextX;
                y = nextY;

                //描画座標の更新
                px = x * MainPanel.CELL_SIZE;
                py = y * MainPanel.CELL_SIZE;

                //移動状態の解消
                moveEnd();
                moveLength = 0;
                frame = 0;
            }
        } else {
            moving = false;
            px = x * MainPanel.CELL_SIZE;
            py = y * MainPanel.CELL_SIZE;
        }
    }

    protected void attack1() {
        attackMessage();
        for (int i = 0; i < 3; i++) {
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
        }

        MainPanel.wave.stop("EnemyMagic");
        MainPanel.wave.play("EnemyMagic");

        freezeCount = 120;
        freezing = true;
        attacking = false;
    }

    protected void attack2() {
        attackMessage();
        em.addEntity(new GreenMagicD(em, getX(), getY(), Entity.UP));
        em.addEntity(new GreenMagicD(em, getX(), getY(), Entity.DOWN));
        em.addEntity(new GreenMagicD(em, getX(), getY(), Entity.RIGHT));
        em.addEntity(new GreenMagicD(em, getX(), getY(), Entity.LEFT));
        MainPanel.wave.stop("EnemyMagic");
        MainPanel.wave.play("EnemyMagic");
        freezeCount = 120;
        freezing = true;
        attacking = false;

    }

    protected void attack3() {
        attackMessage();
        em.addEntity(new GreenMagic(em, getX(), getY(), Entity.UP));
        em.addEntity(new GreenMagic(em, getX(), getY(), Entity.DOWN));
        em.addEntity(new GreenMagic(em, getX(), getY(), Entity.RIGHT));
        em.addEntity(new GreenMagic(em, getX(), getY(), Entity.LEFT));
        MainPanel.wave.stop("EnemyMagic");
        MainPanel.wave.play("EnemyMagic");
        freezeCount = 120;
        freezing = true;
        attacking = false;

    }

    @Override
    public void dead() {
        super.dead();
        em.end();
    }

    public boolean checkOtherEntity() {

        List<Entity> ent = em.getList();
        int counter = 0;

        for (Entity e : ent) {
            if (e instanceof GreenMagic) {
                continue;
            }
            if (e instanceof BlueMagic) {
                continue;
            }
            if (e instanceof RedMagic) {
                continue;
            }
            counter++;
        }

        if (counter == 2) {
            return true;
        } else {
            return false;
        }

    }

}
