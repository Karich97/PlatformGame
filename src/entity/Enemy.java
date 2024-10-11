package entity;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    protected int aniIndex, enemyState, enemyType, aniTick, aniSpeed = 25;
    protected final float gravity = (float) (0.04 * SCALE), walkSpeed = 0.5f * SCALE;
    protected boolean inAir = true;
    protected float fallSpeed, xSpeed;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = TILES_SIZE;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    protected void turnTowardsPlayer(Player player){
        if (player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int) (player.hitbox.y / TILES_SIZE);
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState){
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void updateInAir(int[][] lvlData){
        if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int) (hitbox.y / TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData){
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
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)){
                aniIndex = 0;
                if (enemyState == ATTACK) {
                    enemyState = IDLE;
                }
            }
        }
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
            flipX = width;
            flipW = -1;
        } else {
            walkDir = LEFT;
            flipX = 0;
            flipW = 1;
        }
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
