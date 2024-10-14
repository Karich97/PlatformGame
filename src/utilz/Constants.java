package utilz;

import static main.Game.SCALE;

public class Constants {
    public static class EnemyConstants {
        public static final int CRABBY = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * SCALE);

        public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * SCALE);
        public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType){
                case CRABBY -> {
                    switch (enemyState) {
                        case IDLE -> {
                            return 9;
                        }
                        case RUNNING -> {
                            return 6;
                        }
                        case ATTACK -> {
                            return 7;
                        }
                        case HIT -> {
                            return 4;
                        }
                        case DEAD -> {
                            return 5;
                        }
                    }
                }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType){
            switch (enemyType){
                case CRABBY -> {
                    return 10;
                }
                default -> {
                    return 1;
                }
            }
        }

        public static int GetEnemyDmg(int enemyType){
            switch (enemyType){
                case CRABBY -> {
                    return 15;
                }
                default -> {
                    return 0;
                }
            }
        }
    }
    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * SCALE);
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * SCALE);
    }
    public static class UI{
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * SCALE);
        }

        public static class UrmButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * SCALE);
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants{
        public static final int IDL = 0;
        public static final int RUNNING = 1;
        public static final int JUMPING = 2;
        public static final int FALLING = 3;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;

        public static int GetSpriteAmount(int typeOfAction){
            return switch (typeOfAction) {
                case DEAD -> 8;
                case RUNNING -> 6;
                case IDL -> 5;
                case HIT -> 4;
                case JUMPING, ATTACK -> 3;
                default -> 1;
            };
        }
    }
}
