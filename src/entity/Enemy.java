package entity;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    protected int enemyType;
    protected boolean attackChecked = false;
    protected boolean active = true;
    protected float attackDistance = TILES_SIZE;
    protected int walkDir = LEFT, tileY;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        this.walkSpeed = 0.5f * SCALE;
    }

    protected void turnTowardsPlayer(Player player){
        if (player.hitBox.x > hitBox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int) (player.hitBox.y / TILES_SIZE);
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                return IsSightClear(lvlData, hitBox, player.hitBox, tileY);
            }
        }
        return false;
    }

    private boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void updateInAir(int[][] lvlData){
        if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
            tileY = (int) (hitBox.y / TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))
            if (IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)){
                aniIndex = 0;
                switch (state){
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
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

    protected void hurt(){
        currentHealth -= 10;
        if (currentHealth <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox)) {
            player.changeCurrentHealth(-GetEnemyDmg(enemyType));
        }
        attackChecked = true;
    }

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        inAir = true;
    }
}
