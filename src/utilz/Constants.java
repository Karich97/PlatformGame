package utilz;

public class Constants {
    public static class Directions {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        public static final int BACKWARD = -1;
        public static final int IDL = 0;
        public static final int FORWARD = 1;
        public static final int JUMPING = 2;
        public static final int FALLING = 3;
        public static final int HIT = 4;
        public static final int GROUND = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static int GetSpriteAmount(int typeOfAction){
            return switch (typeOfAction) {
                case IDL -> 5;
                case BACKWARD, FORWARD -> 6;
                case HIT -> 4;
                case JUMPING, ATTACK_1, ATTACK_JUMP_1, ATTACK_JUMP_2 -> 3;
                case FALLING -> 2;
                default -> 1;
            };
        }
    }
}
