import java.util.*;

public class CastingOffice{
    int[] dollarCosts = new int[]{4, 10, 18, 28, 40};
    int[] creditCosts = new int[]{5, 10, 15, 20, 25};

    public CastingOffice(){}

    public int getDollarC(int level){
        return dollarCosts[level - 2];
    }

    public int getCreditC(int level){
        return creditCosts[level - 2];
    }
}