package utilz;

import java.awt.geom.Rectangle2D;

import static main.Game.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData)){
            if (!IsSolid(x + width, y + height, lvlData)){
                if (!IsSolid(x + width, y, lvlData)){
                    if (!IsSolid(x, y + height, lvlData)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= lvlData[0].length * TILES_SIZE || y < 0 || y >= GAME_HIGH){
            return true;
        }
        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];
        return value != 11;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed){
        int currentTile = (int) (hitBox.x / TILES_SIZE);
        if (xSpeed > 0){
            int tileXPos = currentTile * TILES_SIZE;
            int xOffset = (int) (TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed){
        int currentTile = (int) (hitBox.y / TILES_SIZE);
        if (airSpeed > 0){
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int) (TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        } else {
            return currentTile * TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData){
        if (!IsSolid(hitBox.x,  hitBox.y + hitBox.height + 1, lvlData)) {
            if (!IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }
}
