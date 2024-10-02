package main;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread loopThread;
    private int maxFPS = 120, maxUPS = 120;
    private final double billion = 1000000000.0;

    public Game() {
        gamePanel = new GamePanel();
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        gameWindow  = new GameWindow(gamePanel);
        startGameLoop();
    }

    private void startGameLoop(){
        loopThread = new Thread(this);
        loopThread.start();
    }

    public void update(){
        gamePanel.updateGame();
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
}
