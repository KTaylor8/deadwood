

public class ShotToken {
    private int shotNum;
    private AreaData area;

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