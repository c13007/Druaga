/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Map.Map;
import System.ValueRange;
import java.util.Random;

import Entity.Enemy.*;

/**
 *
 * @author takahito
 */
public class EntityType {

    public static Random rand = new Random();

    public static enum EntityName {

        RANDOM,
        BLACKSLIME,
        BLUEORB,
        BLUEROPER,
        BLUESLIME,
        DARKGREENSLIME,
        DARKYELLOWSLIME,
        DRUID,
        DRUIDGHOST,
        GREENROPER,
        GREENSLIME,
        MAGE,
        MAGEGHOST,
        REDORB,
        REDROPER,
        REDSLIME,
        SKELETON,
        SORCERER,
        WIZARD,
        WIZARDGHOST,
        DRUAGA,
        ENTITY_NAME_NAX,
    };

    public static void summon(EntityManager em, EntityName name, int tx, int ty) {

        Entity e;

        int x = ValueRange.rangeOf(tx, 1, Map.COL - 2);
        int y = ValueRange.rangeOf(tx, 1, Map.ROW - 2);

        switch (name) {
        	case BLACKSLIME:
        		e = new GreenSlime(em, x, y);
        		break;
            case GREENSLIME:
                e = new GreenSlime(em, x, y);
                break;
            case REDSLIME:
                e = new RedSlime(em, x, y);
                break;
            case BLUESLIME:
                e = new BlueSlime(em, x, y);
                break;
            case DARKGREENSLIME:
                e = new DarkGreenSlime(em, x, y);
                break;
            case DARKYELLOWSLIME:
                e = new DarkYellowSlime(em, x, y);
                break;
            case BLUEORB:
                e = new BlueOrb(em, x, y);
                break;
            case REDORB:
                e = new RedOrb(em, x, y);
                break;
            case MAGE:
                e = new Mage(em, x, y);
                break;
            case DRUID:
                e = new Druid(em, x, y);
                break;
            case SORCERER:
                e = new Sorcerer(em, x, y);
                break;
            case WIZARD:
                e = new Wizard(em, x, y);
                break;
            case MAGEGHOST:
                e = new MageGhost(em, x, y);
                break;
            case DRUIDGHOST:
                e = new DruidGhost(em, x, y);
                break;
            case WIZARDGHOST:
                e = new WizardGhost(em, x, y);
                break;
            case SKELETON:
                e = new Skeleton(em, x, y);
                break;
            case REDROPER:
                e = new RedRoper(em, x, y);
                break;
            case BLUEROPER:
                e = new BlueRoper(em, x, y);
                break;
            case GREENROPER:
                e = new GreenRoper(em, x, y);
                break;
            case DRUAGA:
                e = new Druaga(em, x, y);
                break;
            default:
                e = new GreenSlime(em, x, y);
                break;
        }
        em.addEntity(e);
    }

    public static void summon(EntityManager em, EntityName name) {

        int x = rand.nextInt(Map.COL - 3) + 1;
        int y = rand.nextInt(Map.ROW - 3) + 1;

        if (name == EntityName.RANDOM) {
            int eName = rand.nextInt(EntityName.ENTITY_NAME_NAX.ordinal() - 1) + 1;
            summon(em, EntityName.values()[eName], x, y);
        }
        summon(em, name, x, y);
    }

    public static void summon(EntityManager em, String summonList) {
        while (summonList.length() < EntityName.values().length) {
            summonList += "0";
        }

        for (int i = 0; i < EntityName.values().length - 1; i++) {
            int count = Integer.parseInt(summonList.charAt(i) + "", 16);
            if (0 < count && count < 16) {
                for (int j = 0; j < count; j++) {
                    summon(em, EntityName.values()[i + 1]);
                }
            }
        }
    }

    public static void summon(EntityManager em) {
        summon(em, EntityName.RANDOM);
    }

    public static EntityName findEntityNameByStaticId(int static_id) {
        //あとで考える
        return EntityName.values()[static_id];
    }
    
    //
    public static Entity create(EntityManager em, EntityName name, int tx, int ty) {

        Entity e;

        int x = ValueRange.rangeOf(tx, 1, Map.COL - 2);
        int y = ValueRange.rangeOf(tx, 1, Map.ROW - 2);

        switch (name) {
        	case BLACKSLIME:
        		e = new GreenSlime(em, x, y);
        		break;
            case GREENSLIME:
                e = new GreenSlime(em, x, y);
                break;
            case REDSLIME:
                e = new RedSlime(em, x, y);
                break;
            case BLUESLIME:
                e = new BlueSlime(em, x, y);
                break;
            case DARKGREENSLIME:
                e = new DarkGreenSlime(em, x, y);
                break;
            case DARKYELLOWSLIME:
                e = new DarkYellowSlime(em, x, y);
                break;
            case BLUEORB:
                e = new BlueOrb(em, x, y);
                break;
            case REDORB:
                e = new RedOrb(em, x, y);
                break;
            case MAGE:
                e = new Mage(em, x, y);
                break;
            case DRUID:
                e = new Druid(em, x, y);
                break;
            case SORCERER:
                e = new Sorcerer(em, x, y);
                break;
            case WIZARD:
                e = new Wizard(em, x, y);
                break;
            case MAGEGHOST:
                e = new MageGhost(em, x, y);
                break;
            case DRUIDGHOST:
                e = new DruidGhost(em, x, y);
                break;
            case WIZARDGHOST:
                e = new WizardGhost(em, x, y);
                break;
            case SKELETON:
                e = new Skeleton(em, x, y);
                break;
            case REDROPER:
                e = new RedRoper(em, x, y);
                break;
            case BLUEROPER:
                e = new BlueRoper(em, x, y);
                break;
            case GREENROPER:
                e = new GreenRoper(em, x, y);
                break;
            case DRUAGA:
                e = new Druaga(em, x, y);
                break;
            default:
                e = new GreenSlime(em, x, y);
                break;
        }
        return e;
    }

}
