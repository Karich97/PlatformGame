package entity;

import states.Playing;
import utilz.LoadSave;

import java.awt.*;
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

    public void update() {
        for (Crabby c : crabbies){
            c.update();
        }
    }

    public void draw(Graphics g, int difX) {
        drawCrabs(g, difX);
    }

    private void drawCrabs(Graphics g, int difX) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int)(c.hitBox.x) - difX, (int)(c.hitBox.y), CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }
}
