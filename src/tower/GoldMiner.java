package tower;
import java.awt.*;

import core.Screen;
import core.Value;

public class GoldMiner extends Tower {
	private static final int GOLD_PER_CYCLE = 4;

    private int goldMineTick = 0; // tichs danhf rieng cho coin

    public GoldMiner(int x, int y, int width, int height) {
        // Truyền thông số: Tầm bắn 70, Sát thương 2, Thời gian khựng 50, ID tháp
        super(x, y, width, height, 0, 0, 2, Value.GOLD_MINER); //check id
        this.animSpeed = 200;
    }

    @Override 
    public void physic(){
        animTick++;
		if (animTick >= animSpeed){
			animFrame++;
			animTick = 0;
		}

        goldMineTick++;
        if (goldMineTick >= 1250) {
            double multiplier = Screen.store == null ? 1.0 : Screen.store.getMineGoldMultiplier();
            Screen.coinage += (int) Math.round(GOLD_PER_CYCLE * multiplier);
            goldMineTick = 0;
        }        
    }

    @Override
    public void draw(Graphics g) {
        if (Screen.cacherTower == null || Screen.cacherTower.length == 0) {
            return; 
        }
        
        // NOTE TO SELF: nhớ quay lại xem lại
        int frame = animFrame % Screen.goldMiner.length;
        Image currentFrame = Screen.goldMiner[frame];
        
        // Tìm tọa độ mục tiêu để lật ảnh
        g.drawImage(currentFrame, x, y, width, height, null);
        
    }

    @Override
    public void fight(Graphics g) {
    }
}
