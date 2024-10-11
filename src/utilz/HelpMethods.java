package utilz;

import java.awt.geom.Rectangle2D;

import static main.Game.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // Проверяем все четыре угла на наличие твердой поверхности
        return !IsSolid(x, y, lvlData) &&
                !IsSolid(x + width, y, lvlData) &&
                !IsSolid(x, y + height, lvlData) &&
                !IsSolid(x + width, y + height, lvlData);
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

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / TILES_SIZE);
        if (airSpeed > 0) {
            // Falling - touching floor
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int) (TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else
            // Jumping
            return currentTile * TILES_SIZE;

    }


    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
     //    Check the pixel below bottomleft and bottomright
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 3, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 3, lvlData)) {
                return false;
            }
        return true;
    }

    private static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < Math.abs(xStart - xEnd); i++) {
            if (IsTileSolid(xEnd + i, y, lvlData)) {
                return false;
            }
            if (!IsTileSolid(xEnd + i, y + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox,
                                       Rectangle2D.Float secondHitbox, int tileY) {
        int firstXTile = (int) (firstHitbox.x / TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / TILES_SIZE);
        return IsAllTilesWalkable(firstXTile, secondXTile,tileY, lvlData);
    }

    private static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        if (xTile >= lvlData[0].length) {
            xTile = lvlData[0].length - 1;
        }
        if (yTile >= lvlData.length) {
            yTile = lvlData.length;
        }
        int value = lvlData[yTile][xTile];
        return value != 11;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * TILES_SIZE;
        if (x < 0 || x >= maxWidth || y < 0 || y >= GAME_HEIGHT) {
            return true;
        } else {
            int xIndex = (int) (x / TILES_SIZE);
            int yIndex = (int) (y / TILES_SIZE);
            return IsTileSolid(xIndex, yIndex, lvlData);
        }
    }

    /**
     * We just check the bottommiddle of the enemy here +/- the xSpeed. We never check bottom right in case the
     * enemy is going to the right. It would be more correct checking the bottomleft for left direction and
     * bottomright for the right direction. But it wont have big effect in the game. The enemy will simply change
     * direction sooner when there is an edge on the right side of the enemy, when its going right.
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        return IsSolid(hitbox.x + hitbox.width / 2 + xSpeed, hitbox.y + hitbox.height + 3, lvlData) ;
    }
}
