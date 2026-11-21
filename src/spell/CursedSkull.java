package spell;

public class CursedSkull extends TimedBuff {
    public static final long DURATION_MS = 15_000;
    public static final double SPEED_MULTIPLIER = 0.5; // the lower, the slower

    public CursedSkull() {
        super(0, 0, DURATION_MS, SPEED_MULTIPLIER);
    }

    public double getSpeedMultiplier() {
        return getMultiplier();
    }
}
