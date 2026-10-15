import java.awt.*; 

public class Block extends Rectangle {
	int groundID;
	int towerID;
	public int rootX = -1;
	public int rootY = -1;
	
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
				currentTower = new CacherTower(x, y, width * 2, height * 2);
			}
			else if(towerID == Value.MAGE_TOWER){
				currentTower = new MageTower(x, y, width * 2, height * 2);
			}
			else if(towerID == Value.CANNON_TOWER){
				currentTower = new Canon(x, y, width * 2, height * 2);
			}
			else if(towerID == Value.GOLD_MINER){
				currentTower = new GoldMiner(x, y, width * 2, height * 2);
			} 
            else {
				currentTower = null;
			}
		}
	}

	void drawGround(Graphics g) {
		int groundTile = groundID >= 1 && groundID <= 4 ? Value.groundRoad : groundID;
		g.drawImage(Screen.tileset_ground[groundTile], x, y, width, height, null);
	}

	void drawTower(Graphics g) {
        SetTower(); // Cập nhật liên tục trong vòng lặp
        if (currentTower != null) {
            currentTower.draw(g); 
        } 
<<<<<<< HEAD
		else if (towerID != Value.PLACEHOLDER && towerID != Value.TOWER_OCCUPIED && towerID != Value.SPELL_BLACK_HOLE && towerID >= 0 && towerID < Screen.tileset_air.length) {
=======
		else if (towerID != Value.PLACEHOLDER && towerID != Value.TOWER_OCCUPIED && towerID >= 0) {
>>>>>>> 50fcaccc3db1501ebec44cd89acd1658bfd8c0a6
            g.drawImage(Screen.tileset_air[towerID], x, y, width, height, null);
        }
	}

	void draw(Graphics g) {
		drawGround(g);
		drawTower(g);
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