package spell;

import java.awt.Graphics;

public class FireSword extends Spell {
    public static final long DURATION_MS = 120000;
    private static final double DAMAGE_MULTIPLIER = 1.5;

    public FireSword() {
        super(0, 0, DURATION_MS);
    }

    public int getRemainingSeconds() {
        long remaining = isFinished() ? 0 : Math.max(0, spawnTime + duration - System.currentTimeMillis());
        return (int) ((remaining + 999) / 1000);
    }

    public double getDamageMultiplier() {
        return isFinished() ? 1.0 : DAMAGE_MULTIPLIER;
    }

    @Override
    public void physic() {
        finished = isFinished();
    }

    @Override
    public void draw(Graphics g) {
        // The item button displays the remaining buff time.
    }

    @Override
    public void destroy() {
        finished = true;
    }
}
