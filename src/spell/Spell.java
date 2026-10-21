package spell;
import java.awt.Graphics;

/**
 * Lớp cha trừu tượng cho tất cả các Kỹ năng / Phép thuật hỗ trợ của người chơi
 * (Hố Đen, Máy bay ném bom, Bão băng...)
 */
public abstract class Spell {
    protected int x;
    protected int y;
    protected long spawnTime;
    protected long duration; // mili-giây
    protected boolean finished = false;

    public Spell(int x, int y, long duration) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.spawnTime = System.currentTimeMillis();
    }

    public boolean isFinished() {
        if (finished) return true;
        if (duration > 0 && (System.currentTimeMillis() - spawnTime >= duration)) {
            return true;
        }
        return false;
    }

    public abstract void physic();
    public abstract void draw(Graphics g);
    public abstract void destroy();
}

