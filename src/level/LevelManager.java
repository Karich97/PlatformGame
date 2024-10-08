package level;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.*;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level lvlOne;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        lvlOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        levelSprite = new BufferedImage[48];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j * DEFAULT_TILE_SIZE, i * DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
            }
        }
            
    }

    public void draw(Graphics g, int x){
        int index;
        for (int j = 0; j <TILES_IN_HIGH; j++) {
            for (int i = 0; i < 50; i++) {
                index = lvlOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * TILES_SIZE - x, j * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    public void update(){}

    public Level getLvlOne() {
        return lvlOne;
    }
}
