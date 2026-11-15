package spell;

public class GoldenPickaxe extends TimedBuff {
    public static final long DURATION_MS = 120_000;
    private static final double GOLD_MULTIPLIER = 1.5;

    public GoldenPickaxe() {
        super(0, 0, DURATION_MS, GOLD_MULTIPLIER);
    }

    public double getGoldMultiplier() {
        return getMultiplier();
    }
}
