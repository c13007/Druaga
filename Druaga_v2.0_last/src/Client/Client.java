package Client;

import Entity.*;
import Item.ItemManager;
import Map.MapManager;
import Server.DataManager;
import java.io.DataOutputStream;
import java.net.*;

/**
 * Client class
 * サーバーとの通信を行う
 * @author c13007
 */
public class Client implements Runnable {
    
    /**
     * ホスト名　"localhost"を指定
     */
    String host;
    
    /**
     * ポート番号 10000番を指定
     */
    int port;
    
    /**
     * ソケットマネージャー
     * ソケットの監視を管理するクラス
     */
    SocketManager socketManager;
    
    /**
     * マップマネージャー　Client内でnewはしない
     * {@see #set(Map.MapManager, Entity.EntityManager, Item.ItemManager)}で必ず値をもらう
     * 今のところ使ってない
     * 
     */
    MapManager mm;
    
    /**
     * エンティティマネージャー Client内でnewはしない
     * {@see #set(Map.MapManager, Entity.EntityManager, Item.ItemManager)}で必ず値をもらう
     */
    EntityManager em;
    
    /**
     * アイテムマネージャー　Client内でnewはしない
     * {@see #set(Map.MapManager, Entity.EntityManager, Item.ItemManager)}で必ず値をもらう
     */
    ItemManager im;
    
    /**
     * データマネージャー
     * 送られてきたデータの配列の要素指定に利用するフィールドと
     * 静的IDから名前を検索するのに利用
     */
    DataManager dm = new DataManager();
    
    /**
     * クライアントのコンストラクタ
     * コンストラクタ呼び出しの後は必ずset()を呼ぶ
     * @param host  ホスト　　　デフォルト値 localhost
     * @param port  ポート番号　デフォルト値 10000
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    
    /**
     * マップマネージャー、エンティティマネージャー、アイテムマネージャの引き渡し<br>
     * Client内ではnewしないので必ず渡す
     * @param mm {@see #mm}
     * @param em {@see #em}
     * @param im {@see #im}
     */
    public void set(MapManager mm, EntityManager em, ItemManager im) {
        this.mm = mm;
        this.em = em;
        this.im = im;
        
        this.em.setClient(this);
        this.im.setClient(this);
    }
    
    /**
     * メインメソッド<br>
     * ソケットを生成後サーバーに接続<br>
     * 接続完了後にソケットマネージャの準備を行う
     */
    @Override
    public void run() {

        try {
            Socket socket = null;
            socket = new Socket();

            while (true) {
                socket.connect(new InetSocketAddress(host, port));
                if (socket.isConnected()) {
                    break;
                }
            }

            socketManager = new SocketManager(socket, this);
            socketManager.run();
            socketManager.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
        }
    }
    
    /**
     * サーバーから送られてきた文字列をもとにアップデート内容を解析
     * 文章の形式については{@see #write(java.lang.String)}を参照
     * 識別タグ
     * [summon] entity,x,y (x, y)の位置にエンティティを追加<br>
     *          item,x,y　　(x, y)の位置にアイテムを追加<br>
     * @param msg 受信したメッセージ -> {@see #write(java.lang.String)}
     */
    public void update(String msg) {
        
        if (msg == null) return;
        String[] data = msg.split(",");

        if (data.length < 5) return;

        switch (data[DataManager.TAG]) {
            case "summon":
                if (data[6].equals("entity")) {
                    EntityType.summon(em, EntityType.EntityName.values()[Integer.parseInt(data[DataManager.STATIC_ID])],
                            Integer.parseInt(data[7]), Integer.parseInt(data[8]));
                } else {
                    //アイテムを追加
                    System.out.println("[Client] summon item {name ITEMNAME position (" + data[7] + ", " + data[8] + ")}");
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * メッセージをソケットに書き込む データはカンマ(,)区切りで記述 形式は以下のとおり<br>
     * 1    : 文字列 "Client"<br>
     * 2    : クライアントの現在時刻 {@see #now()}<br>
     * 3    : 静的ID {@see Entity#staticId}, {@see Item#staticId}<br>
     * 4    : 動的ID {@see Entity#id}, {@see Item#id}<br>
     * 5    : タグ 詳細{@see Server.Server#write(java.lang.String)}<br>
     * 6以降 : タグごとに対応するデータ　詳細{@see @see Server.Server#write(java.lang.String)}<br>
     * @param msg データ形式(data1,data2,data3...)
     */
    public void write(String msg) {
        try {
            DataOutputStream out;
            out = socketManager.getOutputStream();

            if (msg.equals("stop")) {
                socketManager.stop();
            } else {
                out.writeBytes(msg + "\n");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * クライアントの現在時刻を返す
     * @return クライアントの現在時刻
     */
    public long now() {
        return System.currentTimeMillis();
    }
}
