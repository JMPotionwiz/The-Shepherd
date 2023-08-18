package entities;

import core.main;

public abstract class WolfBase extends Entity {
    protected boolean PANIC = false, attacked = false, stopHoming = false;
    protected Entity target;
    public WolfBase(double x, double y) {
        super(x,y);
        idleSounds = new String[] {"sfx_bark1","sfx_bark2","sfx_bark3"};
        hurtSound = "sfx_hurtwolf";
        deathSound = "sfx_wolfdeath";
    }
    public void underAttack(Entity a) {
        super.underAttack(a);
        this.PANIC = true;
        this.targetXY[0] = (a.getX() - this.x) * -1;
        this.targetXY[1] = (a.getY() - this.y) * -1;
        if (this.health <= 0) {main.score += 10;}
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
        this.target = tool.getRandomPlayerOrSheep();
    }
    public void retarget(Entity t) {
        this.PANIC = false;
        this.attacked = false;
        this.stopHoming = false;
        this.target = t;
    }
}
