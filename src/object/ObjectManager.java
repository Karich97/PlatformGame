package object;

import level.Level;
import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();
    }

    public void loadObjects(Level newLevel){
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
    }

    private void loadImages() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];
        for (int j = 0; j < potionImgs.length; j++) {
            for (int i = 0; i < potionImgs[j].length; i++) {
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];
        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }
    }

    public void update(){
        for (Potion p: potions){
            if (p.active){
                p.update();
            }
        }

        for (GameContainer gc: containers){
            if (gc.active){
                gc.update();
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc: containers){
            if (gc.active){
                int type = 0;
                if (gc.getObjectType() == BARREL){
                    type = 1;
                }
                g.drawImage(containerImgs[type][gc.getAniIndex()],
                        (int) gc.getHitBox().x - gc.getXDrawOffset() - xLvlOffset,
                        (int) gc.getHitBox().y - gc.getYDrawOffset(),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p: potions){
            if (p.active){
                g.drawImage(potionImgs[p.getObjectType()][p.getAniIndex()],
                        (int) p.getHitBox().x - p.getXDrawOffset() - xLvlOffset,
                        (int) p.getHitBox().y - p.getYDrawOffset(),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);
            }
        }
    }
}
