package states;

import entity.EnemyManager;
import entity.Player;
import input.StateMethods;
import level.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.Game.*;
import static utilz.Constants.Environment.*;

public class Playing extends State implements StateMethods {
    private final BufferedImage bufferedImg, bigCloud, smallCloud;
    private final int[] smallCloudPos;
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false, gameOver = false, levelCompleted;
    private int maxLvlOffsetX;
    private int difX;

    public Playing(Game game) {
        super(game);
        initClasses();
        bufferedImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos = new int[10];
        for (int i = 0; i < smallCloudPos.length; i++) {
            Random rnd = new Random();
            smallCloudPos[i] = (int) (90 * SCALE) + rnd.nextInt((int) (100 * SCALE));
        }
        calculatingLvlOffset();
        loadStartLvl();
    }

    public void loadNextLvl(){
        resetAll();
        levelManager.loadNextLvl();
        player.setSpawn(levelManager.getCurrentLvl().getPlayerSpawn());
    }

    private void loadStartLvl() {
        enemyManager.loadEnemies(levelManager.getCurrentLvl());
    }

    private void calculatingLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLvl().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player((int) (GAME_WIDTH * 0.2), (int) (GAME_HEIGHT * 0.3), (int) (64 * SCALE), (int) (40 * SCALE), this);
        player.loadLvlData(levelManager.getCurrentLvl().getLvlData());
        player.setSpawn(levelManager.getCurrentLvl().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted){
            levelCompletedOverlay.update();
        } else if (!gameOver) {
            levelManager.update();
            player.update();
            checkCloseBorder();
            enemyManager.update(levelManager.getCurrentLvl().getLvlData(), player);
        }
    }

    private void checkCloseBorder() {
        int maxX = (levelManager.getCurrentLvl().getLvlData()[0].length - TILES_IN_WIDTH) * TILES_SIZE;
        int playerX = (int) player.getHitbox().x;
        difX = playerX - maxLvlOffsetX;
        if (difX < 0) {
            difX = 0;
        } else if (difX > maxX) {
            difX = maxX;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bufferedImg, 0,0,GAME_WIDTH, GAME_HEIGHT, null);
        drawClouds(g);
        drawSmallClouds(g);
        levelManager.draw(g, difX);
        player.render(g, difX);
        enemyManager.draw(g, difX);
        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,GAME_WIDTH, GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (levelCompleted) {
            levelCompletedOverlay.draw(g);
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
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused){
            pauseOverlay.mousePressed(e);
        } else if (levelCompleted) {
            levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused){
            pauseOverlay.mouseReleased(e);
        } else if (levelCompleted) {
            levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused){
            pauseOverlay.mouseMoved(e);
        } else if (levelCompleted) {
            levelCompletedOverlay.mouseMoved(e);
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
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()){
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE:
                    resetPaused();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void resetPaused() {
        paused = !paused;
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        levelCompleted = false;
        player.resetAll();
        enemyManager.resetAll();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void setMaxLvlOffsetX(int lvlOffsetX) {
        this.maxLvlOffsetX = lvlOffsetX;
    }

    public void setLevelCompleted(boolean b) {
        this.levelCompleted = b;
    }
}
