package entity;

import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.LoadSave.getCrabs;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = getCrabs();
        System.out.println("Amount off crabs = " + crabbies.size());
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
        for (Crabby c : crabbies){
            if (c.active) {
                c.update(lvlData, player);
            }
        }
    }

    public void draw(Graphics g, int difX) {
        drawCrabs(g, difX);
    }

    private void drawCrabs(Graphics g, int difX) {
        for (Crabby c : crabbies) {
            if (c.active) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],
                        (int)(c.hitbox.x) - difX - CRABBY_DRAW_OFFSET_X + c.flipX,
                        (int)(c.hitbox.y) - CRABBY_DRAW_OFFSET_Y,
                        CRABBY_WIDTH * c.flipW, CRABBY_HEIGHT, null);
                //g.drawRect((int) c.hitbox.x - difX, (int) c.hitbox.y, (int) c.hitbox.width, (int) c.hitbox.height); //Crabby hitBox
                //g.drawRect((int) c.attackBox.x - difX, (int) c.attackBox.y, (int) c.attackBox.width, (int) c.attackBox.height);  //Crabby attackBox
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c: crabbies) {
            if (c.active) {
                if (attackBox.intersects(c.hitbox)) {
                    c.hurt(10);
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
