package main;

import input.KeyboardInput;
import input.MouseInput;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private int xDelta = 100, yDelta = 100;
    private int xDir = 1, yDir = 1;
    private final int windowWidth = 400;
    private final int windowHeight = 400;
    private int barWidth = 200;
    private int barHeight = 50;

    public GamePanel() {
        KeyboardInput keyboardInput = new KeyboardInput(this);
        MouseInput mouseInput = new MouseInput(this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addKeyListener(keyboardInput);
    }

    public void setXDelta(Integer xDelta) {
        this.xDelta += xDelta;
    }

    public void setYDelta(Integer yDelta) {
        this.yDelta += yDelta;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        updateRectangle();
        g.fillRect(xDelta, yDelta, barWidth, barHeight);
    }

    private void updateRectangle() {
        xDelta += xDir;
        yDelta += yDir;
        if (xDelta > windowWidth - barWidth || xDelta <= 0){
            xDir *= -1;
        }
        if (yDelta >= windowHeight - barHeight || yDelta <= 0){
            yDir *= -1;
        }
    }

    public Dimension setWindowSize() {
        return new Dimension(windowWidth, windowHeight);
    }
}
