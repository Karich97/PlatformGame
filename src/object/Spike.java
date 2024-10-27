package object;

public class Spike extends GameObject{

    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitBox(32, 16);
        xDrawOffset = 0;
    }
}
