package entity;

import static main.Game.SCALE;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    private int aniIndex, enemyState, enemyType, aniTick, aniSpeed = 25;
    private final float gravity = (float) (0.04 * SCALE), walkSpeed = 0.5f * SCALE;
    private boolean firstUpdate = true, inAir;
    private float fallSpeed, xSpeed;
    private int walkDir = LEFT;

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

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
            firstUpdate = false;
        }

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;

                    if (walkDir == LEFT)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;

                    if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
                        if (IsFloor(hitbox, xSpeed, lvlData)) {
                            hitbox.x += xSpeed;
                            return;
                        }

                    changeWalkDir();

                    break;
            }
        }

    }

//    private void updateMove(int[][] lvlData){
//        if (firstUpdate){
//            if (!IsEntityOnFloor(hitBox, lvlData)){
//                inAir = true;
//            }
//            firstUpdate = false;
//        } else {
//            if (inAir) {
//                if (CanMoveHere(hitBox.x, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
//                    hitBox.y += fallSpeed;
//                    fallSpeed += gravity;
//                } else {
//                    inAir = false;
//                    hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
//                }
//            } else {
//                switch (enemyState) {
//                    case IDLE -> {
//                        enemyState = RUNNING;
//                    }
//                    case RUNNING -> {
//                        if (walkDir == LEFT) {
//                            xSpeed = -walkSpeed;
//                        } else {
//                            xSpeed = walkSpeed;
//                        }
//                        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)){
//                            if (IsFloor(hitBox, xSpeed, lvlData)){
//                                hitBox.x += xSpeed;
//                                return;
//                            }
//                        } else {
//                            changeWalkDirection();
//                        }
//                    }
//                }
//            }
//        }
//    }

    private void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
