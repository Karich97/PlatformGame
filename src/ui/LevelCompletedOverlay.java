package ui;

import main.Game;
import states.GameState;
import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utilz.Constants.UI.UrmButtons.URM_SIZE;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int)(330 * SCALE);
        int nextX = (int)(445 * SCALE);
        int y = (int) (195 * SCALE);
        menu = new UrmButton (menuX, y, URM_SIZE, URM_SIZE, 2);
        next = new UrmButton (nextX, y, URM_SIZE, URM_SIZE, 0);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * SCALE);
        bgH = (int) (img.getHeight() * SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * SCALE);
    }

    public void draw(Graphics g){
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update(){
        next.update();
        menu.update();
    }

    private boolean isIn(UrmButton b, MouseEvent e){
        return b.bounds.contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e)){
            menu.setMouseOver(true);
        } else if (isIn(next, e)){
            next.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)){
            if (menu.isMousePressed()){
                playing.resetAll();
                GameState.state = GameState.MENU;
            }
        } else if (isIn(next, e)){
            if (next.isMousePressed()){
                playing.loadNextLvl();
            }
        }
        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)){
            menu.setMousePressed(true);
        } else if (isIn(next, e)){
            next.setMousePressed(true);
        }
    }

    //    public void keyPressed(KeyEvent e){
//        if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
//            playing.resetAll();
//            GameState.state = GameState.MENU;
//        }
//    }
}
