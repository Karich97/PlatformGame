package entity;

import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity{
    //Movement
    private static final float speed = 0.5f * SCALE, gravity = 0.04f * SCALE, jumpSpeed = -2.25f * SCALE, fallSpeedAfterCollision = 0.5f * SCALE;
    private static float airSpeed = 0f;
    private boolean left, right, jump, inAir = true, moving = false, attacking = false;
    private int playerAction = IDL;
    private int[][] lvlData;
    //Animation
    private static final int aniSpeed = (int) (25 / SCALE);
    private static final float xDrawOffset = 21 * SCALE, yDrawOffset = 4 * SCALE;
    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0;
    //StatusBar
    private BufferedImage statusBarImg;
    private static final int statusBarWidth = (int) (192 * SCALE), statusBarHeight = (int) (58 * SCALE), statusBarX = (int) (10 * SCALE), statusBarY = (int) (10 * SCALE);
    private static final int healthBarWidth = (int) (150 * SCALE), healthBarHeight = (int) (4 * SCALE), healthBarXStart = (int) (34 * SCALE), healthBarYStart = (int) (14 * SCALE);
    private static final int maxHealth = 100;
    private static int currentHealth = maxHealth, healthWidth = healthBarWidth;
    //AttackBox
    private static Rectangle2D.Float attackBox;
    private boolean attackChecked = false;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitBox(x, y, 20 * SCALE, 27 * SCALE);
        initAttackBox(x, y);
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
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
        if (!IsEntityOnFloor(hitbox, lvlData)){
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
        if (attacking) {
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if (!attackChecked || aniIndex == 1) {
            attackChecked = true;
            playing.checkEnemyHit(attackBox);
        }
    }

    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitbox.x + hitbox.width + SCALE * 10;
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - SCALE * 10;
        }
        attackBox.y = hitbox.y + 10 * SCALE;
    }

    private void updateHealthBar() {
        healthWidth = (int) (currentHealth / (float) maxHealth * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex],
                (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset),
                width * flipW, height,
                null);

        drawUi(g);
        //drawHitBox(g, lvlOffset); //Player hitBox
        //drawAttackBox(g, lvlOffset);
    }

    private void drawAttackBox(Graphics g, int lvlOffset) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.BLUE);
        //g.drawRect((int) attackBox.x - lvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUi(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;
        playerAction = getCurrentAction();
        if (startAni != playerAction){
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
            xSpeed -= speed;
            flipX = width;
            flipW = -1;
        }
        if (right){
            xSpeed += speed;
            flipX = 0;
            flipW = 1;
        }
        if (inAir){
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0){
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
        moving = true;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            hitbox.x += xSpeed;
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
        playerAction = IDL;
        currentHealth = maxHealth;
        hitbox.x = x;
        hitbox.y = y;
        inAir = !IsEntityOnFloor(hitbox, lvlData);
    }
}
