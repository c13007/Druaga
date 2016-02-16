package Map;


import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Map extends JPanel {

    //ブロックID
    public static final int FLOOR = 0;
    public static final int WALL = 1;
    public static final int WALL_CENTER = 2;
    public static final int WALL_ROD_VE = 3;
    public static final int WALL_ROD_HO = 4;
    
    //アイテムID
    public static final int STAIRS = 0;

    //マップのサイズ
    public static final int ROW = 25;
    public static final int COL = 25;

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    //背景画像の情報用
    public int[][] map_image;
    //迷路部分用
    public int[][] map_wall;
    //マスク用（歩けるところの判定）
    public boolean[][] map_mask;

    public static final int CELL_SIZE = 16;
    private static final String BLOCK_DIR = "/images/block/";
    
    public Image[] blockImage;
    private String[] blockFilepass = {
        "floor.png",
        "wall.png",
        "wall_center.png",
        "wall_rod_ve.png",
        "wall_rod_ho.png"
    };

    public static Random rand = new Random();

    public Map(boolean useMapFile) {
    	
        //画像の読み込み
        blockImage = new Image[blockFilepass.length];
        for (int i = 0; i < blockFilepass.length; i++) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(BLOCK_DIR + blockFilepass[i]));
                blockImage[i] = icon.getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map_image = new int[ROW][COL];
        map_wall = new int[ROW - 1][COL - 1];
        map_mask = new boolean[ROW][COL];

        if (useMapFile) {
            inport("map1.map");
        } else {
            init();
        }
    }

    public boolean isHit(int x, int y, int dir) {

        //移動先に使う座標
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        
        int[] drx = {-1, 0, 0, -1};
        int[] dry = {-1, -1, 0, 0};
        int[] dlx = {0, 0, -1, -1};
        int[] dly = {-1, 0, 0, -1};
        
        try {   
            //行先が立てない場所のとき
            if (!map_mask[y + dy[dir]][x + dx[dir]]) {
                return true;
            }
            if(     //進行方向右側の壁がこっちに倒れている
                    map_wall[y + dry[dir]][x + drx[dir]] == (RIGHT + dir)% 4 ||
                    //進行方向左側の壁がこっちに倒れている
                    map_wall[y + dly[dir]][x + dlx[dir]] == (LEFT + dir)% 4){
                return true;
            }
            
        }catch(ArrayIndexOutOfBoundsException e){
            return true;
        }

        return false;
    }
    
    public boolean breakWall(int x, int y, int dir){
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        
        int[] drx = {-1, 0, 0, -1};
        int[] dry = {-1, -1, 0, 0};
        int[] dlx = {0, 0, -1, -1};
        int[] dly = {-1, 0, 0, -1};
        
        if(map_wall[y + dry[dir]][x + drx[dir]] == (RIGHT + dir)% 4){
            map_wall[y + dry[dir]][x + drx[dir]] = -1;
            return true;
        }
        if(map_wall[y + dly[dir]][x + dlx[dir]] == (LEFT + dir)% 4){
            map_wall[y + dly[dir]][x + dlx[dir]] = -1;
            return true;
        }
        
        return false;
    }
    

    public void init() {

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

        //迷路の生成
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        for (int r = 0; r < ROW - 1; r++) {
            for (int c = 0; c < COL - 1; c++) {
                map_wall[r][c] = -1;
            }
        }

        for (int r = 0; r < ROW - 1; r++) {
            for (int c = 0; c < COL - 1; c++) {
                //周りの部分の柱は外側に向ける
                if (r == 0) {
                    map_wall[r][c] = UP;
                } else if (r == ROW - 2) {
                    map_wall[r][c] = DOWN;
                } else if (c == 0) {
                    map_wall[r][c] = LEFT;
                } else if (c == COL - 2) {
                    map_wall[r][c] = RIGHT;
                } else {
                    int n;
                    do {
                        //１段目のときは全方向からそれ以外のときは上以外から選ぶ
                        n = rand.nextInt(r == 1 ? 4 : 3) + (r == 1 ? 0 : 1);
                    } while ( //倒す先の棒がこっちを向いていたら倒す先の選び直し
                            map_wall[r + dr[n]][c + dc[n]] != -1
                            && map_wall[r + dr[n]][c + dc[n]] == (n + 2) % 4);
                    map_wall[r][c] = n;
                }
            }
        }
        //export("map1.map");
    }

    public void draw(Graphics g) {

        //背景の描画
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                g.drawImage(
                        blockImage[map_image[r][c]],
                        CELL_SIZE * c, CELL_SIZE * r,
                        this);
            }
        }

        //迷路の描画
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};
        for (int r = 1; r < ROW - 2; r++) {
            for (int c = 1; c < COL - 2; c++) {
                if(map_wall[r][c] != -1){
                    int i = (map_wall[r][c] == UP || map_wall[r][c] == DOWN) ? WALL_ROD_VE : WALL_ROD_HO;
                    g.drawImage(
                            blockImage[i],
                            CELL_SIZE / 2 + CELL_SIZE * c + CELL_SIZE * dx[map_wall[r][c]] / 2,
                            CELL_SIZE / 2 + CELL_SIZE * r + CELL_SIZE * dy[map_wall[r][c]] / 2,
                            this);
                }
                

            }
        }
        //真ん中の点
        for (int r = 1; r < ROW - 2; r++) {
            for (int c = 1; c < COL - 2; c++) {

                g.drawImage(
                        blockImage[WALL_CENTER],
                        CELL_SIZE / 2 + CELL_SIZE * c,
                        CELL_SIZE / 2 + CELL_SIZE * r,
                        this);
            }
        }
    }

    private void export(String s) {
        try {
            File file = new File(s);
            //古いファイルは消す
            if (file.exists()) {
                file.delete();
            }
            //ファイルの新規作成
            file.createNewFile();
            if (checkBeforeWritefile(file)) {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

                /*
                 書き込む順序
                 ・ブロック情報
                 ・迷路情報
                 ・マスク情報
                 ・アイテム情報
                 数値はカンマで区切ってtrueは1falseは0で出力する
                 */
                for (int r = 0; r < ROW; r++) {
                    for (int c = 0; c < COL; c++) {
                        pw.print(map_image[r][c] + ",");
                    }
                    pw.println();
                }

                for (int r = 0; r < ROW - 1; r++) {
                    for (int c = 0; c < COL - 1; c++) {
                        pw.print(map_wall[r][c] + ",");
                    }
                    pw.println();
                }
                for (int r = 0; r < ROW; r++) {
                    for (int c = 0; c < COL; c++) {
                        pw.print((map_mask[r][c] ? "1" : "0") + ",");
                    }
                    pw.println();
                }

                pw.close();

            } else {
                System.out.println("書き込めません");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inport(String s) {
        String str;
        String[] buf;
        try {
            File file = new File(s);
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int r = 0; r < ROW; r++) {
                str = br.readLine();
                buf = str.split(",", 0);
                for (int c = 0; c < COL; c++) {
                    map_image[r][c] = Integer.parseInt(buf[c]);
                }
            }

            for (int r = 0; r < ROW - 1; r++) {
                str = br.readLine();
                buf = str.split(",", 0);
                for (int c = 0; c < COL - 1; c++) {
                    map_wall[r][c] = Integer.parseInt(buf[c]);
                }
            }
            for (int r = 0; r < ROW; r++) {
                str = br.readLine();
                buf = str.split(",", 0);
                for (int c = 0; c < COL; c++) {
                    map_mask[r][c] = buf[c].equals("1");
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkBeforeWritefile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        }

        return false;
    }

}
