package utilz;

import java.awt.geom.Rectangle2D;

import static main.Game.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // Проверяем все четыре угла на наличие твердой поверхности
        return !isSolid(x, y, lvlData) &&
                !isSolid(x + width, y, lvlData) &&
                !isSolid(x, y + height, lvlData) &&
                !isSolid(x + width, y + height, lvlData);
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

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        // Проверяем, находится ли объект на уровне
        return isSolidBelow(hitBox, lvlData);
    }

    private static boolean isSolidBelow(Rectangle2D.Float hitBox, int[][] lvlData) {
        // Проверяем два угла hitBox на наличие твердой поверхности
        return isSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData) ||
                isSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        // Проверяем, выходит ли координата за пределы уровня
        if (x < 0 || x >= lvlData[0].length * TILES_SIZE || y < 0 || y >= GAME_HIGH) {
            return true; // За пределами уровня считается твердой поверхностью
        }

        // Получаем индексы плитки
        int xIndex = (int) (x / TILES_SIZE);
        int yIndex = (int) (y / TILES_SIZE);

        // Проверяем значение уровня
        return lvlData[yIndex][xIndex] != 11;
    }
}
