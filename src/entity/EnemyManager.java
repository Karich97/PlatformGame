package entity;

import level.Level;
import states.Playing;
import utilz.LoadSave;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    private void loadEnemyImgs(){
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for (int j = 0; j < crabbyArr.length; j++) {
            for (int i = 0; i < crabbyArr[j].length; i++) {
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }

    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (Crabby c : crabbies){
            if (c.active) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if (!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int difX) {
        drawCrabs(g, difX);
    }

    private void drawCrabs(Graphics g, int lvlOffset) {
        for (Crabby c : crabbies) {
            if (c.active) {
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
                        (int)(c.hitBox.x) - lvlOffset - CRABBY_DRAW_OFFSET_X + c.flipX,
                        (int)(c.hitBox.y) - CRABBY_DRAW_OFFSET_Y,
                        CRABBY_WIDTH * c.flipW, CRABBY_HEIGHT, null);
                if (c.debug){
                    c.drawAttackBox(g, lvlOffset);
                    c.drawHitBox(g, lvlOffset);
                }
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c: crabbies) {
            if (c.active) {
                if (attackBox.intersects(c.hitBox)) {
                    c.hurt();
                    return;
                }
            }
        }
    }

    public void resetAll() {
        for (Crabby c: crabbies) {
            c.resetEnemy();
        }
    }
}
