package entity;

import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity{
    //Movement
    private boolean left, right, jump, moving = false, attacking = false;
    private int[][] lvlData;
    //Animation
    private static final float xDrawOffset = 21 * SCALE, yDrawOffset = 4 * SCALE;
    private BufferedImage[][] animations;
    //StatusBar
    private BufferedImage statusBarImg;
    private static final int statusBarWidth = (int) (192 * SCALE), statusBarHeight = (int) (58 * SCALE), statusBarX = (int) (10 * SCALE), statusBarY = (int) (10 * SCALE);
    private static final int healthBarWidth = (int) (150 * SCALE), healthBarHeight = (int) (4 * SCALE), healthBarXStart = (int) (34 * SCALE), healthBarYStart = (int) (14 * SCALE);
    private static int healthWidth = healthBarWidth;
    //AttackBox
    private boolean attackChecked = false;
    private final Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDL;
        maxHealth = 100;
        currentHealth = maxHealth;
        this.walkSpeed = 0.6f * SCALE;
        loadAnimations();
        initHitBox(20, 27);
        initAttackBox(x, y);
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }

    private void initAttackBox(float x, float y) {
        attackBox = new Rectangle2D.Float(x, y, 20 * SCALE, 20 * SCALE);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData)){
            inAir = true;
        }
    }

    public void update() {
        updateHealthBar();
        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePosition();
        if (moving) {
            checkPotionTouched();
        }
        if (attacking) {
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitBox);
    }

    private void checkAttack() {
        if (!attackChecked && aniIndex == 1) {
            attackChecked = true;
            playing.checkEnemyHit(attackBox);
            playing.checkObjectHit(attackBox);
        }
    }

    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitBox.x + hitBox.width + SCALE * 10;
        } else if (left) {
            attackBox.x = hitBox.x - hitBox.width - SCALE * 10;
        }
        attackBox.y = hitBox.y + 10 * SCALE;
    }

    private void updateHealthBar() {
        healthWidth = (int) (currentHealth / (float) maxHealth * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex],
                (int)(hitBox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitBox.y - yDrawOffset),
                width * flipW, height,
                null);
        drawUi(g);
        if (debug){
            drawAttackBox(g, lvlOffset);
            drawHitBox(g, lvlOffset);
        }
    }

    private void drawUi(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = state;
        state = getCurrentAction();
        if (startAni != state){
            resetTick();
        }
    }

    private int getCurrentAction() {
        int response;
        if (inAir) {
            if (airSpeed < 0){
                response = JUMPING;
            } else {
                response = FALLING;
            }
        } else {
            if (moving){
                response = RUNNING;
            } else {
                response = IDL;
            }
        }
        if (attacking) {
            response = ATTACK;
        }
        return response;
    }

    private void resetTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if (jump){
            jump();
        }
        if (!inAir){
            if ((!left && !right) || (left && right)){
                return;
            }
        }
        float xSpeed = 0;
        if (left){
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right){
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (inAir){
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)){
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                if (airSpeed > 0){
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
            if (!IsEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
        }

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)){
            hitBox.x += xSpeed;
        } else {
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
        }
        moving = true;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)){
            hitBox.x += xSpeed;
            moving = true;
        }
    }

    private void jump() {
        if (!inAir){
            inAir = true;
            airSpeed = jumpSpeed;
        }
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void changeCurrentHealth(int value) {
        currentHealth += value;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        } else {
            if (currentHealth < 0) {
                currentHealth = 0;
                //gameOver();
            }
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void resetAll() {
        resetDirBooleans();
        attacking = false;
        moving = false;
        state = IDL;
        currentHealth = maxHealth;
        hitBox.x = x;
        hitBox.y = y;
        inAir = !IsEntityOnFloor(hitBox, lvlData);
    }

    public void changePower(int bluePotionValue) {
        System.out.println("ADDED Power for " + bluePotionValue + " points");
    }
}
