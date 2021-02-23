import java.util.*;

public class Role{
    String name;
    String level;
    // AreaData area; // uncomment for GUI
    String line;
    Boolean occupied;


    // Role(String name, String level, AreaData area, String line) { // uncomment for GUI
    //     this.name = name;
    //     this.level = level;
    //     this.area = area;
    //     this.line = line;
    // }

    Role() {}

    Role(String name, String level, String line) {
        this.name = name;
        this.level = level;
        this.line = line;
        this.occupied = false;
    }

    public void occupy(){
        this.occupied = true;
    }
    
    public void unoccupy(){
        this.occupied = false;
    }
}