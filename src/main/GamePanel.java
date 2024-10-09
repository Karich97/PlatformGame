package main;

import input.KeyboardInput;
import input.MouseInput;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
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
        Dimension dimension = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
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
