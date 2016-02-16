package Entity;

import Entity.Magic.RedMagic;
import Entity.Magic.GreenMagic;
import Entity.Magic.BlueMagic;
import Client.Client;
import Entity.EntityType.EntityName;
import GameState.GameStateManager;
import Main.MainPanel;
import Map.Map;
import Map.MapManager;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import java.util.List;

/**
 *
 * @author takahito
 */
public class EntityManager {

    private Entity player;
    private ArrayList<Entity> entitys;
    private ArrayList<Entity> tmp;
    MapManager mm;
    Random rand;

    Client client;

    public EntityManager(MapManager mm) {

        this.mm = mm;
        entitys = new ArrayList<>();
        tmp = new ArrayList<>();
        player = new Player(this, 1, 1);
        rand = new Random();

    }

    public Player getPlayer() {
        return (Player) player;
    }

    public MapManager getMapManager() {
        return mm;
    }

    public Map getMap() {
        return mm.getMap();
    }

    public Entity getEntity(int index) {
        return entitys.get(index);
    }

    public List<Entity> getList() {
        return entitys;
    }

    public int getMaxId() {
        int max = 0;
        for (Entity e : entitys) {
            max = max > e.getId() ? max : e.getId();
        }
        return max;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client c) {
        this.client = c;
    }

    public void addEntity(Entity e) {
        tmp.add(e);
    }

    public void clear() {
        entitys.clear();
    }

    private void removeDeadEntity() {
        Iterator<Entity> i = entitys.iterator();
        while (i.hasNext()) {
            if (i.next().dead == true) {
                i.remove();
            }
        }
    }

    public void init() {

        String[] list = {
            "0100111000100000000",
            "0011000100000000100",
            "0000000000001110000",
            "0000000011010000010",
            "1000000000000001001"
        };
        //リストをクリアして先頭にプレイヤーを追加
        entitys.clear();
        entitys.add(player);

        if (mm.getCurrentFloor() == mm.getMaxFloor() - 1) {
            EntityType.summon(this, EntityName.DRUAGA);
        } else {
            EntityType.summon(this, list[mm.getCurrentFloor() % (list.length)]);
        }
        update();

        entitys.forEach(e -> {
            client.write("Client," + client.now() + "," + e.getStaticId() + "," + e.getId() + ", 0,summon,entity," + e.getX() + "," + e.getY());
        });

    }

    public void update() {

        removeDeadEntity();

        if (tmp.size() > 0) {
            tmp.forEach(e -> entitys.add(e));
            tmp.clear();
        }
        entitys.forEach(e -> e.update());

    }

    public void draw(Graphics2D g) {
        entitys.forEach(e -> e.draw(g));
    }

    public Entity findFoword(Entity me) {

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (Entity e : entitys) {
            if (e == me) {
                continue;
            }
            if (e instanceof RedMagic) {
                continue;
            }
            if (e instanceof GreenMagic) {
                continue;
            }
            if (e instanceof BlueMagic) {
                continue;
            }
            if (!mm.getMap().isHit(me.getX(), me.getY(), me.getDir())
                    && me.getX() + dx[me.getDir()] == e.getX()
                    && me.getY() + dy[me.getDir()] == e.getY()) {
                return e;
            }
        }
        return null;
    }

    public Entity findFowordEntity(Entity me) {

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (Entity e : entitys) {
            if (e == me) {
                continue;
            }
            if (me.getX() + dx[me.getDir()] == e.getX()
                    && me.getY() + dy[me.getDir()] == e.getY()) {
                return e;
            }
        }
        return null;
    }

    public boolean findFowordPlayer(Entity me) {

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        if (me.getX() + dx[me.getDir()] == player.getX()
                && me.getY() + dy[me.getDir()] == player.getY()) {
            return true;
        }

        return false;

    }

    public Entity find(int id) {
        for (Entity e : entitys) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public void end() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        MainPanel.gsm.setState(GameStateManager.ENDING_STATE);
        client.write("Client," + client.now() + ","
                + player.getStaticId() + "," + player.getId() + ","
                + mm.getCurrentFloor() + ",playEnd," 
                + ((Player) player).getTimeLeft() + "," + ((Player) player).getScore());

    }

}
