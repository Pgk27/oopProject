import java.awt.*; //Chịu trách nhiệm về logic bắn và chiến đấu của tháp

public class Block extends Rectangle {
	Rectangle towerSquare;
	Rectangle towerSquare2; // 2.kule
	Rectangle towerSquare3; // 3.kule
	Rectangle towerSquare4;
	int goldMineTick = 0; // tichs danhf rieng cho coin
	int goldMinerAnimFrame = 0;

	int towerSquareSize = 130;   //tower range, ne kadar uzağa vurabilir.
	int towerSquareSize2 = 130;  //2. kule menzil
	int towerSquareSize3 = 200;  //3. kule menzil
	int towerSquareSize4 = 2000000; // set cho xôm
	int groundID;
	int airID;
	int loseTime = 100, loseFrame = 0; // hasar vurma aralığı, kulelerin
	
	int shotMob = -1; // -1: chưa khóa mục tiêu nào (tránh lỗi quái ở vị trí số 0)
	int targetType = 0; // 0: không có mục tiêu, 1: mobs, 2: mobss, 3: mobsss
	boolean shotingMob1 = false;
	boolean shotingMob2 = false;
	boolean shotingMob3 = false;

	Block(int x, int y, int width, int height, int groundID, int airID) { // kule menzillerinin oyunun içinde tanımlanması
		setBounds(x, y, width, height);
		towerSquare = new Rectangle(x - (towerSquareSize / 2), y - (towerSquareSize / 2), width + towerSquareSize, height + towerSquareSize);
		towerSquare2 = new Rectangle(x - (towerSquareSize2 / 2), y - (towerSquareSize2 / 2), width + towerSquareSize2, height + towerSquareSize2);
		towerSquare3 = new Rectangle(x - (towerSquareSize3 / 2), y - (towerSquareSize3 / 2), width + towerSquareSize3, height + towerSquareSize3);
		towerSquare4 = new Rectangle(x - (towerSquareSize4 / 2), y - (towerSquareSize4 / 2), width + towerSquareSize4, height + towerSquareSize4);
		this.groundID = groundID;
		this.airID = airID;
	} 

	// Lấy phạm vi bắn tương ứng với loại tháp hiện tại
	Rectangle getTowerRange() {
		if (airID == Value.airTowerLaser2) return towerSquare2;
		if (airID == Value.airTowerLaser3) return towerSquare3;
		if (airID == Value.airTowerLaser4) return towerSquare4;
		return towerSquare;
	}

	void draw(Graphics g) {
		g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);

		if (airID != Value.airAir) {
			Image[] towerFrames;
			if (airID == Value.airTowerLaser2) {
				towerFrames = Screen.mageTower;
			} 
			else if (airID == Value.airTowerLaser) {
				towerFrames = Screen.cacherTower;
			}
			else if (airID == Value.airTowerLaser4) {
				towerFrames = Screen.goldMiner;
			}
			else if (airID == Value.airTowerLaser3) {
				towerFrames = Screen.cannon;
			}
			else {
				g.drawImage(Screen.tileset_air[airID], x, y, width, height, null);
				return;
			}

			int animationFrame = 0;
			if (shotingMob1 || shotingMob2 || shotingMob3) {
				animationFrame = Screen.AnimFrame % towerFrames.length;
			}
			else if (towerFrames == Screen.goldMiner) {
				if (Screen.AnimTick % 120003 == 0) {
					goldMinerAnimFrame = (goldMinerAnimFrame + 1) % towerFrames.length;
				}
				animationFrame = goldMinerAnimFrame;
			}

			Image currentFrame = towerFrames[animationFrame];
            
			// Tìm tọa độ X của quái đang bị ngắm bắn để lật hình ảnh
			int targetX = this.x;
			if (shotMob != -1) {
				if (shotingMob1 && shotMob < Screen.mobs.length) targetX = Screen.mobs[shotMob].x;
				else if (shotingMob2 && shotMob < Screen.mobss.length) targetX = Screen.mobss[shotMob].x;
				else if (shotingMob3 && shotMob < Screen.mobsss.length) targetX = Screen.mobsss[shotMob].x;
			}
            
			if (targetX < this.x && towerFrames != Screen.goldMiner) {
				// Quái ở bên TRÁI -> Lật ảnh
				g.drawImage(currentFrame, x + width, y, -width, height, null);
			} else {
				// Quái ở bên PHẢI -> Vẽ bình thường
				g.drawImage(currentFrame, x, y, width, height, null);
			}
		}
	}
	
	void physic() {
		// Logic kiếm vàng từ mỏ vàng
		if (airID == Value.airTowerLaser4) {
			goldMineTick++;
			if (goldMineTick >= 1250) {
				Screen.coinage += 1;
				goldMineTick = 0;
			}
			return;
		}

		// Chỉ các tháp bắn mới xử lý tấn công
		if (airID != Value.airTowerLaser && airID != Value.airTowerLaser2 && airID != Value.airTowerLaser3) {
			return;
		}

		Rectangle range = getTowerRange();

		// 1. Kiểm tra mục tiêu hiện tại: Nếu ra ngoài phạm vi hoặc đã chết thì hủy ngắm
		if (targetType != 0 && shotMob != -1) {
			Mob currentTarget = null;
			if (targetType == 1 && shotMob < Screen.mobs.length) currentTarget = Screen.mobs[shotMob];
			else if (targetType == 2 && shotMob < Screen.mobss.length) currentTarget = Screen.mobss[shotMob];
			else if (targetType == 3 && shotMob < Screen.mobsss.length) currentTarget = Screen.mobsss[shotMob];

			if (currentTarget == null || !currentTarget.inGame || currentTarget.isDead() || !range.intersects(currentTarget)) {
				targetType = 0;
				shotMob = -1;
				shotingMob1 = false;
				shotingMob2 = false;
				shotingMob3 = false;
			}
		}

		// 2. Nếu chưa có mục tiêu, tìm đúng 1 mục tiêu duy nhất trong tầm bắn
		if (targetType == 0) {
			// Quét nhóm mobs 1
			for (int i = 0; i < Screen.mobs.length; i++) {
				if (Screen.mobs[i].inGame && !Screen.mobs[i].isDead() && range.intersects(Screen.mobs[i])) {
					targetType = 1;
					shotMob = i;
					shotingMob1 = true;
					break;
				}
			}

			// Nếu nhóm 1 không có, quét tiếp nhóm mobss 2
			if (targetType == 0) {
				for (int i = 0; i < Screen.mobss.length; i++) {
					if (Screen.mobss[i].inGame && !Screen.mobss[i].isDead() && range.intersects(Screen.mobss[i])) {
						targetType = 2;
						shotMob = i;
						shotingMob2 = true;
						break;
					}
				}
			}

			// Nếu nhóm 2 không có, quét tiếp nhóm mobsss 3
			if (targetType == 0) {
				for (int i = 0; i < Screen.mobsss.length; i++) {
					if (Screen.mobsss[i].inGame && !Screen.mobsss[i].isDead() && range.intersects(Screen.mobsss[i])) {
						targetType = 3;
						shotMob = i;
						shotingMob3 = true;
						break;
					}
				}
			}
		}

		// 3. Tiến hành bắn và trừ máu mục tiêu đã khóa
		if (targetType != 0 && shotMob != -1) {
			Mob target = null;
			if (targetType == 1 && shotMob < Screen.mobs.length) target = Screen.mobs[shotMob];
			else if (targetType == 2 && shotMob < Screen.mobss.length) target = Screen.mobss[shotMob];
			else if (targetType == 3 && shotMob < Screen.mobsss.length) target = Screen.mobsss[shotMob];

			if (loseFrame >= loseTime) {
				int damage = 2;
				if (airID == Value.airTowerLaser2) damage = 4;
				else if (airID == Value.airTowerLaser3) damage = 10;

				if (target != null) {
					target.loseHealth(damage);
					if (target.isDead()) {
						targetType = 0;
						shotMob = -1;
						shotingMob1 = false;
						shotingMob2 = false;
						shotingMob3 = false;
						Screen.hasWon();
					}
				}
				loseFrame = 0;
			} else {
				loseFrame++;
			}
		}
	}
	
	void getMoney(int mobID) {
		if (mobID >= 0 && mobID < Value.deathReward.length)
			Screen.coinage += Value.deathReward[mobID];
	}
	
	void fight(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (Screen.isDebug) {
			if (airID == Value.airTowerLaser) {
				g2d.drawRect(towerSquare.x, towerSquare.y, towerSquare.width, towerSquare.height);
			}
			if (airID == Value.airTowerLaser2) {
				g2d.drawRect(towerSquare2.x, towerSquare2.y, towerSquare2.width, towerSquare2.height);
			}
			if (airID == Value.airTowerLaser3) {
				g2d.drawRect(towerSquare3.x, towerSquare3.y, towerSquare3.width, towerSquare3.height);
			}
		}

		if (shotMob == -1) return;

		// MOB1
		if (shotingMob1 && shotMob < Screen.mobs.length) {
			if (airID == Value.airTowerLaser) g2d.setColor(new Color(255, 255, 0));
			else if (airID == Value.airTowerLaser2) g2d.setColor(new Color(0, 153, 0));
			else if (airID == Value.airTowerLaser3) g2d.setColor(new Color(51, 153, 255));

			g2d.drawLine(x + (width / 2), y + (height / 2),
					Screen.mobs[shotMob].x + (Screen.mobs[shotMob].width / 2),
					Screen.mobs[shotMob].y + (Screen.mobs[shotMob].height / 2));
		}
		
		// MOB2
		else if (shotingMob2 && shotMob < Screen.mobss.length) {
			if (airID == Value.airTowerLaser) g2d.setColor(new Color(255, 255, 0));
			else if (airID == Value.airTowerLaser2) g2d.setColor(new Color(0, 153, 0));
			else if (airID == Value.airTowerLaser3) g2d.setColor(new Color(51, 153, 255));

			g2d.drawLine(x + (width / 2), y + (height / 2),
					Screen.mobss[shotMob].x + (Screen.mobss[shotMob].width / 2),
					Screen.mobss[shotMob].y + (Screen.mobss[shotMob].height / 2));
		}
		
		// MOB3
		else if (shotingMob3 && shotMob < Screen.mobsss.length) {
			if (airID == Value.airTowerLaser) g2d.setColor(new Color(255, 255, 0));
			else if (airID == Value.airTowerLaser2) g2d.setColor(new Color(0, 153, 0));
			else if (airID == Value.airTowerLaser3) g2d.setColor(new Color(51, 153, 255));

			g2d.drawLine(x + (width / 2), y + (height / 2),
					Screen.mobsss[shotMob].x + (Screen.mobsss[shotMob].width / 2),
					Screen.mobsss[shotMob].y + (Screen.mobsss[shotMob].height / 2));
		}
	}
}