package main;

import states.GameState;
import states.Menu;
import states.Playing;

import java.awt.*;

public class Game implements Runnable{
    private static final double BILLION = 1000000000.0;
    public static final int DEFAULT_TILE_SIZE = 32;
    public static final float SCALE = 1.3f;
    public static final int TILES_IN_WIDTH = 26, TILES_IN_HIGH = 14;
    public static final int TILES_SIZE = (int) (DEFAULT_TILE_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HIGH;
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread loopThread;
    private int maxFPS = 30, maxUPS = 120;
    private Playing playing;
    private Menu menu;


    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gameWindow  = new GameWindow(gamePanel);

        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop(){
        loopThread = new Thread(this);
        loopThread.start();
    }

    public void update(){
        switch (GameState.state){
            case PLAYING -> { playing.update(); }
            case MENU -> { menu.update(); }
            case QUIT, OPTIONS -> { System.exit(0); }
        }
    }

    public void render(Graphics g){
        switch (GameState.state){
            case PLAYING -> {
                playing.draw(g);
            }
            case MENU -> {
                menu.draw(g);
            }
        }
    }

    @Override
    public void run() {
        double timePerFrame = BILLION / maxFPS, timePerUpdate = BILLION / maxUPS;
        long previousTime = System.nanoTime();
        long now, lastCheck = System.currentTimeMillis();
        int frame = 0, update = 0;
        double deltaU = 0, deltaF = 0;
        long currentTime;

        while (true){
            currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1){
                update();
                update++;
                deltaU--;
            }

            if (deltaF >= 1){
                gamePanel.repaint();
                deltaF--;
                frame++;
            }

//            now = System.currentTimeMillis();
//            if (now - lastCheck >= 1000){
//                lastCheck = now;
//                System.out.println("FPS: " + frame + " | UPS: " + update);
//                frame = 0;
//                update = 0;
//            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING){
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}
