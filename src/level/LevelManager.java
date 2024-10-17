package level;

import main.Game;
import states.GameState;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.*;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLvls();
    }

    private void buildAllLvls() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage image : allLevels) {
            levels.add(new Level(image));
        }
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
            for (int i = 0; i < levels.get(lvlIndex).getLvlData()[0].length; i++) {
                index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * TILES_SIZE - x, j * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }

    public void update(){}

    public Level getCurrentLvl() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public void loadNextLvl() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("GAME COMPLETED =)");
            GameState.state = GameState.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffsetX(newLevel.getLvlOffset());
    }
}
