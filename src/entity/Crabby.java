package entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static utilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    //AttackBox
    protected Rectangle2D.Float attackBox;
    private int attackBoxOffset;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, 22 * SCALE, 19 * SCALE);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 82 * SCALE, 19 * SCALE);
        attackBoxOffset = (int) (30 * SCALE);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffset;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    if (canSeePlayer(lvlData, player)){
                        if (isPlayerCloseForAttack(player)){
                            newState(ATTACK);
                        } else {
                            turnTowardsPlayer(player);
                        }
                    }
                    move(lvlData);
                }
                case ATTACK -> {
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 3 && !attackChecked) {
                        checkPlayerHit(attackBox, player);
                    }
                }
            }
        }
    }
}
