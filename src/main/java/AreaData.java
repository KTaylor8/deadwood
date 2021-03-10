// class for Area, so then it's easier to store and access the dimensions
// import java.util.*;

public class AreaData{
    private int x;
    private int y;
    private int h;
    private int w;

    AreaData() {}

    AreaData(String x, String y, String h, String w) {
        this.x = Integer.parseInt(x) + 300;
        this.y = Integer.parseInt(y);
        this.h = Integer.parseInt(h);
        this.w = Integer.parseInt(w);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

}