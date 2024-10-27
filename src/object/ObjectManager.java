package object;

import entity.Player;
import level.Level;
import states.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;
import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();
    }

    public void checkSpikesTouched(Player player){
        for (Spike s: spikes){
            if (s.hitBox.intersects(player.getHitBox())){
                player.changeCurrentHealth(-player.getMaxHealth());
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitBox){
        for (Potion p : potions){
            if (p.active && hitBox.intersects(p.hitBox)){
                applyEffectToPlayer(p);
                p.setActive(false);
            }
        }
    }

    public void applyEffectToPlayer(Potion p){
        if (p.active && p.getObjectType() == RED_POTION){
            System.out.println("APPLIED!!!");
            playing.getPlayer().changeCurrentHealth(RED_POTION_VALUE);
        } else playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox){
        for (GameContainer gc : containers) {
            if (gc.active && !gc.doAnimation && gc.hitBox.intersects(attackBox)){
                gc.setDoAnimation(true);
                int type = 0;
                if (gc.getObjectType() == BARREL) {
                    type = 1;
                }
                potions.add(new Potion(
                        (int) (gc.getHitBox().x + gc.getHitBox().width / 2),
                        (int) (gc.getHitBox().y - (float) TILES_SIZE / 3),
                        type));
                return;
            }
        }
    }

    public void loadObjects(Level newLevel){
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
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

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
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
        drawTraps(g, xLvlOffset);
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s: spikes) {
            g.drawImage(spikeImg, (int) (s.getHitBox().x - xLvlOffset), (int) s.getHitBox().y, SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
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

    public void resetAll() {
        loadObjects(playing.getLevelManager().getCurrentLvl());
        for (Potion p: potions){
            p.reset();
        }
        for (GameContainer gc: containers){
            gc.reset();
        }
    }
}
