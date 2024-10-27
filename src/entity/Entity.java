package entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;

public abstract class Entity {
    protected float x, y;
    protected float jumpSpeed = -2.25f * SCALE, airSpeed = 0f;
    protected float walkSpeed, fallSpeedAfterCollision = 0.5f * SCALE;
    protected boolean inAir = true, debug = false;
    protected int width, height, flipX = 0, flipW = 1;
    protected Rectangle2D.Float hitBox;
    protected Rectangle2D.Float attackBox;
    protected int maxHealth, currentHealth;
    protected int aniTick = 0, aniIndex = 0;
    protected int state;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(float width, float height) {
        hitBox = new Rectangle2D.Float(x, y, width * SCALE, height * SCALE);
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    protected int getState() {
        return state;
    }

    protected int getAniIndex() {
        return aniIndex;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    // For debugging
    protected void drawHitBox(Graphics g, int difX){
        g.setColor(Color.pink);
        g.drawRect((int) hitBox.x - difX,(int) hitBox.y,(int) hitBox.width,(int) hitBox.height);
    }

    protected void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.BLUE);
        g.drawRect((int) attackBox.x - lvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }
}
