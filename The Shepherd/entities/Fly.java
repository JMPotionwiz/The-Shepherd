package entities;
import java.awt.Graphics2D;
import java.awt.Color;

public class Fly extends Entity {
    private int timer, idleSoundCooldown;
    
    public Fly(double x, double y) {
        super(x,y);
        this.health = 1;
        this.maxHealth = 1;
        this.deathAnim = 0;
        this.deathAnimMax = 0;
        this.timer = (int)Math.round(Math.random() * 80) + 80;
    }
    public void tick() {
        this.x += ((int)(Math.random() * 3)) - 1;
        this.y += ((int)(Math.random() * 3)) - 1;
        if (--this.idleSoundCooldown <= 0) {
            if (this.idleSoundCooldown == 0 && this.health > 0) tool.playSoundWithinDistance("sfx_fly", this.x, this.y, 64);
            this.idleSoundCooldown = 20 + (int)(Math.random() * 30);
        }
        this.timer--;
        if (this.timer <= 0) this.health = 0;
    }
    public void render(Graphics2D g) {
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        g.fillRect((int)Math.round(this.x - 1),(int)Math.round(this.y - 14),1,1);
    }
}