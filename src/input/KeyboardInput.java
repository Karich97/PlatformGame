package input;

import main.GamePanel;
import states.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    private final GamePanel gamePanel;

    public KeyboardInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePanel.getGame().getPlaying().keyPresses(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().keyPresses(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePanel.getGame().getPlaying().keyReleased(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().keyReleased(e);
            }
        }
    }
}
