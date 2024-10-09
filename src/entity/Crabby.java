package entity;

import static main.Game.SCALE;
import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, 22 * SCALE, 19 * SCALE);
    }
}
