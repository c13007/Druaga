package Entity.Enemy;

import Entity.Entity;
import Entity.EntityManager;
import Main.MainPanel;
import Map.Map;
import System.ValueRange;

import java.awt.Color;
import java.util.Random;
import javax.swing.ImageIcon;

public class BlueOrb extends Entity {

    Random rand;

    public BlueOrb(EntityManager em, int x, int y) {
        super(em, 2, x, y);
    }

    @Override
    public void init() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/blue_willo_wisp.png"));
            image = icon.getImage();

            lifePoint = 60;
            maxLifePoint = 60;
            attackPoint = 20;
            defensePoint = 10;

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dir = Entity.DOWN;
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

                    if (!moving) {

                        if (this.dir == Entity.DOWN) {
                            if (!em.getMap().isHit(x, y, Entity.RIGHT)) {
                                this.dir = Entity.RIGHT;
                            } else if (!em.getMap().isHit(x, y, Entity.DOWN)) {
                                this.dir = Entity.DOWN;
                            } else if (!em.getMap().isHit(x, y, Entity.LEFT)) {
                                this.dir = Entity.LEFT;
                            } else if (!em.getMap().isHit(x, y, Entity.UP)) {
                                this.dir = Entity.UP;
                            }
                        } else if (this.dir == Entity.UP) {
                            if (!em.getMap().isHit(x, y, Entity.LEFT)) {
                                this.dir = Entity.LEFT;
                            } else if (!em.getMap().isHit(x, y, Entity.UP)) {
                                this.dir = Entity.UP;
                            } else if (!em.getMap().isHit(x, y, Entity.RIGHT)) {
                                this.dir = Entity.RIGHT;
                            } else if (!em.getMap().isHit(x, y, Entity.DOWN)) {
                                this.dir = Entity.DOWN;
                            }
                        } else if (this.dir == Entity.LEFT) {
                            if (!em.getMap().isHit(x, y, Entity.DOWN)) {
                                this.dir = Entity.DOWN;
                            } else if (!em.getMap().isHit(x, y, Entity.LEFT)) {
                                this.dir = Entity.LEFT;
                            } else if (!em.getMap().isHit(x, y, Entity.UP)) {
                                this.dir = Entity.UP;
                            } else if (!em.getMap().isHit(x, y, Entity.RIGHT)) {
                                this.dir = Entity.RIGHT;
                            }
                        } else if (this.dir == Entity.RIGHT) {
                            if (!em.getMap().isHit(x, y, Entity.UP)) {
                                this.dir = Entity.UP;
                            } else if (!em.getMap().isHit(x, y, Entity.RIGHT)) {
                                this.dir = Entity.RIGHT;
                            } else if (!em.getMap().isHit(x, y, Entity.DOWN)) {
                                this.dir = Entity.DOWN;
                            } else if (!em.getMap().isHit(x, y, Entity.LEFT)) {
                                this.dir = Entity.LEFT;
                            }
                        }

                    }
                    moving = true;

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
                0, 0, MainPanel.CELL_SIZE, MainPanel.CELL_SIZE,
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
    protected void move() {
        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        //移動先の座標の指定
        int nextX = ValueRange.rangeOf(x + dx[dir], 0, Map.COL);
        int nextY = ValueRange.rangeOf(y + dy[dir], 0, Map.ROW);

        //移動可能なときだけ
        if (!em.getMap().isHit(x, y, dir) && !(em.findFowordPlayer(this))) {
            //描画座標の更新
            px = ValueRange.rangeOf(px + SPEED * dx[dir], 0, MainPanel.WIDTH - MainPanel.CELL_SIZE);
            py = ValueRange.rangeOf(py + SPEED * dy[dir], 0, MainPanel.HEIGHT - MainPanel.CELL_SIZE);

            //移動量の更新
            moveLength += SPEED;
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

    @Override
    protected void attack() {
        attackMessage();
        em.getPlayer().damage(attackPoint);
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
