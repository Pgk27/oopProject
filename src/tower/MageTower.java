package tower;
import java.awt.*;

import core.Screen;
import core.Constants;

public class MageTower extends Tower {
    public MageTower(int x, int y, int width, int height) {
        // Truyền thông số: Tầm bắn 240, Sát thương 8, frame bắn 5, ID tháp
        super(x, y, width, height, 240, 8, 5, Constants.MAGE_TOWER); 
        this.shootSound = Screen.sound.spellCast;
    }

    @Override
    protected int getTotalAnimationFrames() {
        return Screen.mageTower != null ? Screen.mageTower.length : 8;
    }

    @Override
    public void draw(Graphics g) {
        if (Screen.mageTower == null || Screen.mageTower.length == 0)
            return; 

        int frame = towerAnimFrame % Screen.mageTower.length;
        drawOriented(g, Screen.mageTower[frame]);
    }
}