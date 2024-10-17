package utilz;

import entity.Crabby;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static main.Game.*;
import static utilz.Constants.EnemyConstants.CRABBY;

public class LoadSave {
    private static BufferedImage img;
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String CRABBY_SPRITE = "crabby_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
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
    public static final String COMPLETED_IMG = "completed_sprite.png";

    public static BufferedImage GetSpriteAtlas(String fileName){

        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try(InputStream ignored = is){
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;
        try {
            assert url != null;
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        }
        File[] files = new File[0];
        if (file != null) {
            files = file.listFiles();
        }
        assert files != null;
        File[] filesSorted = new File[3];
        for (File f : files) {
            switch (f.getName()) {
                case "1.png" -> filesSorted[0] = f;
                case "2.png" -> filesSorted[1] = f;
                case "3.png" -> filesSorted[2] = f;
            }
        }
        BufferedImage[] images = new BufferedImage[filesSorted.length];
        for (int i = 0; i < images.length; i++) {
            try {
                images[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return images;
    }
}
