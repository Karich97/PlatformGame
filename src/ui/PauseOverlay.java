package ui;

import main.Game;
import states.GameState;
import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.UrmButtons.URM_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class PauseOverlay {
    private int bgX, bgY, bgW, bgH, soundX, musicY, sfxY, unpauseX, replayX, menuX, bY, vX, vY;
    private BufferedImage backgroundImg;
    private SoundButton musicButton, sfxButton;
    private UrmButton unpauseB, replayB, menuB;
    private VolumeButton volumeButton;
    private Playing playing;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSondButtons();
        createUrmButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        vX = (int) (309 * SCALE);
        vY = (int) (278 * SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createUrmButtons() {
        unpauseX = (int)(462 * SCALE);
        replayX = (int)(387 * SCALE);
        menuX = (int)(313 * SCALE);
        bY = (int)(325 * SCALE);

        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        menuB = new UrmButton (menuX, bY, URM_SIZE, URM_SIZE, 2);
    }

    private void createSondButtons() {
        soundX = (int)(450 * SCALE);
        musicY = (int)(140 * SCALE);
        sfxY = (int)(186 * SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * SCALE);
        bgH = (int) (backgroundImg.getHeight() * SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * SCALE);
    }

    public void update(){
        //Sound buttons
        musicButton.update();
        sfxButton.update();
        //URM buttons
        menuB.update();
        replayB.update();
        unpauseB.update();
        //Volume slider
        volumeButton.update();
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        //Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);
        //URM buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        //Volume slider
        volumeButton.draw(g);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)){
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)){
            if (musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()){
                playing.resetPaused();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()){
                playing.resetAll();
            }
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()){
                GameState.state = GameState.MENU;
                playing.resetPaused();
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
        menuB.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        menuB.setMouseOver(false);
        volumeButton.setMouseOver(false);
        if (isIn(e, musicButton)){
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
