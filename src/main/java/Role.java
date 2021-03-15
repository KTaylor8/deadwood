

public class Role{
    private String roleName;
    private String roleLevel;
    private String line;
    private Boolean occupied;
    private AreaData area;

    public Role() {
        
    }

    /**
     * Constructor for the roles
     * @param name
     * @param level
     * @param line
     * @param areaData
     */
    public Role(String name, String level, String line, AreaData areaData) {
        this.roleName = name;
        this.roleLevel = level;
        this.line = line;
        this.occupied = false;
        this.area = areaData;
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

    public AreaData getArea() {
        return area;
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