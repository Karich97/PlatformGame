package states;

import entity.EnemyManager;
import entity.Player;
import input.StateMethods;
import level.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.Game.*;
import static utilz.Constants.Environment.*;

public class Playing extends State implements StateMethods {
    private final int rightBorder = (int)(TILES_IN_WIDTH * TILES_SIZE * 0.5);
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    private int difX, maxX, playerX;
    private BufferedImage bufferedImg, bigCloud, smallCloud;
    private int[] smallCloudPos;
    private Random rnd =new Random();

    public Playing(Game game) {
        super(game);
        initClasses();
        bufferedImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos = new int[10];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (90 * SCALE) + rnd.nextInt((int) (100 * SCALE));
        }
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player((int) (GAME_WIDTH * 0.2), (int) (GAME_HIGH * 0.3), (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLvlData(levelManager.getLvlOne().getLvlData());
        pauseOverlay = new PauseOverlay(this);
    }

    public void windowFocusLost() {
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else {
            levelManager.update();
            player.update();
            checkCloseBorder();
            enemyManager.update();
        }
    }

    private void checkCloseBorder() {
        maxX = (levelManager.getLvlOne().getLvlData()[0].length - TILES_IN_WIDTH) * TILES_SIZE;
        playerX = (int) player.getHitBox().x;
        difX = playerX - rightBorder;
        if (difX < 0) {
            difX = 0;
        } else if (difX > maxX) {
            difX = maxX;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bufferedImg, 0,0,GAME_WIDTH, GAME_HIGH, null);
        drawClouds(g);
        drawSmallClouds(g);
        levelManager.draw(g, difX);
        player.render(g, difX);
        enemyManager.draw(g, difX);
        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,GAME_WIDTH, GAME_HIGH);
            pauseOverlay.draw(g);
        }
    }

    private void drawSmallClouds(Graphics g) {
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(smallCloud, 4 * i * SMALL_CLOUD_WIDTH - (int) (difX * 0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (difX * 0.3), (int) (204 * SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1){
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused){
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused){
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused){
            pauseOverlay.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (paused){
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPresses(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_S:
                player.setDown(true);
                break;
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_W:
                player.setUp(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE:
                resetPaused();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_S:
                player.setDown(false);
                break;
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_W:
                player.setUp(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public void resetPaused() {
        paused = !paused;
    }
}
