package entities;

import core.main;
import java.awt.*;

public class Player extends Entity {
    private boolean[] lastWS = {false,false}, lastAD = {false,false};
    private boolean attacked = false, attacking = false, FOOD = false;
    private int attackAnim = 0, food = Math.max(5 - (2 * main.Difficulty), 0);
    
    public Player(double x, double y) {
        super(x,y);
        this.health = 50;
        this.maxHealth = 50;
        this.healing = 0.0125;
        this.baseSpeed = 4;
        this.free = false;
        this.deathAnimMax = 1;
        this.bB = new boundingBox(-4d,-6d,4d,2d);
    }
    public boolean deathAnimFinished() {return false;}
    public void tick() {
        if (this.attacking) {this.attackAnim++;} else this.attackAnim = 0;
        if (this.attackAnim >= 16) {
            this.attacking = false;
        }
        if (this.attackAnim == 5) {
            this.attack(new boundingBox(-24d,-24d,24d,24d), 6);
        }
        
        super.tick();
        
        if (main.cam != null) {
            main.cam.targetX = this.x;
            main.cam.targetY = this.y;
        }
        main.recordedHealth = Math.max((int)this.health, ((this.health > 0) ? 1:0));
        main.recordedFood = this.food;
    }
    public void control() {
        double moveX = 0, moveY = 0, speed = this.baseSpeed;
        
        if ((int)this.health <= 15) speed *= 0.5;
        
        if (main.keys[0].pressed() && lastWS[0] == false) {
            dir[1] = false;
            lastWS[0] = true;
        } else if (!main.keys[0].pressed()) lastWS[0] = false;
        if (main.keys[1].pressed() && lastWS[1] == false) {
            dir[1] = true;
            lastWS[1] = true;
        } else if (!main.keys[1].pressed()) lastWS[1] = false;
        if (main.keys[2].pressed() && lastAD[0] == false) {
            dir[0] = false;
            lastAD[0] = true;
        } else if (!main.keys[2].pressed()) lastAD[0] = false;
        if (main.keys[3].pressed() && lastAD[1] == false) {
            dir[0] = true;
            lastAD[1] = true;
        } else if (!main.keys[3].pressed()) lastAD[1] = false;
        
        if (main.keys[0].pressed() && !dir[1]) moveY -= 1;
        if (main.keys[1].pressed() && dir[1]) moveY += 1;
        if (main.keys[2].pressed() && !dir[0]) moveX -= 1;
        if (main.keys[3].pressed() && dir[0]) moveX += 1;
        if (main.keys[4].pressed() && !this.attacked && !this.attacking && !main.absorbAttackCall) {
            this.attacking = true;
            this.attacked = true;
        } else if (!main.keys[4].pressed() && this.attackCooldown == 0) {
            this.attacked = false;
        }
        if (main.keys[5].pressed() && !this.FOOD && !this.attacking && this.food > 0 && this.health < this.maxHealth) {
            this.food--;
            this.heal(8);
            this.FOOD = true;
        } else if (!main.keys[5].pressed()) this.FOOD = false;
        if (this.attacking) {
            moveX = 0;
            moveY = 0;
        }
        
        double d = Math.hypot(moveX, moveY);
        if (d != 0) {
            moveX /= d;
            moveY /= d;
        }
        
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
            if (e instanceof WolfBase) {
                if (e.getX() - this.x >= b.x1 && e.getX() - this.x <= b.x2 && e.getY() - this.y >= b.y1 && e.getY() - this.y <= b.y2) {
                    e.health -= d;
                    e.invul = 10;
                    e.underAttack(this);
                    attackSuccessful = true;
                    if (e.health <= 0) this.food = Math.min(this.food + 1, Math.max(7 - (main.Difficulty * 2), 0));
                }
            }
        }
        this.attackCooldown = 8;
        if (attackSuccessful == true) {main.freezeFrame = true;}
    }
    public void render(Graphics2D g) {
        g.setColor(new Color(0.0f, 0.0f, 0.0f));
        if (this.health <= 0) {
            g.fillRect((int)Math.round(this.x - 7),(int)Math.round(this.y + 1),14,1);
        } else g.fillRect((int)Math.round(this.x - 6),(int)Math.round(this.y + 1),12,1);
        StringBuilder i = new StringBuilder("idle");
        if (this.moving) {
            String[] s = {"walk1","walk2","walk3","walk4"};
            i = new StringBuilder(s[(int)(this.moveAnim / 4)]);
        }
        //if (this.attackAnim < 2 && this.attacking) i = new StringBuilder("attack_0");
        if (this.attacking) {
            i = new StringBuilder("attack_" + (int)(Math.min((attackAnim) * 0.5, 5)));
        }
        if (this.health <= 0) {i = new StringBuilder("dead");}
        if (!this.dir[1]) i.append("_back");
        if (!this.dir[0]) i.append("_flipped");
        if ((this.invul/2) % 2 == 0 && ((this.deathAnim/2) % 2 == 0 || this.deathAnim < this.deathAnimMax - 40))
        g.drawImage(main.shepherdA.get(i.toString()), (int)Math.round(this.x)-24, (int)Math.round(this.y)-19, null);
        
    }
}
