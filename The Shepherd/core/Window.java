package core;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.*;
import java.util.HashSet;
import entities.*;
import assets.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class Window extends JFrame {
    private String name;
    private Canvas c;
    private boolean fullscreen;
    private final int gameSize = 256;
    private static Text preloadText = new Text();
    private static Text T_001 = new Text(0.5f,0.0f,0.0f);
    private static Text T_002 = new Text(1.0f,0.0f,0.0f);
    private static Text T_003 = new Text(0.5f,0.5f,0.5f);
    public static Text T_004 = new Text(1.0f,1.0f,0.0f);
    public static Text T_005 = new Text(0.0f,1.0f,0.0f);
    
    public Window(String name) {
        setSize(512, 512);
        this.name = name;
        setTitle(name);
        setResizable(false);
        fullscreen = false;
        
        setUndecorated(true);
        
        //Object testing = Window.class.getResourceAsStream("/assets/icon.png");
        //System.out.println(testing);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/icon.png"));
        Image windowIcon = icon.getImage();
        setIconImage(windowIcon);

        c = new Canvas() {
            public void paintComponent(Graphics g) {};  
        };
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) Math.floor((dim.width - getSize().width) / 2), (int) Math.floor((dim.height - getSize().height) / 2));

        c.setBackground(Color.black);
        add(c);
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void render(HashSet<Entity> E, ArrayList<ArrayList<Button>> B) {
        if (!isDisplayable()) return;
        BufferedImage buff = new BufferedImage(getSize().width, getSize().height, 1);
        Graphics2D g = buff.createGraphics();
        
        //Graphics g = c.getGraphics();
        
        g.translate(Math.floor(getSize().width / 2), Math.floor(getSize().height / 2));
        double s;
        if (getSize().width < getSize().height) {
            s = getSize().width;
        } else s = getSize().height;
        double s1 = s;
        s /= gameSize;
        g.scale(s,s);
        //g.fillRect((int)((double)gameSize * -0.5), (int)((double)gameSize * -0.5), gameSize, gameSize);
        //g.setColor(new Color(0.0f, 0.5f, 0.0f));
        //g.fillRect(-1,-1,1,1);
        
        if (main.cam != null) {
            g.translate(0 - Math.round(main.cam.x), 0 - Math.round(main.cam.y));
        }
        
        if (main.mapImage != null) g.drawImage(main.mapImage, 0, 0, null);
        if (main.tileMap != null) {
            int E_001 = main.tileMap.size() * 16;
            for (int y = 0; y < E_001; y++) {
                for (Entity i : E) {
                    if ((int)Math.round(i.getY()) == y) i.render(g);
                }
            }
        }
        if (main.mapLeaves != null) g.drawImage(main.mapLeaves, 0, -4, null);
        if (main.cam != null) {
            g.translate(Math.round(main.cam.x), Math.round(main.cam.y));
        }
        
        if (main.menu == -1 || main.menu == 0) {
            renderText(g, "Level: " + main.Level + " (Wolves: " + tool.getNumberOfWolves() + ")", -128 + 8, -128 + 9, T_003);
            renderText(g, "Score: " + main.score + " (Sheep: " + tool.getNumberOfSheep() + ")", -128 + 8, -128 + 21, T_003);
            renderText(g, "High Score: " + main.highScore, -128 + 8, -128 + 33, T_003);
            if (main.wolfAttack > 0) {
                renderText(g, "Wolf Attacks: +" + main.wolfAttack, -128 + 8, -128 + 45, T_001);
                renderText(g, "Wolf Attacks: +" + main.wolfAttack, -128 + 8, -128 + 44, T_002);
            }
            
            renderText(g, "Level: " + main.Level + " (Wolves: " + tool.getNumberOfWolves() + ")", -128 + 8, -128 + 8);
            renderText(g, "Score: " + main.score + " (Sheep: " + tool.getNumberOfSheep() + ")", -128 + 8, -128 + 20);
            renderText(g, "High Score: " + main.highScore, -128 + 8, -128 + 32);
            
            renderText(g, "Food: " + main.recordedFood, 128 - 10 - getTextWidth("Food: " + main.recordedFood), 128 - 15, T_003);
            renderText(g, "Food: " + main.recordedFood, 128 - 10 - getTextWidth("Food: " + main.recordedFood), 128 - 16);
            
            if (main.recordedHealth <= 15) {
                renderText(g, "Health: " + main.recordedHealth, -128 + 8, 128 - 15, T_001);
                renderText(g, "Health: " + main.recordedHealth, -128 + 8, 128 - 16, T_002);
            } else {
                renderText(g, "Health: " + main.recordedHealth, -128 + 8, 128 - 15, T_003);
                renderText(g, "Health: " + main.recordedHealth, -128 + 8, 128 - 16);
            }
            g.setColor(new Color(0f, 0f, 0f));
            g.fillRect(-25,128 - 15,50,6);
            g.setColor(new Color(1f, 0f, 0f));
            g.fillRect(-25,128 - 15,main.recordedHealth,6);
        }
        switch (main.menu) {
            case 0:
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
                g.fillRect((int)(gameSize * 0.5) * -1,(int)(gameSize * 0.5) * -1,gameSize,gameSize);
                renderText(g, "Paused", 0 - (int)(getTextWidth("Paused") * 0.5), -32);
                break;
            case 3:
                g.setColor(new Color(0.0f, 0.0f, 0.0f));
                g.fillRect((int)(gameSize * 0.5) * -1,(int)(gameSize * 0.5) * -1,gameSize,gameSize);
                renderText(g, "Game Over!", 0 - (int)(getTextWidth("Game Over!") * 0.5), -48);
                if (main.Difficulty >= 3) {
                    if (Math.random() < 0.25)
                    renderText(g, "[ E N T I T Y   G O T   Y O U ! ]", 0 - (int)(getTextWidth("[ E N T I T Y   G O T   Y O U ! ]") * 0.5) + (int)(Math.random() * 9 - 5), -16 + (int)(Math.random() * 9 - 5), T_001);
                    renderText(g, "[ E N T I T Y   G O T   Y O U ! ]", 0 - (int)(getTextWidth("[ E N T I T Y   G O T   Y O U ! ]") * 0.5), -16, T_002);
                } else if (main.recordedHealth <= 0) {
                    g.drawImage(main.shepherdA.get("dead"), 0 - 24, -24 - 19, null);
                    renderText(g, "You were overrun by wolves", 0 - (int)(getTextWidth("You were overrun by wolves") * 0.5), -16);
                } else {
                    g.drawImage(main.sheepA.get("dead"), 0 - 8, -24 - 15, null);
                    renderText(g, "You failed to protect your sheep", 0 - (int)(getTextWidth("You failed to protect your sheep") * 0.5), -16);
                }
                String hsText = "Highscore: " + main.highScore;
                if (main.highScore > main.highScorePrev) hsText = "New Highscore!";
                renderText(g, "Score: " + main.score, 0 - (int)(getTextWidth(hsText) * 0.5), 0);
                if (main.highScore > main.highScorePrev) {renderText(g, hsText, 0 - (int)(getTextWidth(hsText) * 0.5), 12, T_004);} else 
                renderText(g, hsText, 0 - (int)(getTextWidth(hsText) * 0.5), 12);
                break;
            case 64:
                g.drawImage(main.miscAssets.get("dev"), -16, -48, null);
                renderText(g, "Made by J. M. Potionwiz", 0 - (int)(getTextWidth("Made by J. M. Potionwiz") * 0.5), 32);
                break;
            case 1:
                for (int i = -1; i < 2; i++) for (int j = -1; j < 2; j++)
                g.drawImage(main.menuBackground, -(int)main.menuWander[0] + (j * 1024), -(int)main.menuWander[1] + (i * 1024), null);
                g.drawImage(main.miscAssets.get("title"), -80, -128 + 24, null);
                renderText(g, "version: 0.1.0", -126, 128 - 9);
                break;
            case 2:
                for (int i = -1; i < 2; i++) for (int j = -1; j < 2; j++)
                g.drawImage(main.menuBackground, -(int)main.menuWander[0] + (j * 1024), -(int)main.menuWander[1] + (i * 1024), null);
                g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
                g.fillRect((int)(gameSize * 0.5) * -1,(int)(gameSize * 0.5) * -1,gameSize,gameSize);
                break;
            
        }
        if (buttons.buttonsInUse) for (ArrayList<Button> b1 : B) {for (Button b : b1) {b.render(g);}}
        if (main.fade[0] > 0) {
            float f[] = {0.0f,0.5f,0.75f,1.0f};
            
            g.setColor(new Color(0.0f, 0.0f, 0.0f, f[(int)Math.min(main.fade[0] / 10, 3)]));
            g.fillRect((int)(gameSize * 0.5) * -1,(int)(gameSize * 0.5) * -1,gameSize,gameSize);
        }
        
        g.dispose();
        
        BufferedImage tmp1 = new BufferedImage((int)s1, (int)s1, 1);
        g = tmp1.createGraphics();
        g.translate((int)(s1*0.5),(int)(s1*0.5));
        g.translate(getSize().width * -0.5, getSize().height * -0.5);
        g.drawImage((Image) buff, 0, 0, null);
        
        
        Graphics2D g1 = (Graphics2D) c.getGraphics();
        g1.drawImage((Image) tmp1, (int)((getSize().width*0.5)-(s1*0.5)), (int)((getSize().height*0.5)-(s1*0.5)), null);
        
    }
    public Canvas getCanvas() {
        return this.c;
    }
    public void FULLSCREEN_MODE_AAAHH() {
        if (fullscreen == false) {
            GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice d = g.getDefaultScreenDevice();
            dispose();
            //setUndecorated(true);
            setVisible(true);
            d.setFullScreenWindow(this);
            fullscreen = true;
        } else if (fullscreen == true) {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            //dispose();
            //setUndecorated(false);
            //setVisible(true);
            setSize(512, 512);
            setLocation((dim.width - getSize().width) / 2, (dim.height - getSize().height) / 2);
            fullscreen = false;
        }
    }
    static int getCharWidth(String letter) {
        String c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.!?:,;'\"-+_()[]{}/\\*%<> ";
        String w = "55555555455465556556566565444444442343644444444464444555555555224222244443333444446555";
        String out = w.charAt(c.indexOf(letter)) + "";
        try {
            return Integer.parseInt(out);
        } catch(NumberFormatException e) {}
        return 5;
    }
    static int getTextWidth(String text) {
        int w = 0;
        int E_001 = text.length();
        for (int i = 0; i < E_001; i++) {
            w += getCharWidth(text.charAt(i) + "");
        }
        return w - 1;
    }
    static void renderText(Graphics2D g, String text, int x, int y) {
        int E_001 = text.length();
        for (int i = 0; i < E_001; i++) {
            g.drawImage(preloadText.get(text.charAt(i) + ""), x, y, null);
            x += getCharWidth(text.charAt(i) + "");
        }
    }
    static void renderText(Graphics2D g, String text, int x, int y, Text reloadText) {
        int E_001 = text.length();
        for (int i = 0; i < E_001; i++) {
            g.drawImage(reloadText.get(text.charAt(i) + ""), x, y, null);
            x += getCharWidth(text.charAt(i) + "");
        }
    }
}