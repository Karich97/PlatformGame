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
        // Check the pixel below bottomleft and bottomright
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;

        return true;

    }

    private static boolean isSolidBelow(Rectangle2D.Float hitBox, int[][] lvlData) {
        // Проверяем два угла hitBox на наличие твердой поверхности
        return IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData) ||
                IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= GAME_HEIGHT)
            return true;
        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];

        if (value >= 48 || value < 0 || value != 11)
            return true;
        return false;
    }

    /**
     * We just check the bottomleft of the enemy here +/- the xSpeed. We never check bottom right in case the
     * enemy is going to the right. It would be more correct checking the bottomleft for left direction and
     * bottomright for the right direction. But it wont have big effect in the game. The enemy will simply change
     * direction sooner when there is an edge on the right side of the enemy, when its going right.
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        return IsSolid(hitbox.x + xSpeed + hitbox.width / 2, hitbox.y + hitbox.height + 1, lvlData) ;
    }
}
