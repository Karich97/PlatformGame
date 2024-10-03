package main;

import entity.Player;

import java.awt.*;

public class Game implements Runnable{
    private static final double billion = 1000000000.0;
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread loopThread;
    private int maxFPS = 120, maxUPS = 120;
    private Player player;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gameWindow  = new GameWindow(gamePanel);

        startGameLoop();
    }

    private void initClasses() {
        player = new Player(200, 200);
    }

    private void startGameLoop(){
        loopThread = new Thread(this);
        loopThread.start();
    }

    public void update(){
        player.update();
    }

    public void render(Graphics g){
        player.render(g);
    }

    @Override
    public void run() {
        double timePerFrame = billion / maxFPS, timePerUpdate = billion / maxUPS;
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

            now = System.currentTimeMillis();
            if (now - lastCheck >= 1000){
                lastCheck = now;
                System.out.println("FPS: " + frame + " | UPS: " + update);
                frame = 0;
                update = 0;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }
}
