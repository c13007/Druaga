package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import Main.MainPanel;
import System.ValueRange;

public class Option extends GameState {

    private Image wrench;
    private Image ret;
    private int currentChoice;
    private int currentChoiceMenu;

    private boolean soundOnOff;

    private Rectangle rect;

    private String[] options = {
        "Game Mode",
        "Item Amount",
        "Sound",
        "BGM",
        "SE",
        "Return",};

    private String[][] optionMenu = {
        {"Easy", "Normal", "Hard"},
        {"Few", "Usually", "Many"},
        {"On", "Off"},
        {"Volume"}, {"Volume"},
        null,};

    public static int[][] optionTable = {
        {0, 1, 0},
        {0, 1, 0},
        {1, 0},
        {0, 0, 1, 0, 0},
        {0, 0, 1, 0, 0},
        {0},};

    private int[] interval = {20, 60, 110, 150};

    private Font font;

    public Option(GameStateManager gsm) {

        this.gsm = gsm;

        ImageIcon icon = new ImageIcon(getClass().getResource("/images/system/wrench.png"));
        wrench = icon.getImage();
        icon = new ImageIcon(getClass().getResource("/images/system/return.png"));
        ret = icon.getImage();

        rect = new Rectangle();
        rect.setRect(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);

        try {
            font = new Font(
                    "Century",
                    Font.PLAIN,
                    10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();

    }
    
    public static int getGameMode(){
        for(int i = 0; i < optionTable[0].length; i++)
            if(optionTable[0][i] == 1)
                return i;
        return 0;
    }
    
    public static int getItemMount(){
        for(int i = 0; i < optionTable[1].length; i++)
            if(optionTable[1][i] == 1)
                return i;
        return 0;
    }

    public void init() {
        currentChoice = 0;
        for (int i = 0; i < optionTable[0].length; i++) {
            if (optionTable[0][i] == 1) {
                currentChoiceMenu = i;
                break;
            }
        }
        soundOnOff = optionTable[2][0] == 1 ? true : false;
    }

    public void update() {
    }

    public void draw(Graphics2D g) {

        g.setColor(Color.BLACK);
        g.fill(rect);

        int w = 0;
        g.setFont(font);

        g.setColor(Color.WHITE);
        g.drawString("Option", 60, 80);
        g.drawImage(wrench, 43, 68, null);

        for (int i = 0; i < options.length - 1; i++) {

            g.setColor(i == currentChoice ? Color.RED : Color.WHITE);
            if (options[i].equals("BGM") || options[i].equals("SE")) {
                if (!soundOnOff) {
                    g.setColor(Color.GRAY);
                }
                g.drawString(options[i], 60, 110 + i * 18);
            } else {
                g.drawString(options[i], 50, 110 + i * 18);
            }

            if (optionMenu[i] != null) {
                for (int j = 0; j < optionMenu[i].length; j++) {
                    if (i == 3 || i == 4) {
                        for (int l = 0; l < optionTable[i].length; l++) {
                            g.fillRect(172 + (l * 4), 111 + w - 7, 2, 5);
                            if (optionTable[i][l] == 1) {
                                break;
                            }
                        }
                    } else {
                        if (i == currentChoice) {
                            g.setColor(j == currentChoiceMenu ? Color.RED : Color.WHITE);
                        }
                        if (optionTable[i][j] != 0 && i != currentChoice) {
                            g.setColor(Color.GREEN);
                        }
                        g.drawString(optionMenu[i][j], 150 + interval[j], 111 + w);
                        g.setColor(Color.WHITE);
                    }
                }
            }
            w = (i + 1) * 18;

        }
        g.setColor(options.length - 1 == currentChoice ? Color.RED : Color.WHITE);
        g.drawString(options[options.length - 1], 50, 115 + (options.length - 1) * 18);
        g.drawImage(ret, 88, 102 + (options.length - 1) * 18, null);

    }

    public void select() {

        if (currentChoice == 5) {
            currentChoice = 0;
            currentChoiceMenu = 0;

            gsm.setState(GameStateManager.MENU_STATE);
        }

    }

    public void setOption() {
        for (int i = 0; i < optionTable[currentChoice].length; i++) {
            optionTable[currentChoice][i] = 0;
        }
        optionTable[currentChoice][currentChoiceMenu] = 1;
        soundOnOff = optionTable[2][0] == 1 ? true : false;

    }

    public int setOption(int k) {

        for (int i = 0; i < optionTable[currentChoice].length; i++) {
            optionTable[currentChoice][i] = 0;
        }
        optionTable[currentChoice][currentChoiceMenu] = 1;

        /*for (int i = 0; i < optionTable.length; i++) {
            for (int j = 0; j < optionTable[i].length; j++) {
                System.out.print(optionTable[i][j] + " ");
            }
            System.out.println();
        }*/

        soundOnOff = optionTable[2][0] == 1 ? true : false;
        int iv = !soundOnOff && (currentChoice == 2 || currentChoice == 5) ? 3 : 1;

        if (k == KeyEvent.VK_UP) {
            if (currentChoice - iv < 0) {
                return 0;
            }
            for (int i = 0; i < optionTable[currentChoice - iv].length; i++) {
                if (optionTable[currentChoice - iv][i] == 1) {
                    return ValueRange.rangeOf(i, 0, optionTable[currentChoice - iv].length);
                }
            }
        } else if (k == KeyEvent.VK_DOWN) {
            if (currentChoice + iv > optionTable.length - 1) {
                return 0;
            }
            for (int i = 0; i < optionTable[currentChoice + iv].length; i++) {
                if (optionTable[currentChoice + iv][i] == 1) {
                    return ValueRange.rangeOf(i, 0, optionTable[currentChoice + iv].length);
                }
            }
        }

        return 0;

    }

    public void keyPressed(int k) {

        if (k == KeyEvent.VK_ENTER) {
            MainPanel.wave.stop("KReturn");
            MainPanel.wave.play("KReturn");
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoiceMenu = setOption(KeyEvent.VK_UP);
            if (!soundOnOff && currentChoice == 5) {
                currentChoice -= 3;
            } else {
                currentChoice--;
            }
            if (currentChoice < 0) {
                currentChoice = options.length - 1;
            }

            MainPanel.wave.stop("KPush");
            MainPanel.wave.play("KPush");
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoiceMenu = setOption(KeyEvent.VK_DOWN);
            if (!soundOnOff && currentChoice == 2) {
                currentChoice += 3;
            } else {
                currentChoice++;
            }
            if (currentChoice > options.length - 1) {
                currentChoice = 0;
            }

            MainPanel.wave.stop("KPush");
            MainPanel.wave.play("KPush");
        }
        if (k == KeyEvent.VK_LEFT) {
            if (currentChoice == 3 || currentChoice == 4) {
                currentChoiceMenu--;
                if (currentChoiceMenu == -1) {
                    currentChoiceMenu = 0;
                }
            } else if (currentChoice != 5) {
                currentChoiceMenu--;
                if (currentChoiceMenu == -1) {
                    currentChoiceMenu = optionMenu[currentChoice].length - 1;
                }
            }
            setOption();
            MainPanel.wave.stop("KPush");
            MainPanel.wave.play("KPush");
        }
        if (k == KeyEvent.VK_RIGHT) {
            if (currentChoice == 3 || currentChoice == 4) {
                currentChoiceMenu++;
                if (currentChoiceMenu == optionTable[currentChoice].length) {
                    currentChoiceMenu = optionTable[currentChoice].length - 1;
                }
            } else if (currentChoice != 5) {
                currentChoiceMenu++;
                if (currentChoiceMenu == optionMenu[currentChoice].length) {
                    currentChoiceMenu = 0;
                }
            }
            setOption();
            MainPanel.wave.stop("KPush");
            MainPanel.wave.play("KPush");
        }

    }

    public void keyReleased(int k) {
    }
}
