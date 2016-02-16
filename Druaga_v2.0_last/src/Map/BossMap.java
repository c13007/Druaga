package Map;

import java.awt.Graphics;

/**
 *
 * @author takahito
 */
public class BossMap extends Map{

    public BossMap(boolean useMapFile) {
        super(useMapFile);
    }
    
    @Override
    public boolean isHit(int x, int y, int dir){
        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        try {   
            //行先が立てない場所のとき
            if (!map_mask[y + dy[dir]][x + dx[dir]]) {
                return true;
            }            
        }catch(ArrayIndexOutOfBoundsException e){
            return true;
        }
        return false;
    }
    
    @Override
    public void init(){
         //マスクとイメージマップの初期化
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if (r == 0 || r == ROW - 1 || c == 0 || c == COL - 1) {
                    map_image[r][c] = WALL;
                    map_mask[r][c] = false;
                } else {
                    map_image[r][c] = FLOOR;
                    map_mask[r][c] = true;
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics g){
        //背景の描画
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                g.drawImage(
                        blockImage[map_image[r][c]],
                        CELL_SIZE * c, CELL_SIZE * r,
                        this);
            }
        }
    }
}
