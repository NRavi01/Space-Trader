public class SpecialRegion extends Region {
    private boolean winningRegion = true;

    public SpecialRegion(String name, int uniX, int uniY, int size, int techLevel,
            String government, int policePresence) {
        super(name, uniX, uniY, size, techLevel, government, policePresence);
    }

    public boolean getWin() {
        return true;
    }
}
