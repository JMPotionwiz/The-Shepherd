package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class DerpShepAssets {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public DerpShepAssets() {
        final int sx = 16, sy = 16, f = 7;
        InputStream tmp = getClass().getResourceAsStream("/assets/derp_shep.png");
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
        String[] n = {"idle","walk1","walk2","idle_hurt","walk1_hurt","walk2_hurt","dead"};
        
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
        for (int y = 0; y < normal.getHeight(); y++) {
            for (int x = 0; x < normal.getWidth(); x++) {
                int p = normal.getRGB(x,y);
                Color c = new Color(p, true);
                if (c.getAlpha() <= 0) continue;
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red == 0 && green == 0 && blue == 0) {
                    c = new Color(255, 255, 255);
                } else if (red == 255 && green == 255 && blue == 255) {
                    c = new Color(68, 68, 68);
                } else if (red == 221 && green == 221 && blue == 221) {
                    c = new Color(51, 51, 51);
                } else if (red == 187 && green == 187 && blue == 187) {
                    c = new Color(34, 34, 34);
                }
                normal.setRGB(x, y, c.getRGB());
            }
        }
        for (int y = 0; y < flipped.getHeight(); y++) {
            for (int x = 0; x < flipped.getWidth(); x++) {
                int p = flipped.getRGB(x,y);
                Color c = new Color(p, true);
                if (c.getAlpha() <= 0) continue;
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red == 0 && green == 0 && blue == 0) {
                    c = new Color(255, 255, 255);
                } else if (red == 255 && green == 255 && blue == 255) {
                    c = new Color(68, 68, 68);
                } else if (red == 221 && green == 221 && blue == 221) {
                    c = new Color(51, 51, 51);
                } else if (red == 187 && green == 187 && blue == 187) {
                    c = new Color(34, 34, 34);
                }
                flipped.setRGB(x, y, c.getRGB());
            }
        }
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i] + "_black", frame);
        }
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) flipped, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i] + "_flipped_black", frame);
        }
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
