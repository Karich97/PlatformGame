package level;

import entity.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private int lvlTileWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;
    private int[][] lvlData;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calculateOffsets();
        calculatePlayerSpawn();
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calculateOffsets() {
        lvlTileWide = img.getWidth();
        maxTilesOffset = lvlTileWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE * maxTilesOffset / 2;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int getLvlOffset() {
        return maxLevelOffsetX;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
