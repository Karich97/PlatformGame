package object;

import static main.Game.SCALE;
import static utilz.Constants.ObjectConstants.BOX;

public class GameContainer extends GameObject{

    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();

    }

    private void createHitbox() {
        if (objectType == BOX) {
            initHitBox(25, 18);
            xDrawOffset = (int) (7 * SCALE);
            yDrawOffset = (int) (12 * SCALE);
        } else {
            initHitBox(23, 25);
            xDrawOffset = (int) (8 * SCALE);
            yDrawOffset = (int) (5 * SCALE);
        }
    }

    public void update(){
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}
