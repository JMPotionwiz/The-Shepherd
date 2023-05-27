package assets;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.InputStream;

public class LeavesAssets {
    HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    public LeavesAssets() {
        final int sx = 16, sy = 16, f = 13;
        InputStream tmp = getClass().getResourceAsStream("/assets/leaves.png");
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
        
        for (int y = 0; y < normal.getHeight(); y++) {
            for (int x = 0; x < normal.getWidth(); x++) {
                int p = normal.getRGB(x,y);
                Color c = new Color(p, true);
                if (c.getAlpha() <= 0) continue;
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                if (red == 0 && green == 0 && blue == 0) {
                    c = new Color(0f,0f,0f,0.3f);
                }
                normal.setRGB(x, y, c.getRGB());
            }
        }
        
        BufferedImage frame = null;
        String[] n = {"leaves","leaves_thin_0","leaves_thin_1","leaves_thin_2","leaves_thin_3","leaves_edgeN","leaves_edgeS","leaves_edgeW","leaves_edgeE","leaves_edgeNW","leaves_edgeSW","leaves_edgeSE","leaves_edgeNE"};
        
        for (int i = 0; i < n.length; i++) {
            frame = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_ARGB);
            g = frame.createGraphics();
            g.drawImage((Image) normal, 0, sy * i * -1, null);
            g.dispose();
            sprites.put(n[i], frame);
        }
    }
    public Image get(String s) {return (Image)sprites.get(s);}
}
