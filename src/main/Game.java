package main;

public class Game implements Runnable{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread loopThread;
    private int maxFPS = 120;
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

    @Override
    public void run() {
        double timePerFrame = billion / maxFPS;
        long lastFrame = System.nanoTime();
        long now = lastFrame;
        int frame = 0;
        long lastCheck = System.currentTimeMillis();

        while (true){
            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame){
                gamePanel.repaint();
                lastFrame = now;
                frame++;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frame);
                frame = 0;
            }
        }
    }
}
