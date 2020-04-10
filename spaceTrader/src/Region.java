public class Region {
    //private String[] specialResources;
    private String name;
    private int size;
    private int uniX;
    private int uniY;
    private int subX;
    private int subY;
    private int techLevel;
    private String government;
    private int policePresence;
    private Market market;

    public Region(String name, int uniX, int uniY, int size) {
        this(name, uniX, uniY, size, 1, "Democratic", 1);
    }

    public Region(String name, int uniX, int uniY, int size, int techLevel, String government,
                int policePresence) {
        this.uniX = uniX;
        this.uniY = uniY;
        this.subX = uniX;
        this.subY = uniY;
        this.name = name;
        this.size = size;
        this.techLevel = techLevel;
        this.government = government;
        this.policePresence = policePresence;
    }

    public boolean getWin() {
        return false;
    }

    public String getName() {
        return name;
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

    public int getUniX() {
        return uniX;
    }

    public int getUniY() {
        return uniY;
    }

    public int getSubX() {
        return subX;
    }

    public int getSubY() {
        return subY;
    }

    public void setSubX(int subX) {
        this.subX = subX;
    }

    public void setSubY(int subY) {
        this.subY = subY;
    }

    public int getPolicePresence() {
        return policePresence;
    }

    public void createMarket() {
        this.market = new Market(techLevel);
    }

    public Market getMarket() {
        return market;
    }

}
