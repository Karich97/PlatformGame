package utilz;

import entity.Crabby;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static main.Game.*;
import static utilz.Constants.EnemyConstants.CRABBY;

public class LoadSave {
    private static BufferedImage img;
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String CRABBY_SPRITE = "crabby_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    //public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String STATUS_BAR = "health_power_bar.png";

    public static BufferedImage GetSpriteAtlas(String fileName){

        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try(InputStream ignored = is){
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static ArrayList<Crabby> getCrabs(){
        img = GetSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Crabby> list = new ArrayList<>();
        Color color;
        int value;
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                color = new Color(img.getRGB(i, j));
                value = color.getGreen();
                if (value == CRABBY){
                    list.add(new Crabby(i * TILES_SIZE, j * TILES_SIZE));
                }
            }
        }
        return list;
    }

    public static int[][] GetLevelData(){
        img = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        Color color;
        int value;
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                color = new Color(img.getRGB(i, j));
                value = color.getRed();
                if (value >= 48){
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
