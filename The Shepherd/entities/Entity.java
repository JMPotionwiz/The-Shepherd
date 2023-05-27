package entities;

import core.main;
import java.awt.*;
import assets.EntityAssets;

public class Entity {
    protected double x, y, health, maxHealth, baseSpeed, velocityX, velocityY, healing;
    protected int ID, deathAnim = 0, deathAnimMax, attackCooldown = 0, invul = 0, moveAnim = 0, blighted = 0;
    protected boolean free, moving = false;
    protected boundingBox bB;
    protected boolean[] dir = {true,true};
    protected double[] targetXY = {0,0};
    
    private EntityAssets a = new EntityAssets();
    private Entity t = null;
    
    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
        this.ID = main.UID++; //Assign a unique ID to this entity
        this.velocityX = 0;
        this.velocityY = 0;
        this.health = 9999;
        this.maxHealth = 9999;
        this.healing = 9999;
        this.baseSpeed = 2.75;
        this.free = true;
        this.deathAnimMax = 1;
        this.bB = new boundingBox(-5d,-5d,5d,5d);
    }
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public int ID() {return this.ID;}
    public double getHealth() {return this.health;}
    public double getMaxHealth() {return this.maxHealth;}
    public int invul() {return (this.health > 0) ? this.invul:0;}
    public boolean free() {return this.free;}
    public void heal(double h) {if (this.health > 0) this.health = Math.min(this.health + h * ((this.blighted > 0) ? 0.5d:1d), this.maxHealth);}
    public boolean deathAnimFinished() {
        if (this.health <= 0) return this.deathAnim++ >= this.deathAnimMax;
        return false;
    }
    
    public void tick() {
        if (this.health > 0 && this.health < this.maxHealth && this.blighted <= 0) this.health = Math.min(this.health + this.healing, this.maxHealth);
        if (this.attackCooldown > 0) {this.attackCooldown--;}
        if (this.invul > 0) {this.invul--;}
        if (this.blighted > 0) {
            this.blighted--;
            if (Math.random() * 30d <= 1) main.spawnIn.add(new Fly(this.x + Math.floor(Math.random() * 7 - 3),this.y +  + Math.floor(Math.random() * 7 - 3)));
        }
        this.moving = false;
        if (this.health > 0) this.control();
        this.move();
        //System.out.println(this.x + ", " + this.y);
    }
    public void move() {
        double moveX = this.velocityX, moveY = this.velocityY;
        
        this.x += moveX;
        this.y += moveY;
        if (!this.free) {
            this.x = Math.max(Math.min(this.x, (main.tileMap.get(0).size()*16) - 16*16 - this.bB.x2), 16*16 - this.bB.x1);
            this.y = Math.max(Math.min(this.y, (main.tileMap.size()*16) - 16*16 - this.bB.y2), 16*16 - this.bB.y1);
        }
        this.velocityX *= 0.75d;
        this.velocityY *= 0.75d;
    }
    public void control() {
        double moveX = 0, moveY = 0, speed = this.baseSpeed;
        if (this.t == null && (tool.getNumberOfPlayers() > 0 || tool.getNumberOfSheep() > 0)) this.t = tool.getRandomPlayerOrSheep();
        if (this.t == null) return;
        if (Math.random() < 0.0125) this.t = tool.getRandomPlayerOrSheep();
        moveX = this.t.getX() - this.x;
        moveY = this.t.getY() - this.y;
        
        double d = Math.hypot(moveX, moveY);
        if (d != 0) {
            moveX /= d;
            moveY /= d;
        }
        
        if (moveX > 0) this.dir[0] = true;
        if (moveX < 0) this.dir[0] = false;
        if (moveY > 0) this.dir[1] = true;
        if (moveY < 0) this.dir[1] = false;
        
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
        this.attack(this.bB, 9999);
        this.baseSpeed = Math.min(this.baseSpeed + 0.005, 4.5);
    }
    public void attack(boundingBox b, double d) {
        boolean attackSuccessful = false;
        for (Entity e : main.entities) {
            if (e == this || e.invul > 0 || e.health <= 0) continue;
            if (e instanceof Entity) {
                if (e.getX() - this.x >= b.x1 && e.getX() - this.x <= b.x2 && e.getY() - this.y >= b.y1 && e.getY() - this.y <= b.y2) {
                    e.health -= d;
                    e.invul = 10;
                    e.underAttack(this);
                    attackSuccessful = true;
                }
            }
        }
        if (attackSuccessful == true) {
            this.t = null;
        }
        this.attackCooldown = 0;
    }
    public void underAttack(Entity a) {}
    public void retarget(Entity t) {}
    public void render(Graphics2D g) {
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        g.fillRect((int)Math.round(this.x - 6),(int)Math.round(this.y + 1),12,1);
        StringBuilder i = new StringBuilder("idle");
        if (!this.dir[1]) i.append("_back");
        if (!this.dir[0]) i.append("_flipped");
        if ((this.invul/2) % 2 == 0 && ((this.deathAnim/2) % 2 == 0 || this.deathAnim < this.deathAnimMax - 40)) {
            if (Math.random() < 0.25)
            g.drawImage(this.a.get("shadow"), (int)Math.round(this.x + (Math.random() * 11 - 6))-8, (int)Math.round(this.y + (Math.random() * -5))-23, null);
            g.drawImage(this.a.get(i.toString()), (int)Math.round(this.x + (Math.random() * 3 - 1))-8, (int)Math.round(this.y + (Math.random() * -2))-23, null);
        }
    }
}





