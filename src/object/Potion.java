package object;

import static main.Game.SCALE;

public class Potion extends GameObject{

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitBox(7, 14);
        xDrawOffset = (int) (3 * SCALE);
        yDrawOffset = (int) (2 * SCALE);
    }

    public void update(){
        updateAnimationTick();
    }
}
