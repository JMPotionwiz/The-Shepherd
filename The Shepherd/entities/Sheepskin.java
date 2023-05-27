package entities;

import core.main;
import java.awt.*;

public class Sheepskin extends WolfBase {
    protected boolean threatNear = false, notSuspect = false;
    protected int idle = 0;
    
    public Sheepskin(double x, double y) {// Easily my favorate sheep (wolf) EVER!
        super(x,y);
        this.health = 10;
        this.maxHealth = 10;
        this.healing = 0.0125;
        this.baseSpeed = 1.75;
        this.free = true;
        this.deathAnimMax = 40 * 10;
        this.bB = new boundingBox(-5d,-5d,5d,5d);
        this.target = null;
    }
    public void tick() {
        if (main.maintool.wolfSpawnZone(this.x, this.y) && (this.attacked || this.PANIC || this.stopHoming)) {
            this.relocateRestrike();
        } else {
            super.tick();
            if (this.attackCooldown == 0 && this.health > 0 && this.target != null) this.attack(this.bB, (this.PANIC) ? 8d:20d);
        }
    }
    public void control() {
        double moveX = 0, moveY = 0, speed = this.baseSpeed;
        Entity player = tool.getNearestPlayer(this.x, this.y);
        
        if (player != null) {
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) <= 48) threatNear = true;
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) > 64) threatNear = false;
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) > 128 && notSuspect) {
                this.target = tool.getNearestSheep(this.x, this.y); // SET TARGET HERE!
                notSuspect = false;
            }
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) < 96) this.target = null;
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) <= 72) notSuspect = true;
            if (!this.PANIC && tool.getDistance(this.x,this.y,player.getX(),player.getY()) > 128) speed *= 1.5;
            if (!this.PANIC && tool.getDistance(this.x,this.y,player.getX(),player.getY()) > 164) speed *= 2;
        } else {
            threatNear = false;
            notSuspect = true;
        }
        if (this.PANIC) speed *= 1.75;
        if (!this.PANIC && !this.threatNear) speed *= 0.2;
        
        if (!this.PANIC && this.target != null) {
            moveX = target.getX() - this.x;
            moveY = target.getY() - this.y;
            this.targetXY[0] = 0;
            this.targetXY[1] = 0;
        } else if (!this.PANIC && threatNear) {
            moveX = (player.getX() - this.x) * -1;
            moveY = (player.getY() - this.y) * -1;
            this.targetXY[0] = 0;
            this.targetXY[1] = 0;
            idle = 2 * 40;
        } else if (!this.PANIC && this.target == null && !notSuspect) {
            moveX = player.getX() - this.x;
            moveY = player.getY() - this.y;
            this.targetXY[0] = 0;
            this.targetXY[1] = 0;
            idle = 2 * 40;
        } else if (!this.PANIC) {
            if (this.idle <= 0) {
                this.targetXY[0] = (int)(Math.random() * 3) - 1;
                this.targetXY[1] = (int)(Math.random() * 3) - 1;
                this.idle = 20 + (int)(Math.random() * 40);
            }
            moveX = this.targetXY[0];
            moveY = this.targetXY[1];
            this.idle--;
        } else {
            moveX = this.targetXY[0];
            moveY = this.targetXY[1];
        }
        
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
            if (++this.moveAnim >= 32) this.moveAnim = 0;
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
            if (e instanceof Sheep) {
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
            this.target = null;
        }
    }
    public void underAttack(Entity a) {
        this.PANIC = true;
        this.targetXY[0] = (a.getX() - this.x) * -1;
        this.targetXY[1] = (a.getY() - this.y) * -1;
        if (this.health <= 0) {main.score += 32;}
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
        this.target = null;
    }
    public void render(Graphics2D g) {
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        if (this.health <= 0) {
            g.fillRect((int)Math.round(this.x - 10),(int)Math.round(this.y + 1),20,1);
        } else g.fillRect((int)Math.round(this.x - 6),(int)Math.round(this.y + 1),12,1);
        StringBuilder i = new StringBuilder("idle");
        if (this.moving) {
            String[] s = {"walk1","idle","walk2","idle"};
            i = new StringBuilder(s[(int)(this.moveAnim / 8)]);
        }
        if (this.health <= 0) {i = new StringBuilder("dead");}
        if (!this.dir[1]) i.append("_back");
        if (!this.dir[0]) i.append("_flipped");
        if ((this.invul/2) % 2 == 0 && ((this.deathAnim/2) % 2 == 0 || this.deathAnim < this.deathAnimMax - 40))
        g.drawImage(main.sheepskinA.get(i.toString()), (int)Math.round(this.x)-12, (int)Math.round(this.y)-15, null);
        if ((this.health < this.maxHealth || main.alwaysShowHealthbars) && this.health > 0) {
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,8,1);
            g.setColor(new Color(1.0f, 0.0f, 0.0f));
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,(int)Math.round((this.health / this.maxHealth) * 8),1);
        }
    }
}
