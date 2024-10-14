package ui;

import main.Game;
import states.GameState;
import states.Playing;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, Game.GAME_HEIGHT / 6);
        g.drawString("Press ESC to enter Main Menu!", Game.GAME_WIDTH / 2, Game.GAME_HEIGHT / 3);
    }

    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
}
