package input;

import main.GamePanel;
import states.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Objects;

public class MouseInput implements MouseListener, MouseMotionListener {
    private GamePanel gamePanel;

    public MouseInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Objects.requireNonNull(GameState.state) == GameState.PLAYING) {
            gamePanel.getGame().getPlaying().mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePanel.getGame().getPlaying().mousePressed(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePanel.getGame().getPlaying().mouseReleased(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (Objects.requireNonNull(GameState.state) == GameState.PLAYING) {
            gamePanel.getGame().getPlaying().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePanel.getGame().getPlaying().mouseMoved(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mouseMoved(e);
            }
        }
    }
}
