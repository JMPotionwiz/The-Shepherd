package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class Text {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public Text(int cr, int cg, int cb) {
        cr = Math.max(Math.min(cr,255), 0);
        cg = Math.max(Math.min(cg,255), 0);
        cb = Math.max(Math.min(cb,255), 0);
        
        final int sx = 5, sy = 8, f = 85;
        InputStream tmp = getClass().getResourceAsStream("/assets/text.png");
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
        String n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.!?:,;'\"-+_()[]{}/\\*%<>";
        
        for (int y = 0; y < normal.getHeight(); y++) {
            for (int x = 0; x < normal.getWidth(); x++) {
                int p = normal.getRGB(x,y);
                Color c = new Color(p, true);
                if (c.getAlpha() <= 0) continue;
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red == 255 && green == 255 && blue == 255) {
                    c = new Color(cr, cg, cb);
                }
                normal.setRGB(x, y, c.getRGB());
            }
        }
        for (int i = 0; i < n.length(); i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, sx * i * -1, 0, null);
            g.dispose();
            sprites.put(n.charAt(i) + "", frame);
        }
    }
    public Text(float cr, float cg, float cb) {
        cr = Math.max(Math.min(cr,1f), 0);
        cg = Math.max(Math.min(cg,1f), 0);
        cb = Math.max(Math.min(cb,1f), 0);
        
        final int sx = 5, sy = 8, f = 85;
        InputStream tmp = getClass().getResourceAsStream("/assets/text.png");
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
        String n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.!?:,;'\"-+_()[]{}/\\*%<>";
        
        for (int y = 0; y < normal.getHeight(); y++) {
            for (int x = 0; x < normal.getWidth(); x++) {
                int p = normal.getRGB(x,y);
                Color c = new Color(p, true);
                if (c.getAlpha() <= 0) continue;
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red == 255 && green == 255 && blue == 255) {
                    c = new Color(cr, cg, cb);
                }
                normal.setRGB(x, y, c.getRGB());
            }
        }
        for (int i = 0; i < n.length(); i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, sx * i * -1, 0, null);
            g.dispose();
            sprites.put(n.charAt(i) + "", frame);
        }
    }
    public Text() {
        final int sx = 5, sy = 8, f = 85;
        InputStream tmp = getClass().getResourceAsStream("/assets/text.png");
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
        String n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.!?:,;'\"-+_()[]{}/\\*%<>";
        
        for (int i = 0; i < n.length(); i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, sx * i * -1, 0, null);
            g.dispose();
            sprites.put(n.charAt(i) + "", frame);
        }
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
