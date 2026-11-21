package spell;

public class GoldenPickaxe extends TimedBuff {
    public static final long DURATION_MS = 15_000;
    public static final double GOLD_MULTIPLIER = 1.5;

    public GoldenPickaxe() {
        super(0, 0, DURATION_MS, GOLD_MULTIPLIER);
    }

    public double getGoldMultiplier() {
        return getMultiplier();
    }
}
