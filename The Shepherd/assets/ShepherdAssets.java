package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class ShepherdAssets {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public ShepherdAssets() {
        final int sx = 48, sy = 20, f = 12;
        InputStream tmp = getClass().getResourceAsStream("/assets/shepherd.png");
        InputStream tmp_back = getClass().getResourceAsStream("/assets/shepherd_back.png");
        Image img = null, img_back = null;
        try {
            img = ImageIO.read(tmp);
            img_back = ImageIO.read(tmp_back);
        } catch(Exception e){
            core.main.paused = true;
            core.main.ERRAH = e + "";
        }
        
        BufferedImage normal = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB), flipped = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB);
        BufferedImage normal_back = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB), flipped_back = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = normal.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        g = flipped.createGraphics();
        g.translate(sx,0);
        g.scale(-1,1);
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        g = normal_back.createGraphics();
        g.drawImage(img_back, 0, 0, null);
        g.dispose();
        
        g = flipped_back.createGraphics();
        g.translate(sx,0);
        g.scale(-1,1);
        g.drawImage(img_back, 0, 0, null);
        g.dispose();
        
        BufferedImage frame = null;
        String[] n = {"idle","walk1","walk2","walk3","walk4","attack_0","attack_1","attack_2","attack_3","attack_4","attack_5","dead"};
        
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
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal_back, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i] + "_back", frame);
        }
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) flipped_back, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i] + "_back_flipped", frame);
        }
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
