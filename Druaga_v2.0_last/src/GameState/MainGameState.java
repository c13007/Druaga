package GameState;

import Client.Client;
import Entity.EntityManager;
import Item.*;
import Item.ItemManager;
import Main.MainPanel;
import Map.MapManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public class MainGameState extends GameState {

    MapManager mm = null;
    EntityManager em = null;
    ItemManager im = null;

    Random rand;
    Rectangle rect;
    public static int startPanel = 0;

    Client client;

    public MainGameState(GameStateManager gsm) {

        this.gsm = gsm;
        mm = new MapManager(6, false);
        em = new EntityManager(mm);
        im = new ItemManager(mm, em.getPlayer());

        rect = new Rectangle();
        rect.setRect(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);

        client = new Client("localhost", 10000);
        client.set(mm, em, im);
        Thread thread = new Thread(client);
        thread.start();

    }

    @Override
    public void init() {

        client.write("Client," + em.getClient().now() + "," + -1
                + "," + -1 + "," + em.getMapManager().getCurrentFloor()
                + ",playStart," + mm.getMaxFloor());

        if (em == null) {
            mm = new MapManager(6, false);
            em = new EntityManager(mm);
            im = new ItemManager(mm, em.getPlayer());
            client.set(mm, em, im);
        }

        rect = new Rectangle();
        rect.setRect(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);

        em.setClient(client);
        im.setClient(client);

        //敵の生成
        em.init();
        //アイテムの生成
        im.init();

        mm.musicflg = 1;

    }

    @Override
    public void update() {
        em.update();
        im.update();

        Item item = im.find(em.getPlayer());
        if (item != null) {
            item.effect();
        }

    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.fill(rect);

        if (startPanel == 0) {

            Font font = new Font("Century", Font.PLAIN, 10);

            g.setFont(font);
            g.setColor(Color.GREEN);
            g.drawString("    GET   READY", 160, 170);
            g.drawString("  PLAYER   ONE", 160, 190);
            g.drawString("  FLOOR", 180, 210);
            g.setColor(Color.YELLOW);
            g.drawString(String.valueOf(mm.getCurrentFloor() + 1), 200, 230);
            startPanel = 1;

        } else if (startPanel == 1) {

            MainPanel.wave.stop("Main");
            MainPanel.wave.stop("Main2");
            MainPanel.wave.stop("Boss");

            MainPanel.wave.play("Start");
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
            }
            MainPanel.wave.stop("Start");

            if (MapManager.musicflg == 0) {
                MainPanel.wave.loopPlay("Main", 100);
                MapManager.musicflg = 1;
            } else if (MapManager.musicflg == 1) {
                MainPanel.wave.loopPlay("Main2", 100);
                MapManager.musicflg = 0;
            } else if (MapManager.musicflg == 2) {
                MainPanel.wave.loopPlay("Boss", 100);
                MapManager.musicflg = 0;
            }

            startPanel = 2;

        } else if (startPanel == 2) {
            mm.draw(g);
            im.draw(g);
            em.draw(g);
        }
    }

    @Override
    public void keyPressed(int k) {
        em.getPlayer().keyPressed(k);
    }

    @Override
    public void keyReleased(int k) {
        em.getPlayer().keyReleased(k);
    }

}
