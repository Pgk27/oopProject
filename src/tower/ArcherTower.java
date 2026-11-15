package tower;
import java.awt.*;

import core.Screen;
import core.Constants;

public class ArcherTower extends Tower {
    public ArcherTower(int x, int y, int width, int height) {
        // Truyền thông số: Tầm bắn 100, Sát thương 2, frame bắn 6, ID tháp
        super(x, y, width, height, 1000, 1, 5, Constants.ARCHER_TOWER); //check id
    }

    @Override
    protected int getTotalAnimationFrames() {
        return Screen.archerTower != null ? Screen.archerTower.length : 8;
    }

    @Override
    public void draw(Graphics g) {
        if (Screen.archerTower == null || Screen.archerTower.length == 0)
            return; 

        int frame = towerAnimFrame % Screen.archerTower.length;
        drawOriented(g, Screen.archerTower[frame]);
    }
}