package entities;

import core.main;
import java.awt.*;

public class DerpShep extends Sheep {
    public DerpShep(double x, double y, boolean falling) {
        super(x,y,falling);
    }
    public void render(Graphics2D g) {
        if (this.fall > 0) {
            g.setColor(new Color(1.0f,1.0f,0.5f,(float)this.fall/80f));
            int tmp = Math.min(80 - this.fall, 16) / 2;
            g.fillRect((int)Math.round(this.x - tmp),(int)Math.round(this.y + 1 - 80*80),tmp * 2,80*80);
        }
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        if (this.fall <= 0) if (this.health <= 0) {
            g.fillRect((int)Math.round(this.x - 9),(int)Math.round(this.y + 1),18,1);
        } else g.fillRect((int)Math.round(this.x - 6),(int)Math.round(this.y + 1),12,1);
        StringBuilder i = new StringBuilder("idle");
        if (this.moving) {
            String[] s = {"walk1","idle","walk2","idle"};
            i = new StringBuilder(s[(int)(this.moveAnim / 8)]);
        }
        if (this.health <= 0) {i = new StringBuilder("dead");} else if ((int)this.health <= 15) i.append("_hurt");
        if (!this.dir[0]) i.append("_flipped");
        if (this.black) i.append("_black");
        if ((this.invul/2) % 2 == 0 && ((this.deathAnim/2) % 2 == 0 || this.deathAnim < this.deathAnimMax - 40))
        g.drawImage(main.derpA.get(i.toString()), (int)Math.round(this.x)-8, (int)Math.round(this.y)-15 - (int)Math.pow(this.fall * 0.4,2), null);
        if ((this.health < this.maxHealth || main.alwaysShowHealthbars) && this.health > 0 && this.fall <= 0) {
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,8,1);
            g.setColor(new Color(1.0f, 0.0f, 0.0f));
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,(int)Math.round((this.health / this.maxHealth) * 8),1);
        }
    }
}
