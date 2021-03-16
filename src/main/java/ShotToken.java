

public class ShotToken {
    private int shotNum;
    private AreaData area;

    /**
     * Constructor for the Shot Tokens
     * @param shotNum
     * @param area
    */
    public ShotToken(int shotNum, AreaData area) {
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