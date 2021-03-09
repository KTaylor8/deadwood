// import java.util.*;

public class ShotToken {
    private int shotNum;
    private int x;
    private int y;
    private int w;
    private int h;

    ShotToken(int shotNum, int x, int y, int w, int h) {
        this.shotNum = shotNum;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getShotNum() {
        return shotNum;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
} 