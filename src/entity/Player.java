package entity;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.CanMoveHere;

public class Player extends Entity{
    private static final int aniSpeed = 15;
    private static final float speed = 2 * SCALE;
    private static float xSpeed = 0, ySpeed = 0;
    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0;
    private boolean left, right, up, down;
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
        initHitBox(x, y, 20 * SCALE, 28 * SCALE);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int)(hitBox.x - xDrawOffset), (int) (hitBox.y - yDrawOffset), width, height, null);
        drawHitBox(g);
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
        if (moving){
            playerAction = FORWARD;
        } else {
            playerAction = IDL;
        }
        if (attacking) {
            playerAction = ATTACK_1;
        }
        if (startAni != playerAction){
            resetTick();
        }
    }

    private void resetTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if (!left && !right && !up && !down){
            return;
        }
        xSpeed = 0;
        ySpeed = 0;
        if (left && !right){
            xSpeed = -speed;
        } if (right && !left){
            xSpeed = speed;
        } if (down && !up){
            ySpeed = speed;
        } if (up && !down){
            ySpeed = -speed;
        }
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

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
