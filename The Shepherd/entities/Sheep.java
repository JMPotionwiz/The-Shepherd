package entities;

import core.main;
import java.awt.*;

public class Sheep extends Entity {
    protected boolean BAAAH_sHeEPeRdNeAR = false, waitWhereAreYouGoingCOMEBACK = false, black;
    protected int idle = 0, fall = 0;
    
    public Sheep(double x, double y, boolean falling) {
        super(x,y);
        this.health = 20;
        this.maxHealth = 20;
        this.healing = 0.0125;
        this.baseSpeed = 1.75;
        this.free = false;
        this.deathAnimMax = 40 * 10;
        this.black = ((int)(Math.random() * 20) == 0);
        this.bB = new boundingBox(-5d,-8d,5d,2d);
        if (falling) {
            fall = 80;
            tool.playSoundWithinDistance("sfx_sheepspawn", this.x, this.y, 256);
        }
        hurtSound = "sfx_hurtsheep";
        deathSound = "sfx_sheepdeath";
    }
    public void tick() {
        if (this.fall <= 0) {
            super.tick();
        } else this.fall--;
    }
    public void control() {
        double moveX = 0, moveY = 0, speed = this.baseSpeed;
        Entity player = tool.getNearestPlayer(this.x, this.y);
        if (player != null) {
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) <= 48) BAAAH_sHeEPeRdNeAR = true;
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) > 64) BAAAH_sHeEPeRdNeAR = false;
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) > 128) waitWhereAreYouGoingCOMEBACK = true;
            if (tool.getDistance(this.x,this.y,player.getX(),player.getY()) <= 72) waitWhereAreYouGoingCOMEBACK = false;
        } else {
            BAAAH_sHeEPeRdNeAR = false;
            waitWhereAreYouGoingCOMEBACK = false;
        }
        if (!BAAAH_sHeEPeRdNeAR) speed *= 0.2;
        if ((int)this.health <= 15) speed *= 0.5;
        
        if (BAAAH_sHeEPeRdNeAR) {
            moveX = (player.getX() - this.x) * -1;
            moveY = (player.getY() - this.y) * -1;
            this.targetXY[0] = 0;
            this.targetXY[1] = 0;
            idle = 2 * 40;
        } else if (waitWhereAreYouGoingCOMEBACK) {
            moveX = player.getX() - this.x;
            moveY = player.getY() - this.y;
            this.targetXY[0] = 0;
            this.targetXY[1] = 0;
            idle = 2 * 40;
        } else {
            if (this.idle <= 0) {
                this.targetXY[0] = (int)(Math.random() * 3) - 1;
                this.targetXY[1] = (int)(Math.random() * 3) - 1;
                this.idle = 20 + (int)(Math.random() * 40);
            }
            moveX = this.targetXY[0];
            moveY = this.targetXY[1];
            this.idle--;
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
    public void underAttack(Entity a) {
        super.underAttack(a);
        if (this.health <= 0) if (a instanceof Sheepskin) {
            core.main.lastAttackBySheepskin = true;
        } else core.main.lastAttackBySheepskin = false;
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
        if (!this.dir[1]) i.append("_back");
        if (this.fall > 0) i = new StringBuilder("fall");
        if (!this.dir[0]) i.append("_flipped");
        if (this.black) i.append("_black");
        if ((this.invul/2) % 2 == 0 && ((this.deathAnim/2) % 2 == 0 || this.deathAnim < this.deathAnimMax - 40))
        g.drawImage(main.sheepA.get(i.toString()), (int)Math.round(this.x)-8, (int)Math.round(this.y)-15 - (int)Math.pow(this.fall * 0.4,2), null);
        if ((this.health < this.maxHealth || main.alwaysShowHealthbars) && this.health > 0 && this.fall <= 0) {
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,8,1);
            g.setColor(new Color(1.0f, 0.0f, 0.0f));
            g.fillRect((int)Math.round(this.x)-4, (int)Math.round(this.y)-15,(int)Math.round((this.health / this.maxHealth) * 8),1);
        }
    }
}
