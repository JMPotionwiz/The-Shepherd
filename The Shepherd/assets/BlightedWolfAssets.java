package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class BlightedWolfAssets {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public BlightedWolfAssets() {
        final int sx = 24, sy = 16, f = 10;
        InputStream tmp = getClass().getResourceAsStream("/assets/blighted_wolf.png");
        Image img = null;
        try {
            img = ImageIO.read(tmp);
        } catch(Exception e){
            core.main.paused = true;
            core.main.ERRAH = e + "";
        }
        
        BufferedImage normal = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB), flipped = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = normal.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        g = flipped.createGraphics();
        g.translate(sx,0);
        g.scale(-1,1);
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        BufferedImage frame = null;
        String[] n = {"idle","walk1","walk2","walk3","idle_back","walk1_back","walk2_back","walk3_back","dead","dead_back"};
        
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i], frame);
        }
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) flipped, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i] + "_flipped", frame);
        }
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
