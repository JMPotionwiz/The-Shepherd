package entities;

import core.main;
import java.util.ArrayList;

class boundingBox {
    public double x1, x2, y1, y2;
    public boundingBox(double x1,double y1,double x2,double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        if (this.x1 > this.x2) {
            double x = this.x1;
            this.x1 = this.x2;
            this.x2 = x;
        }
        if (this.y1 > this.y2) {
            double y = this.y1;
            this.y1 = this.y2;
            this.y2 = y;
        }
    }
    public double getWidth() {
        return this.x2 - this.x1;
    }
    public double getLength() {
        return this.y2 - this.y1;
    }
}
public class tool {
    static Entity getEntityByID(int ID) {
        for (Entity e : main.entities) if (e.ID() == ID) {return e;};
        return null;
    }
    static Entity getNearestPlayer(double x, double y) {
        Entity p = null;
        double d = -1;
        for (Entity e : main.entities) {
            if (e instanceof Player) {
                if (d == -1 || (getDistance(x,y,e.getX(),e.getY()) < d)) {
                    d = getDistance(x,y,e.getX(),e.getY());
                    p = e;
                }
            }
        }
        return p;
    }
    static Entity getNearestSheep(double x, double y) {
        Entity s = null;
        double d = -1;
        for (Entity e : main.entities) {
            if (e instanceof Sheep) {
                if (d == -1 || (getDistance(x,y,e.getX(),e.getY()) < d)) {
                    d = getDistance(x,y,e.getX(),e.getY());
                    s = e;
                }
            }
        }
        return s;
    }
    static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }
    static Entity getRandomPlayerOrSheep() {
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (Entity e : main.entities) {
            if ((e instanceof Player || e instanceof Sheep) && e.health > 0) targets.add(e.ID());
        }
        if (targets.size() > 0) return getEntityByID(targets.get((int)(Math.random() * targets.size())));
        return null;
    }
    static Entity getNearestPlayerOrSheep(double x, double y) {
        Entity p = null;
        double d = -1;
        for (Entity e : main.entities) {
            if (e instanceof Player || e instanceof Sheep) {
                if (d == -1 || (getDistance(x,y,e.getX(),e.getY()) < d)) {
                    d = getDistance(x,y,e.getX(),e.getY());
                    p = e;
                }
            }
        }
        return p;
    }
    public static int getNumberOfPlayers() {
        int output = 0;
        for (Entity e : main.entities) {
            if (e instanceof Player && e.health > 0) output++;
        }
        return output;
    }
    public static int getNumberOfSheep() {
        int output = 0;
        for (Entity e : main.entities) {
            if (e instanceof Sheep && e.health > 0) output++;
        }
        return output;
    }
    public static int getNumberOfWolves() {
        int output = 0;
        for (Entity e : main.entities) {
            if (e instanceof WolfBase && !(e instanceof Sheepskin) && e.health > 0) output++;
        }
        return output;
    }
    public static int getNumberOfNonspecialWolves() {
        int output = 0;
        for (Entity e : main.entities) {
            if (e instanceof WolfBase && !(e instanceof Sheepskin || e instanceof WolfBoss || e instanceof Dartwolf) && e.health > 0) output++;
        }
        return output;
    }
    static boolean inTheWoods(double x, double y, boundingBox b) {
        if (x + b.x2 <= 16*16) {return true;}
        if (y + b.y2 <= 16*16) {return true;}
        if (x + b.x1 >= (main.tileMap.get(0).size()*16)-(16*16)) {return true;}
        if (y + b.y1 >= (main.tileMap.size()*16)-(16*16)) {return true;}
        return false;
    }
}