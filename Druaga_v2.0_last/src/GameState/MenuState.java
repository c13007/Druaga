package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import Main.MainPanel;

public class MenuState extends GameState {

    private Image background;
    private int currentChoice;

    private String[] options = {
        "   - PLAY -",
        "- OPTION -",
        "   - QUIT -"
    };

    private Font font;

    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/system/title.png"));
            background = icon.getImage();

            font = new Font(
                    "Century",
                    Font.PLAIN,
                    16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();

    }

    public void init() {
        MainPanel.wave.play("Opening");
    }

    public void update() {
    }

    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);

        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == currentChoice ? Color.RED : Color.WHITE);
            g.drawString(options[i], 165, 240 + i * 18);
        }
    }

    public void select() {
        if (currentChoice == 0) {
            gsm.setState(GameStateManager.MAIN_GAME_STATE);
        }
        if (currentChoice == 1) {
            gsm.setState(GameStateManager.OPTION);
        }
        if (currentChoice == 2) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            System.exit(0);
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            MainPanel.wave.stop("KReturn");
            MainPanel.wave.play("KReturn");
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length - 1;
            }
            MainPanel.wave.stop("KPush");
            MainPanel.wave.play("KPush");
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
            MainPanel.wave.stop("KPush");
            MainPanel.wave.play("KPush");
        }
    }

    public void keyReleased(int k) {
    }
}
