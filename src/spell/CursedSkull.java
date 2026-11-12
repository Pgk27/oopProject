package spell;

import java.awt.Graphics;

public class CursedSkull extends Spell {
    public static final long DURATION_MS = 120000;
    private static final double SPEED_MULTIPLIER = 0.7;

    public CursedSkull() {
        super(0, 0, DURATION_MS);
    }

    public int getRemainingSeconds() {
        long remaining = isFinished() ? 0 : Math.max(0, spawnTime + duration - System.currentTimeMillis());
        return (int) ((remaining + 999) / 1000);
    }

    public double getSpeedMultiplier() {
        return isFinished() ? 1.0 : SPEED_MULTIPLIER;
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
