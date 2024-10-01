package main;

import javax.swing.*;

public class GameWindow {

    public GameWindow(GamePanel gamePanel) {
        JFrame jFrame = new JFrame();

        jFrame.setSize(gamePanel.setWindowSize());
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.add(gamePanel);

        jFrame.setVisible(true);
    }
}
