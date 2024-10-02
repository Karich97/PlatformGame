package main;

import input.KeyboardInput;
import input.MouseInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

public class GamePanel extends JPanel {

    private int xDelta = 100, yDelta = 100;
    private int tileWidth = 40, tileHeight = 25, tileSize = 32;
    private int windowWidth = tileWidth * tileSize ,windowHeight = tileHeight * tileSize;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0, aniSpeed = 15;
    private int playerAction = IDL, playerDir = -1;
    private boolean moving = false;

    public GamePanel() {
        importImg();
        loadAnimations();
        MouseInput mouseInput = new MouseInput(this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addKeyListener(new KeyboardInput(this));
        setPanelSize();
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try(InputStream ignored = is){
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDirection(int direction){
        this.playerDir = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
            }
        }
    }

    public void setPanelSize() {
        Dimension dimension = new Dimension(windowWidth, windowHeight);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    private void setAnimation() {
        if (moving){
            playerAction = FORWARD;
        } else {
            playerAction = IDL;
        }
    }

    private void updatePosition() {
        if (moving){
            switch (playerDir){
                case LEFT -> xDelta -= 5;
                case RIGHT -> xDelta += 5;
                case UP -> yDelta -= 5;
                case DOWN -> yDelta += 5;
            }
        } else {
            playerAction = IDL;
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(animations[playerAction][aniIndex], xDelta, yDelta, tileSize*8, 160, null);
    }

    public void updateGame() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }
}
