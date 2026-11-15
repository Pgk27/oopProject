package spell;

public class CursedSkull extends TimedBuff {
    public static final long DURATION_MS = 120_000;
    private static final double SPEED_MULTIPLIER = 0.7;

    public CursedSkull() {
        super(0, 0, DURATION_MS, SPEED_MULTIPLIER);
    }

    public double getSpeedMultiplier() {
        return getMultiplier();
    }
}
