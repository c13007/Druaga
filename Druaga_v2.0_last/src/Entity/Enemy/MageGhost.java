package Entity.Enemy;

import Entity.Magic.WhiteMagic;
import Entity.*;
import Main.MainPanel;
import Map.Map;
import Map.Position;
import System.ValueRange;
import java.util.Random;
import javax.swing.ImageIcon;

public class MageGhost extends Entity {

    Random rand;
    private LOS los;
    private Position positionOld;

    public MageGhost(EntityManager em, int x, int y) {
        super(em, 12, x, y);
        los = new LOS(em, x, y);
        positionOld = new Position(x, y);
    }

    @Override
    public void init() {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/entity/mageghost.png"));
            image = icon.getImage();

            lifePoint = 250;
            maxLifePoint = 250;
            attackPoint = 135;
            defensePoint = 70;

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
                if ((x >= 23 || y >= 23 || x <= 1 || y <= 1)) {
                    dead();
                }
            } else if (attacking) {
                attack();
            }

            int r = rand.nextInt(100);
            if (r < 7) {

                if (!moving) {

                    los.buildPathTo(em.getPlayer().getX(), em.getPlayer().getY(), x, y);

                    positionOld = los.chaseByLOS();

                    int dix = positionOld.getX() - x;
                    int diy = positionOld.getY() - y;

                    if (dix == 0 && diy == -1) {
                        this.dir = Entity.UP;
                    } else if (dix == 0 && diy == 1) {
                        this.dir = Entity.DOWN;
                    } else if (dix == -1 && diy == 0) {
                        this.dir = Entity.LEFT;
                    } else if (dix == 1 && diy == 0) {
                        this.dir = Entity.RIGHT;
                    }

                }

                moving = true;
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
    protected void move() {
        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        //移動先の座標の指定
        int nextX = ValueRange.rangeOf(x + dx[dir], 0, Map.COL);
        int nextY = ValueRange.rangeOf(y + dy[dir], 0, Map.ROW);

        //移動可能なときだけ
        if ((em.findFowordEntity(this) == null)
                && !(nextX == em.getPlayer().getX() && nextY == em.getPlayer().getY())) {
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
                moving = false;
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
        if (!em.getMap().isHit(x, y, this.dir)) {
            em.addEntity(new WhiteMagic(em, getX(), getY(), this.dir));
            MainPanel.wave.stop("EnemyMagic");
            MainPanel.wave.play("EnemyMagic");
        }
        freezeCount = 120;
        freezing = true;
        attacking = false;
    }
}
