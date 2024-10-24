package object;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex, xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objectType)){
                aniIndex = 0;
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset(){
        aniTick = 0;
        aniIndex = 0;
        active = true;
        doAnimation = objectType != BARREL && objectType != BOX;
    }

    protected void initHitBox(float width, float height) {
        hitBox = new Rectangle2D.Float(x, y, width * SCALE, height * SCALE);
    }

    protected void drawHitBox(Graphics g, int difX){
        g.setColor(Color.pink);
        g.drawRect((int) hitBox.x - difX,(int) hitBox.y,(int) hitBox.width,(int) hitBox.height);
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public int getXDrawOffset() {
        return xDrawOffset;
    }

    public int getYDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}
