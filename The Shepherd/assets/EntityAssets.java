package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class EntityAssets {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public EntityAssets() {
        final int sx = 16, sy = 24, f = 4;
        InputStream tmp = getClass().getResourceAsStream("/assets/entity.png");
        Image img = null;
        try {
            img = ImageIO.read(tmp);
        } catch(Exception e){
            core.main.paused = true;
            core.main.ERRAH = e + "";
        }
        
        BufferedImage normal = new BufferedImage(sx,sy*f,BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = normal.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        
        BufferedImage frame = null;
        String[] n = {"idle","idle_back","idle_flipped","idle_back_flipped"};
        
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i], frame);
        }
        for (int y = 0; y < normal.getHeight(); y++) {
            for (int x = 0; x < normal.getWidth(); x++) {
                int p = normal.getRGB(x,y);
                Color c = new Color(p, true);
                if (c.getAlpha() <= 0) continue;
                c = new Color(1f,0f,0f,0.75f);
                normal.setRGB(x, y, c.getRGB());
            }
        }
        frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
        g = frame.createGraphics();
        g.drawImage((Image) normal, 0, 0, null);
        g.dispose();
        sprites.put("shadow", frame);
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
