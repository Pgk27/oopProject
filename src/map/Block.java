package map;
import java.awt.*;

import core.Screen;
import core.Constants;
import tower.ArcherTower;
import tower.Cannon;
import tower.GoldMiner;
import tower.MageTower;
import tower.Tower; 

public class Block extends Rectangle {
	public int groundID;
	public int towerID;
	public int rootX = -1;
	public int rootY = -1;
	
	boolean hasDealtDamage = false;
	
	private int lastAirID = -1; 
	public Tower currentTower = null;

	Block(int x, int y, int width, int height, int groundID, int towerID) { // kule menzillerinin oyunun içinde tanımlanması
    // Thêm biến chốt để theo dõi thay đổi ID

		setBounds(x, y, width, height);
		this.groundID = groundID;
		SetTower();
	} 

	public boolean isRoad() {
		return groundID >= Constants.groundRoad && groundID <= 4;
	}

	void SetTower() {
        // Chỉ tạo tháp mới khi ID thực sự thay đổi
		if (towerID != lastAirID) {
			lastAirID = towerID;
			
			if(towerID == Constants.ARCHER_TOWER){
				currentTower = new ArcherTower(x, y, width * 2, height * 2);
			}
			else if(towerID == Constants.MAGE_TOWER){
				currentTower = new MageTower(x, y, width * 2, height * 2);
			}
			else if(towerID == Constants.CANNON_TOWER){
				currentTower = new Cannon(x, y, width * 2, height * 2);
			}
			else if(towerID == Constants.GOLD_MINER){
				currentTower = new GoldMiner(x, y, width * 2, height * 2);
			} 
            else {
				currentTower = null;
			}
		}
	}

	// void drawGround(Graphics g) {
	// 	int groundTile = groundID >= 1 && groundID <= 4 ? Value.groundRoad : groundID;
	// 	g.drawImage(Screen.tileset_ground[groundTile], x, y, width, height, null);

	// 	// g.drawImage(Screen.tiles.m1, 0, 0, 1200, 600, null);
	// }

	

	void drawTower(Graphics g) {
        SetTower(); // Cập nhật liên tục trong vòng lặp
        if (currentTower != null) {
            currentTower.draw(g); 
        } 
		else if (towerID != Constants.PLACEHOLDER && towerID != Constants.TOWER_OCCUPIED && towerID != Constants.SPELL_BLACK_HOLE && towerID >= 0 && towerID < Screen.tileset_air.length) {
            g.drawImage(Screen.tileset_air[towerID], x, y, width, height, null);
        }
	}

	void draw(Graphics g) {
		// drawGround(g);
		drawTower(g);
	}
	
	void physic() {
        SetTower(); // Cập nhật liên tục trong vòng lặp
		
        if (currentTower != null) {
            currentTower.physic();
        }
	}
}