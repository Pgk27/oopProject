package spell;

public class FireSword extends TimedBuff {
    public static final long DURATION_MS = 15_000;
    public static final double DAMAGE_MULTIPLIER = 1.5;

    public FireSword() {
        super(0, 0, DURATION_MS, DAMAGE_MULTIPLIER);
    }

    public double getDamageMultiplier() {
        return getMultiplier();
    }
}
