package Main;

import GameState.GameStateManager;
import System.WaveEngine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable, KeyListener{
    
    public static final int WIDTH = 400;
    public static final int HEIGHT = 416;
    public static final int CELL_SIZE = 16;
    public static final int SCALE = 2;
    
    private int FPS = 60;
    private long targetTime = 1000 / FPS;
    
    private Thread thread;
    private Boolean running;
    
    public static GameStateManager gsm;
    
    private BufferedImage image;
    private Graphics2D g;
    
    public static WaveEngine wave;
    
    
    public MainPanel(){
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
    }
    
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }
    

    @Override
    public void run(){
        init();
        
        long start;
        long elapsed;
        long wait;
        
        while(running){
            start = System.nanoTime();
            
            update();
            draw();
            drawToScreen();
            
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            if(wait < 0) wait = 5;
            
            try{
                Thread.sleep(wait);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private void init(){
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D)image.getGraphics();
        
        wave = new WaveEngine(256);
        loadSound();
        
        running = true;
        
        gsm = new GameStateManager();
    }
    
    private void update(){
        gsm.update();
    }
    
    private void draw(){
        gsm.draw(g);
    }

    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(
                image,
                0, 0,
                WIDTH * SCALE, HEIGHT * SCALE, 
                null);
        g2.dispose();
    }
    
    private void loadSound(){
        
        wave.load("Boss", "/sounds/DruagaBossSound.wav");
        wave.load("Ending", "/sounds/DruagaEndingSound.wav");
        wave.load("GameOver", "/sounds/DruagaGameOver.wav");
        wave.load("Main", "/sounds/DruagaMainSound.wav");
        wave.load("Main2", "/sounds/DruagaMainSound2.wav");
        wave.load("Opening", "/sounds/DruagaOpeningSound.wav");
        wave.load("Start", "/sounds/DruagaStartSound.wav");
        wave.load("EnemyBattle", "/sounds/EnemyBattle.wav");
        wave.load("EnemyMagic", "/sounds/EnemyMagic.wav");
        wave.load("FloorClear1", "/sounds/FloorClearSound.wav");
        wave.load("FloorClear2", "/sounds/FloorClearSound2.wav");
        wave.load("Clear", "/sounds/GoalSound.wav");
        wave.load("PlayerBattle", "/sounds/PlayerBattle.wav");
        wave.load("PlayerGetItem", "/sounds/PlayerGetItem.wav");
        wave.load("Heal", "/sounds/PlayerHeal.wav");
        wave.load("PlayerStep", "/sounds/PlayerStep.wav");
        wave.load("KPush", "/sounds/KPush.wav");
        wave.load("KReturn", "/sounds/KReturn.wav");
        
    }
    
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyPressed(KeyEvent e){
        gsm.KeyPressed(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e){
        gsm.keyReleased(e.getKeyCode());
    }
    
}
