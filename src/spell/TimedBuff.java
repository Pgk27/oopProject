package spell;

import java.awt.Graphics;

/**
 * Lop co so cho cac buff co thoi han (FireSword, GoldenPickaxe, CursedSkull, Enhancement).
 * Dong goi toan bo logic dem nguoc thoi gian, kiem tra ket thuc va he so nhan.
 */
public abstract class TimedBuff extends Spell {
    protected final double multiplier;

    public TimedBuff(int x, int y, long durationMs, double multiplier) {
        super(x, y, durationMs);
        this.multiplier = multiplier;
    }

    public int getRemainingSeconds() {
        long remaining = isFinished() ? 0 : Math.max(0, spawnTime + duration - System.currentTimeMillis());
        return (int) ((remaining + 999) / 1000);
    }

    public long getEndTime() {
        return spawnTime + duration;
    }

    public double getMultiplier() {
        return isFinished() ? 1.0 : multiplier;
    }

    @Override
    public void physic() {
        finished = isFinished();
    }

    @Override
    public void draw(Graphics g) {
        // Mac dinh hien thi thoi gian tren HUD / button
    }

    @Override
    public void destroy() {
        finished = true;
    }
}
