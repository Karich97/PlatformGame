package input;

import main.GamePanel;
import states.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {
    private GamePanel gamePanel;

    public MouseInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.state){
            case PLAYING -> {
                gamePanel.getGame().getPlaying().mouseClicked(e);
            }
            case MENU -> {
                gamePanel.getGame().getMenu().mouseClicked(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
