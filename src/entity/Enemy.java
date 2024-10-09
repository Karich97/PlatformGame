package entity;

import static utilz.Constants.EnemyConstants.GetSpriteAmount;

public abstract class Enemy extends Entity {
    private int aniIndex, enemyState, enemyType, aniTick, aniSpeed = 25;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)){
                aniIndex = 0;
            }
        }
    }

    public void update() {
        updateAnimationTick();
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
