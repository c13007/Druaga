package GameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {
    
    private ArrayList<GameState> gameState;
    private int currentState;

    public static final int MENU_STATE = 0;
    public static final int MAIN_GAME_STATE = 1;
    public static final int OPTION = 2;
    public static final int GAMEOVER_STATE = 3;
    public static final int ENDING_STATE = 4;
    
    

    public GameStateManager() {
        gameState = new ArrayList<>();

        currentState = 0;
        gameState.add(new MenuState(this));
        gameState.add(new MainGameState(this));
        gameState.add(new Option(this));
        gameState.add(new GameOverState(this));
        gameState.add(new EndingState(this));
        
    }

    public void setState(int state) {
        currentState = state;
        gameState.get(currentState).init();
    }

    public void update() {
        gameState.get(currentState).update();
    }

    public void draw(Graphics2D g) {
        gameState.get(currentState).draw(g);

    }

    public void KeyPressed(int k) {
        gameState.get(currentState).keyPressed(k);
    }

    public void keyReleased(int k) {
        gameState.get(currentState).keyReleased(k);
    }
    
}
