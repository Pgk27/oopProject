package spell;

public class FireSword extends TimedBuff {
    public static final long DURATION_MS = 120_000;
    private static final double DAMAGE_MULTIPLIER = 1.5;

    public FireSword() {
        super(0, 0, DURATION_MS, DAMAGE_MULTIPLIER);
    }

    public double getDamageMultiplier() {
        return getMultiplier();
    }
}
