package spell;

public class Enhancement extends TimedBuff {
    public static final long DURATION_MS = 15_000;
    private static final double DAMAGE_MULTIPLIER = 2.0;

    public Enhancement() {
        super(300, 700, DURATION_MS, DAMAGE_MULTIPLIER);
    }

    public double getDamageMultiplier() {
        return getMultiplier();
    }
}
