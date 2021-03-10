// import java.util.*;

public class ShotToken {
    private int shotNum;
    private AreaData area;
    ShotToken(int shotNum, int x, int y, int w, int h) {
        this.shotNum = shotNum;
        area = new AreaData((x-300) + "", y + "", w + "", h + "");
        
    }

    public AreaData getArea(){
        return area;
    }

    public int getShotNum() {
        return shotNum;
    }


} 