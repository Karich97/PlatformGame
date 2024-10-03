package main;

import input.KeyboardInput;
import input.MouseInput;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private int tileWidth = 40, tileHeight = 25, tileSize = 32;
    private int windowWidth = tileWidth * tileSize ,windowHeight = tileHeight * tileSize;
    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        MouseInput mouseInput = new MouseInput(this);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addKeyListener(new KeyboardInput(this));
        setPanelSize();
    }

    public void setPanelSize() {
        Dimension dimension = new Dimension(windowWidth, windowHeight);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    public void updateGame() {}

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
