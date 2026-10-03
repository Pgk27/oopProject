import java.awt.*; 

public class Block extends Rectangle {
	int groundID;
	int airID;
	
    // Thêm biến chốt để theo dõi thay đổi ID
	private int lastAirID = -1; 
	public Tower currentTower = null;
	
	Block(int x, int y, int width, int height, int groundID, int airID) { 
		setBounds(x, y, width, height);
		this.groundID = groundID;
		this.airID = airID;
		SetTower();
	} 

	void SetTower() {
        // Chỉ tạo tháp mới khi ID thực sự thay đổi
		if (airID != lastAirID) {
			lastAirID = airID;
			
			if(airID == Value.airTowerLaser){
				currentTower = new CacherTower(x, y, width, height);
			}
			else if(airID == Value.airTowerLaser2){
				currentTower = new MageTower(x, y, width, height);
			}
			else if(airID == Value.airTowerLaser3){
				currentTower = new Canon(x, y, width, height);
			}
			else if(airID == Value.airTowerLaser4){
				currentTower = new GoldMiner(x, y, width, height);
			} 
            else {
				currentTower = null;
			}
		}
	}

	void draw(Graphics g) {
        SetTower(); // Cập nhật liên tục trong vòng lặp
		
		g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);
        if (currentTower != null) {
            currentTower.draw(g); 
        } 
		else if (airID != Value.airAir) {
            g.drawImage(Screen.tileset_air[airID], x, y, width, height, null);
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