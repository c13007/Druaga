package Entity;

import Main.MainPanel;
import Map.Map;
import System.ValueRange;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * プレイヤーと敵のベースに使用するクラス
 * @author c13007
 */
public abstract class Entity {
    
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    
    public static final int SPEED = 1;
    
    public EntityManager em;
    
    private int staticId;
    private int id;
    
    protected int x;
    protected int y;
    protected int px;
    protected int py;
    
    protected int dir;
    
    protected int lifePoint;
    protected int maxLifePoint;
    protected int attackPoint;
    protected int defensePoint;
    
    protected Rectangle back;
    protected Rectangle life;
    
    protected Image image;
    protected int frame;
    protected int frameMax;
    
    protected int moveLength;
    protected int freezeCount;
    protected boolean showLifePoint;
    
    protected boolean moving;
    protected boolean attacking;
    protected boolean freezing;
    protected boolean dead;
    
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getDir(){ return dir; }
    public int getStaticId(){ return staticId; }
    public int getId(){ return id; }
    
    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
        
        this.px = x * Map.CELL_SIZE;
        this.py = y * Map.CELL_SIZE;
    }
    
    public void setDir(int dir){
        this.dir = dir;
    }
    
    private void setId(){
        id = em.getMaxId() + 1;
    }
    
    public abstract void init();
    public abstract void update();
    protected abstract void attack();
    
    
    
    public Entity(EntityManager em, int staticId, int x, int y){
        this.em = em;
        this.staticId = staticId;
        setPos(x, y);
        setId();
        
        moving = false;
        attacking = false;
        freezing = false;
        dead = false;
        showLifePoint = true;
        
        back = new Rectangle();
        life = new Rectangle();
        
        init();        
    }
    
    public void draw(java.awt.Graphics2D g){
        g.drawImage(image,
                px, py, 
                px + MainPanel.CELL_SIZE, py + MainPanel.CELL_SIZE,
                dir * MainPanel.CELL_SIZE, frame * MainPanel.CELL_SIZE,
                dir * MainPanel.CELL_SIZE + MainPanel.CELL_SIZE, frame * MainPanel.CELL_SIZE + MainPanel.CELL_SIZE,
                null);
        
        
        if(showLifePoint){
            back.setRect(px - 1, py - 3, 18, 4);
            g.setColor(Color.BLACK);
            g.fill(back);

            int length = ValueRange.rangeOf((int)(16 * (lifePoint / (double)maxLifePoint)), 0, maxLifePoint);
            life.setRect(px, py - 2, length, 2);
            g.setColor(length <= 4 ? Color.RED : Color.GREEN);
            g.fill(life);
        }
    }
    
    protected void move(){
        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        //移動先の座標の指定
        int nextX = ValueRange.rangeOf(x + dx[dir], 0, Map.COL);
        int nextY = ValueRange.rangeOf(y + dy[dir], 0, Map.ROW);

        //移動可能なときだけ
        if (!em.getMap().isHit(x, y, dir) && em.findFoword(this) == null) {
            //描画座標の更新
            px = ValueRange.rangeOf(px + SPEED * dx[dir], 0, MainPanel.WIDTH - MainPanel.CELL_SIZE);
            py = ValueRange.rangeOf(py + SPEED * dy[dir], 0, MainPanel.HEIGHT - MainPanel.CELL_SIZE);

            //移動量の更新
            moveLength += SPEED;
            frame++;
            if(frame > frameMax) frame = 0;

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
    
    public void addLifePoint(int point){
        lifePoint = ValueRange.rangeOf(lifePoint + point, 0, maxLifePoint);
        
        MainPanel.wave.stop("Heal");
        MainPanel.wave.play("Heal");
    }
    
    public boolean breakWall(){
        return em.getMap().breakWall(x, y, dir);
    }
    
    public void startMove(){
        moving = true;
        em.getClient().write("Client," + em.getClient().now()+ "," + staticId 
                + "," + id + "," + em.getMapManager().getCurrentFloor()
                + ",moveStart," + x + "," + y + "," + dir);
    }
    
    public void moveEnd(){
        moving = false;
        em.getClient().write("Client," + em.getClient().now()+ "," + staticId 
                + "," + id + "," + em.getMapManager().getCurrentFloor()
                + ",moveEnd," + x + "," + y + "," + dir);
    }
    
    protected void attackMessage(){
        em.getClient().write("Client," + em.getClient().now()+ "," + staticId 
                + "," + id + "," + em.getMapManager().getCurrentFloor()
                + ",attack," + x + "," + y + "," + attackPoint);
    }
    
    public void damage(int attackPoint){
        int damagePoint = ValueRange.rangeOf(attackPoint - defensePoint, 10, attackPoint);
        lifePoint -= damagePoint;
        em.getClient().write("Client," + em.getClient().now()+ "," + staticId 
                + "," + id + "," + em.getMapManager().getCurrentFloor()
                + ",damage," + x + "," + y + "," + damagePoint);
        if(lifePoint <= 0)
            dead();
    }
    public void dead(){
        dead = true;
        em.getPlayer().addScore(maxLifePoint);
        em.getClient().write("Client," + em.getClient().now()+ "," + staticId 
                + "," + id + "," + em.getMapManager().getCurrentFloor()
                + ",dead");
    }
    
}
