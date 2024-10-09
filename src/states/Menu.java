package states;

import static main.Game.*;
import static states.GameState.*;
import input.StateMethods;
import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods {
    private BufferedImage backgroundImg;
    private final BufferedImage backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;

    private final MenuButton[] buttons = new MenuButton[3];
    public Menu(Game game) {
        super(game);
        loadBackground();
        loadButtons();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * SCALE);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(GAME_WIDTH / 2, (int) (150 * SCALE), 0, PLAYING);
        buttons[1] = new MenuButton(GAME_WIDTH / 2, (int) (220 * SCALE), 1, OPTIONS);
        buttons[2] = new MenuButton(GAME_WIDTH / 2, (int) (290 * SCALE), 2, QUIT);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink,0,0,GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed()){
                    mb.applyGameState();
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons){
            mb.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons){
            if (isIn(e, mb)){
                mb.setMouseOver(true);
                break;
            }
            mb.setMouseOver(false);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPresses(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
