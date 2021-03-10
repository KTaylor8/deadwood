// import java.util.*;

public class ShotToken {
    private int shotNum;
    private AreaData area;
    // ShotToken(int shotNum, int x, int y, int w, int h) {
    //     this.shotNum = shotNum;
    //     area = new AreaData((x-300) + "", y + "", w + "", h + "");
    // }

    ShotToken(int shotNum, AreaData area) {
        this.shotNum = shotNum;
        this.area = area;
    }

    public AreaData getArea(){
        return area;
    }

    public int getShotNum() {
        return shotNum;
    }


} 