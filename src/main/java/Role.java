// import java.util.*;

public class Role{
    private String roleName;
    private String roleLevel;
    // AreaData area; // uncomment for GUI
    private String line;
    private Boolean occupied;


    // Role(String name, String level, AreaData area, String line) { // uncomment for GUI
    //     this.roleName = name;
    //     this.roleLevel = level;
    //     this.area = area;
    //     this.line = line;
    // }

    Role() {}

    Role(String name, String level, String line) {
        this.roleName = name;
        this.roleLevel = level;
        this.line = line;
        this.occupied = false;
    }

    public String getName() {
        return roleName;
    }

    public String getLevel() {
        return roleLevel;
    }

    public String getLine() {
        return line;
    }

    public Boolean isOccupied() {
        return occupied;
    }

    public void occupy(){
        this.occupied = true;
    }
    
    public void unoccupy(){
        this.occupied = false;
    }
}