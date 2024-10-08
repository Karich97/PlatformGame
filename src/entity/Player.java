package entity;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity{
    private static final int aniSpeed = (int) (25 / SCALE);
    private static final float speed = 0.5f * SCALE;
    private static float xSpeed = 0, ySpeed = 0, airSpeed = 0f, gravity = 0.04f * SCALE, jumpSpeed = -2.25f * SCALE, fallSpeedAfterCollision = 0.5f * SCALE;
    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0;
    private boolean left, right, up, down, jump, inAir = true;
    private int playerAction = IDL;
    private boolean moving = false, attacking = false;
    private int[][] lvlData;
    private float xDrawOffset = 21 * SCALE, yDrawOffset = 4 * SCALE;

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, 20 * SCALE, 27 * SCALE);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, int x) {
        g.drawImage(animations[playerAction][aniIndex], (int)(hitBox.x - xDrawOffset) - x, (int) (hitBox.y - yDrawOffset), width, height, null);
       //drawHitBox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
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
                response = FORWARD;
            } else {
                response = IDL;
            }
        }
        if (attacking) {
            response = ATTACK_1;
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
        xSpeed = 0;
        if (left){
            xSpeed -= speed;
        }
        if (right){
            xSpeed += speed;
        }
        if (inAir){
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)){
                hitBox.y += airSpeed;
                airSpeed += gravity;
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

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y + ySpeed, hitBox.width, hitBox.height, lvlData)){
            hitBox.x += xSpeed;
        } else {
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
        }
        moving = true;
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

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y + ySpeed, hitBox.width, hitBox.height, lvlData)){
            hitBox.x += xSpeed;
            hitBox.y += ySpeed;
            moving = true;
        }
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData)){
            inAir = true;
        }
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
