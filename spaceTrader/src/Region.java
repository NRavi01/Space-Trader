import java.io.PrintStream;

public class Region {
    //private String[] specialResources;
    private int size;
    private int techLevel;
    private String government;
    private int policePresence;
    private Market market;


    public Region(int size) {
        this(size, 1, "Democratic", 1);
    }
    public Region(int size, int techLevel, String government, int policePresence) {
        this.size = size;
        this.techLevel = techLevel;
        this.government = government;
        this.policePresence = policePresence;
    }

    public int getSize() {
        return size;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public String getGovernment() {
        return government;
    }

    public int getPolicePresence() {
        return policePresence;
    }

}
