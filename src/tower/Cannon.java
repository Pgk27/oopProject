package tower;
import java.awt.*;

import core.Screen;
import core.Constants;

public class Cannon extends Tower {
    public Cannon(int x, int y, int width, int height) {
        // Truyền thông số: Tầm bắn 160, Sát thương 20, frame bắn: 2, ID tháp
        super(x, y, width, height, 160, 20, 2, Constants.CANNON_TOWER); //check id
        this.shootSound = Screen.sound.boomShot;
    }

    @Override
    protected int getTotalAnimationFrames() {
        return Screen.cannon != null ? Screen.cannon.length : 8;
    }

    @Override
    public void draw(Graphics g) {
        if (Screen.cannon == null || Screen.cannon.length == 0)
            return; 

        int frame = towerAnimFrame % Screen.cannon.length;
        drawOriented(g, Screen.cannon[frame]);
    }
}