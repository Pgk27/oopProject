package spell;
import java.awt.*;

import core.Screen;
import core.Value;
import entity.mob.Mob;

public class BlackHole extends Spell {
    private int width;
    private int height;
    private int rootX;
    private int rootY;
    private static final int FRAME_DELAY_MS = 80; // 80ms đổi 1 frame ảnh xoay

    public BlackHole(int x, int y, int rootX, int rootY) {
        super(x, y, 5000); // Tồn tại đúng 5000ms = 5.0 giây
        int bSize = (Screen.room != null) ? Screen.room.blockSize : 30;
        this.width = bSize * 2;  // 2x2 ô = 60px
        this.height = bSize * 2; // 2x2 ô = 60px
        this.rootX = rootX;
        this.rootY = rootY;
    }

    @Override
    public void physic() {
        if (isFinished()) return;

        Rectangle hitBox = new Rectangle(x, y, width, height);

        for (int i = 0; i < Screen.mobs.length; i++) {
            if (Screen.mobs[i] != null && Screen.mobs[i].inGame && !Screen.mobs[i].isDead() && hitBox.intersects(Screen.mobs[i])) {
                swallow(Screen.mobs[i]);
            }
        }
        for (int i = 0; i < Screen.mobss.length; i++) {
            if (Screen.mobss[i] != null && Screen.mobss[i].inGame && !Screen.mobss[i].isDead() && hitBox.intersects(Screen.mobss[i])) {
                swallow(Screen.mobss[i]);
            }
        }
        for (int i = 0; i < Screen.mobsss.length; i++) {
            if (Screen.mobsss[i] != null && Screen.mobsss[i].inGame && !Screen.mobsss[i].isDead() && hitBox.intersects(Screen.mobsss[i])) {
                swallow(Screen.mobsss[i]);
            }
        }
    }

    private void swallow(Mob m) {
        m.deleteMob();   // Quái biến mất ngay lập tức
        Screen.killed++; // Tăng điểm hạ gục
    }

    @Override
    public void destroy() {
        // Trả lại 4 ô đường về trạng thái trống
        for (int dy = 0; dy < 2; dy++) {
            for (int dx = 0; dx < 2; dx++) {
                int ny = rootY + dy;
                int nx = rootX + dx;
                if (ny >= 0 && ny < Screen.room.worldHeight && nx >= 0 && nx < Screen.room.worldWidth) {
                    Screen.room.block[ny][nx].towerID = Value.PLACEHOLDER;
                    Screen.room.block[ny][nx].rootX = -1;
                    Screen.room.block[ny][nx].rootY = -1;
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (Screen.blackHole == null || Screen.blackHole.length == 0) return;

        // Tính frame xoay trực tiếp theo thời gian thực (80ms/frame), cực kỳ mượt mà
        int currentFrame = (int) ((System.currentTimeMillis() / FRAME_DELAY_MS) % Screen.blackHole.length);
        Image img = Screen.blackHole[currentFrame];
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }
}
