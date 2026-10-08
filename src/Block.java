import java.awt.*; 

public class Block extends Rectangle {
	int groundID;
	int towerID;
	
	boolean hasDealtDamage = false;
	
	int shotMob = -1; // -1: chưa khóa mục tiêu nào (tránh lỗi quái ở vị trí số 0)
	int targetType = 0; // 0: không có mục tiêu, 1: mobs, 2: mobss, 3: mobsss
	private int lastAirID = -1; 
	public Tower currentTower = null;

	boolean shotingMob1 = false;
	boolean shotingMob2 = false;
	boolean shotingMob3 = false;


	Block(int x, int y, int width, int height, int groundID, int towerID) { // kule menzillerinin oyunun içinde tanımlanması
    // Thêm biến chốt để theo dõi thay đổi ID

		setBounds(x, y, width, height);
		this.groundID = groundID;
		SetTower();
	} 

	boolean isRoad() {
		return groundID >= Value.groundRoad && groundID <= 4;
	}

	void SetTower() {
        // Chỉ tạo tháp mới khi ID thực sự thay đổi
		if (towerID != lastAirID) {
			lastAirID = towerID;
			
			if(towerID == Value.ARCHER_TOWER){
				currentTower = new CacherTower(x, y, width, height);
			}
			else if(towerID == Value.MAGE_TOWER){
				currentTower = new MageTower(x, y, width, height);
			}
			else if(towerID == Value.CANNON_TOWER){
				currentTower = new Canon(x, y, width, height);
			}
			else if(towerID == Value.GOLD_MINER){
				currentTower = new GoldMiner(x, y, width, height);
			} 
            else {
				currentTower = null;
			}
		}
	}

	void draw(Graphics g) {
        SetTower(); // Cập nhật liên tục trong vòng lặp
		
		int groundTile = groundID >= 1 && groundID <= 4 ? Value.groundRoad : groundID;
		g.drawImage(Screen.tileset_ground[groundTile], x, y, width, height, null);
        if (currentTower != null) {
            currentTower.draw(g); 
        } 
		else if (towerID != Value.PLACEHOLDER) {
            g.drawImage(Screen.tileset_air[towerID], x, y, width, height, null);
        }
	}
	
	void physic() {
        SetTower(); // Cập nhật liên tục trong vòng lặp
		
        if (currentTower != null) {
            currentTower.physic();
        }
	}

	void fight(Graphics g) {
		if (currentTower != null) {
            currentTower.fight(g);
        }
	}
}