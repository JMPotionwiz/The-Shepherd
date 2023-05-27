package entities;

import core.main;
import java.awt.*;

public class Dartwolf extends WolfBase {
    public Dartwolf(double x, double y) {
        super(x,y);
        this.health = 5;
        this.maxHealth = 5;
        this.healing = 0;
        this.baseSpeed = 3;
        this.free = true;
        this.deathAnimMax = 40 * 10;
        this.bB = new boundingBox(-5d,-5d,5d,5d);
        this.target = tool.getNearestPlayer(x,y);
    }
    public void tick() {
        if (main.maintool.wolfSpawnZone(this.x, this.y) && (this.attacked || this.PANIC || this.stopHoming)) {
            this.relocateRestrike();
        } else {
            super.tick();
            if (this.attackCooldown == 0 && this.health > 0) this.attack(this.bB, ((this.PANIC) ? 10d:10d) + main.wolfAttack);
        }
    }
    public void control() {
        if (tool.getDistance(this.x, this.y, target.getX(), target.getY()) <= 48) this.stopHoming = true;
        
        if (!this.attacked && !this.PANIC && !this.stopHoming && this.target != null) {
            this.targetXY[0] = target.getX() - this.x;
            this.targetXY[1] = target.getY() - this.y;
        }
        
        double moveX = this.targetXY[0], moveY = this.targetXY[1], speed = this.baseSpeed;
        
        double d = Math.hypot(moveX, moveY);
        if (d != 0) {
            moveX /= d;
            moveY /= d;
        }
        
        if (moveX > 0) this.dir[0] = true;
        if (moveX < 0) this.dir[0] = false;
        if (moveY > 0) this.dir[1] = true;
        if (moveY < 0) this.dir[1] = false;
        
        if (Math.abs(moveX) + Math.abs(moveY) > 0) {
            if (++this.moveAnim >= 16) this.moveAnim = 0;
            this.moving = true;
        } else this.moveAnim = 0;
        
        moveX *= speed * 0.5d;
        moveY *= speed * 0.5d;
        this.velocityX += moveX;
        this.velocityY += moveY;
        
        d = Math.hypot(this.velocityX,this.velocityY);
        if (d != 0 && d > speed) {
            this.velocityX /= d;
            this.velocityY /= d;
            this.velocityX *= speed;
            this.velocityY *= speed;
        }
    }
    public void attack(boundingBox b, double d) {
        boolean attackSuccessful = false;
        for (Entity e : main.entities) {
            if (e == this || e.invul > 0 || e.health <= 0) continue;
            if (e instanceof Player) {
                if (e.getX() - this.x >= b.x1 && e.getX() - this.x <= b.x2 && e.getY() - this.y >= b.y1 && e.getY() - this.y <= b.y2) {
                    e.health -= d;
                    e.invul = 10;
                    e.underAttack(this);
                    attackSuccessful = true;
                }
            }
        }
        if (attackSuccessful == true) {
            this.attackCooldown = 40;
            this.attacked = true;
            double r = Math.toRadians(Math.random() * 360);
            this.targetXY[0] = Math.cos(r);
            this.targetXY[1] = Math.sin(r);
        }
    }
    public void underAttack(Entity a) {
        this.PANIC = true;
        this.targetXY[0] = (a.getX() - this.x) * -1;
        this.targetXY[1] = (a.getY() - this.y) * -1;
    }
    public void relocateRestrike() {
        if (tool.getNumberOfPlayers() <= 0 && tool.getNumberOfSheep() <= 0) return;
        double attemptX, attemptY;
        byte tries = 0;
        do {
            attemptX = Math.random() * ((main.tileMap.get(0).size() - 2) * 16) + 16;
            attemptY = Math.random() * ((main.tileMap.size() - 2) * 16) + 16;
            if (tries++ >= 64) return;
        } while (!main.maintool.wolfSpawnZone(attemptX,attemptY));
        this.x = attemptX;
        this.y = attemptY;
        this.PANIC = false;
        this.attacked = false;
        this.stopHoming = false;
        this.target = tool.getNearestPlayer(this.x, this.y);
    }
    public void render(Graphics2D g) {
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        if (this.health <= 0) {
            g.fillRect((int)Math.round(this.x - 10),(int)Math.round(this.y + 1),20,1);
        } else g.fillRect((int)Math.round(this.x - 6),(int)Math.round(this.y + 1),12,1);
        StringBuilder i = new StringBuilder("idle");
        if (this.moving) {
            String[] s = {"idle","walk1","walk2","walk3"};
            i = new StringBuilder(s[(int)(this.moveAnim / 4)]);
        }
        if (this.health <= 0) {i = new StringBuilder("dead");}
        if (!this.dir[1]) i.append("_back");
        if (!this.dir[0]) i.append("_flipped");
        if ((this.invul/2) % 2 == 0 && ((this.deathAnim/2) % 2 == 0 || this.deathAnim < this.deathAnimMax - 40))
        g.drawImage(main.dwolfA.get(i.toString()), (int)Math.round(this.x)-12, (int)Math.round(this.y)-15, null);
        if ((this.health < this.maxHealth || main.alwaysShowHealthbars) && this.health > 0) {
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,8,1);
            g.setColor(new Color(1.0f, 0.0f, 0.0f));
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,(int)Math.round((this.health / this.maxHealth) * 8),1);
        }
    }
}
