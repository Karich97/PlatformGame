package entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width = 0, height = 0, flipX = 0, flipW = 1;
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // For debugging
    protected void drawHitBox(Graphics g, int difX){
        g.setColor(Color.pink);
        g.drawRect((int) hitbox.x - difX,(int) hitbox.y,(int) hitbox.width,(int) hitbox.height);
    }

    protected void initHitBox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
