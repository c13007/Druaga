package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import Main.MainPanel;

public class GameOverState extends GameState {

    private Image ret;
    Rectangle rect;

    private Font font;

    public GameOverState(GameStateManager gsm) {

        this.gsm = gsm;

        try {

            ImageIcon icon = new ImageIcon(getClass().getResource("/images/system/return.png"));
            ret = icon.getImage();

            font = new Font(
                    "Century",
                    Font.PLAIN,
                    12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        rect = new Rectangle();
        rect.setRect(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);
    }

    public void init() {
        MainPanel.wave.stop("GameOver");
        MainPanel.wave.play("GameOver");
    }

    public void update() {
    }

    public void draw(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.fill(rect);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("Game    Over", 170, 110);

        g.drawString("Push Enter MainMenu", 130, 260);
        g.drawImage(ret, 270, 246, null);

    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            MainPanel.wave.stop("KReturn");
            MainPanel.wave.play("KReturn");
            MainPanel.wave.stop("Main");
            MainPanel.wave.stop("Main2");
            MainPanel.wave.stop("Boss");
            gsm.setState(GameStateManager.MENU_STATE);
            MainGameState.startPanel = 0;
        }
    }

    public void keyReleased(int k) {
    }
}
