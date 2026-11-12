package spell;

import java.awt.Graphics;

public class Enhancement extends Spell {
    public static final long DURATION_MS = 30000;
    private static final double DAMAGE_MULTIPLIER = 1.5;

    public Enhancement() {
        super(300, 700, DURATION_MS); // gọi hàm constructor của cha
    }

    public long getEndTime() {// tgian hết buff
        return spawnTime + duration; 
    }

    public int getRemainingSeconds() { //tgian còn lại của buff
        long remaining = Math.max(0, getEndTime() - System.currentTimeMillis());
        return (int) ((remaining + 999) / 1000);
    }

    public double getDamageMultiplier() { // ouput hệ số để nhân với damage con hero
        return isFinished() ? 1.0 : DAMAGE_MULTIPLIER;
    }

    @Override
    public void physic() {
        finished = isFinished();
    }

    @Override
    public void draw(Graphics g) {
        if (isFinished()) return;
        // g.setColor(Color.YELLOW);
        // g.setFont(new Font("Courier New", Font.BOLD, 16));
        // g.drawString("Enhancement: " + getRemainingSeconds() + "s", x, y);
    }

    @Override
    public void destroy() {
        // Buff được tính khi ra đòn, không thay đổi chỉ số gốc của hero.
    }
}
