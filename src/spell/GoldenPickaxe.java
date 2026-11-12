package spell;

import java.awt.Graphics;

public class GoldenPickaxe extends Spell {
    public static final long DURATION_MS = 120000;
    private static final double GOLD_MULTIPLIER = 1.5;

    public GoldenPickaxe() {
        super(0, 0, DURATION_MS);
    }

    public int getRemainingSeconds() {
        long remaining = Math.max(0, spawnTime + duration - System.currentTimeMillis());
        return (int) ((remaining + 999) / 1000);
    }

    public double getGoldMultiplier() {
        return isFinished() ? 1.0 : GOLD_MULTIPLIER;
    }

    @Override
    public void physic() {
        finished = isFinished();
    }

    @Override
    public void draw(Graphics g) {
        // Thời gian buff được hiển thị trên nút item.
    }

    @Override
    public void destroy() {
        // Không chiếm ô và không thay đổi lượng vàng gốc của mỏ.
    }
}
