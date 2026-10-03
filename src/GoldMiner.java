import java.awt.*;

public class GoldMiner extends Tower {

    private int goldMineTick = 0; // tichs danhf rieng cho coin
	private int goldMinerAnimFrame = 0;

    public GoldMiner(int x, int y, int width, int height) {
        // Truyền thông số: Tầm bắn 70, Sát thương 2, Thời gian khựng 50, ID tháp
        super(x, y, width, height, 70, 2, 50, Value.airTowerLaser); //check id
    }

    @Override 
    public void physic(){
        goldMineTick++;
        if (goldMineTick >= 1250) {
            Screen.coinage += 10;
            goldMineTick = 0;
        }        
    }

    @Override
    public void draw(Graphics g) {
        if (Screen.cacherTower == null || Screen.cacherTower.length == 0) {
            return; 
        }

        if (Screen.AnimTick % 120003 == 0) {
            goldMinerAnimFrame = (goldMinerAnimFrame + 1) % Screen.goldMiner.length;
        }
        Image currentFrame = Screen.goldMiner[goldMinerAnimFrame];
        
        // Tìm tọa độ mục tiêu để lật ảnh
        g.drawImage(currentFrame, x + width, y, -width, height, null);
        
    }

    @Override
    public void fight(Graphics g) {
    }
}