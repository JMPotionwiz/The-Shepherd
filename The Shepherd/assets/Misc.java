package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class Misc {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public Misc() {
        int sx = 32, sy = 72, f = 8;
        InputStream tmp = getClass().getResourceAsStream("/assets/jmpotionwiz.png");
        Image img = null;
        try {
            img = ImageIO.read(tmp);
        } catch(Exception e){
            core.main.paused = true;
            core.main.ERRAH = e + "";
        }
        
        BufferedImage normal = new BufferedImage(sx*f,sy,BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = normal.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        BufferedImage frame = null;
        
        frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
        g = frame.createGraphics();
        g.drawImage((Image) normal, sx * (int)(Math.random() * 8) * -1, 0, null);
        g.dispose();
        sprites.put("dev", frame);
        
        sx = 80; sy = 32; f = 1;
        tmp = getClass().getResourceAsStream("/assets/title.png");
        img = null;
        try {
            img = ImageIO.read(tmp);
        } catch(Exception e){
            core.main.paused = true;
            core.main.ERRAH = e + "";
        }
        
        normal = new BufferedImage(sx*f,sy,BufferedImage.TYPE_INT_ARGB);
        
        g = normal.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        frame = null;
        
        frame = new BufferedImage(sx * 2,sy * 2,BufferedImage.TYPE_INT_ARGB);
        g = frame.createGraphics();
        g.scale(2,2);
        g.drawImage((Image) normal, 0, 0, null);
        g.dispose();
        sprites.put("title", frame);
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
