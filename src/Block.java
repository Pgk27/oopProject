
import java.awt.*; //Chịu trách nhiệm về logic bắn và chiến đấu của tháp

public class Block extends Rectangle{
	Rectangle towerSquare;
	Rectangle towerSquare2; // 2.kule
	Rectangle towerSquare3; // 3.kule
	
	int towerSquareSize = 130;   //tower range, ne kadar uzağa vurabilir.
	int towerSquareSize2= 130;   //2. kule menzil
	int towerSquareSize3= 200;  //3. kule menzil
	int groundID;
	int airID;
	int loseTime = 100, loseFrame = 0; // hasar vurma aralığı, kulelerin
	
	int shotMob = 0;// kulelerin alanındaki mobları vurması için
	boolean shotingMob1 = false ;
	boolean shotingMob2 = false ;   //kulelerin moblara vurması için gereken kod
	boolean shotingMob3 = false ;
	

	Block(int x, int y, int width, int height, int groundID, int airID) { // kule menzillerinin oyunun içinde tanımlanması
		
		setBounds(x,y,width, height);
		towerSquare = new Rectangle(x-(towerSquareSize/2), y-(towerSquareSize/2), width+(towerSquareSize), height+(towerSquareSize));
		towerSquare2 = new Rectangle(x-(towerSquareSize2/2), y-(towerSquareSize2/2), width+(towerSquareSize2), height+(towerSquareSize2));//2. kule
		towerSquare3 = new Rectangle(x-(towerSquareSize3/2), y-(towerSquareSize3/2), width+(towerSquareSize3), height+(towerSquareSize3));//2. kule 
		this.groundID = groundID;
		this.airID = airID;
	} 
	
	// void draw(Graphics g) { // tileset air ve ground  içindeki png leri koyuyor..
	// 	g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);
		
	// 	if(airID != Value.airAir) {
	// 		g.drawImage(Screen.tileset_air[airID], x, y, width, height, null);
	// 	}
	// }

	void draw(Graphics g) {
		g.drawImage(Screen.tileset_ground[groundID], x, y, width, height, null);

		if (airID != Value.airAir) {
			int animationFrame = 0;
			if(shotingMob1 || shotingMob2 || shotingMob3){
				animationFrame = Screen.AnimFrame % Screen.cacherTower.length;
			}
			// do là hàm draw sẽ vẽ lại mỗi lần nên mình không cần vòng lặp để reset lại 

			Image currentFrame = Screen.cacherTower[animationFrame];
            
            // 2. Tìm tọa độ X của con quái vật đang bị nhắm bắn
            int targetX = this.x; // Khởi tạo mặc định
            if (shotingMob1) targetX = Screen.mobs[shotMob].x;       //[cite: 1]
            else if (shotingMob2) targetX = Screen.mobss[shotMob].x; //[cite: 1]
            else if (shotingMob3) targetX = Screen.mobsss[shotMob].x;//[cite: 1]
            
            // 3. So sánh tọa độ để vẽ ảnh 
            if (targetX < this.x) {
                // Quái ở bên TRÁI -> Lật ảnh bằng cách cộng width vào X và đặt width thành số âm
                g.drawImage(currentFrame, x + width, y, -width, height, null);
            } else {
                // Quái ở bên PHẢI -> Vẽ bình thường
                g.drawImage(currentFrame, x, y, width, height, null);
            }
		}
	}
	
	void physic() { // physics for tower shooting and mob taking damage
		
		if (shotMob != 0 && towerSquare.intersects(Screen.mobs[shotMob])   ) { // mob 1 get shot, check xem co de len nhau k
			shotingMob1 = true;
		}
		else if(shotMob != 0 && towerSquare.intersects(Screen.mobss[shotMob])) { // mob 2 get shot
			shotingMob2 = true;
		}
		else if(shotMob != 0 && towerSquare.intersects(Screen.mobsss[shotMob])) {
			shotingMob3 = true;
		}
		else {
			shotingMob1 = false;
			shotingMob2 = false;
			shotingMob3 = false;
		}
		
		if (!shotingMob1) { // burası towerların kendi menzillerine girene kadar beklemesini sağlıyor 
			if (airID == Value.airTowerLaser || airID == Value.airTowerLaser2 || airID == Value.airTowerLaser3)  { // başka tower için ya da ile ekle
				for (int i = 0; i < Screen.mobs.length; i++) {
					if (Screen.mobs[i].inGame) {
						if (towerSquare.intersects(Screen.mobs[i])) { //çerçevenin içerisinde vurmasını sağlıyor
							shotingMob1 = true;
							shotMob = i;
						}
					}
				}
			}
		}
		
		if (shotingMob1) {
			if (loseFrame>=loseTime) {// her kule için ayrı değerimiz var  
				if(airID == Value.airTowerLaser) {
					Screen.mobs[shotMob].loseHealth(2); // mobun ne kadar canı gidiyor
				}
				else if(airID == Value.airTowerLaser2) {
					Screen.mobs[shotMob].loseHealth(4); // mobun ne kadar canı gidiyor
				}
				else if(airID == Value.airTowerLaser3) {
					Screen.mobs[shotMob].loseHealth(10); // mobun ne kadar canı gidiyor
				}
				loseFrame = 0;
			} 
			else {
				loseFrame += 1;
			}

			if (Screen.mobs[shotMob].isDead()) {
				shotingMob1 = false;
				shotMob = 0;

				Screen.hasWon();
			}
		}
		
		
		 
		if (!shotingMob2) {
			if (airID == Value.airTowerLaser ||airID == Value.airTowerLaser2||airID == Value.airTowerLaser3 )  { // başka tower için ya da ile ekle
				for (int i = 0; i < Screen.mobss.length; i++) {
					if (Screen.mobss[i].inGame) {
						if (towerSquare.intersects(Screen.mobss[i]) ) {
							shotingMob2 = true;
							shotMob = i;
						}

					}
				}
			}
		}

		if (shotingMob2) {
			if (loseFrame >= loseTime) {
				if(airID == Value.airTowerLaser) {
					Screen.mobss[shotMob].loseHealth(2); // mobun ne kadar canı gidiyor
				}
				else if(airID == Value.airTowerLaser2) {
					Screen.mobss[shotMob].loseHealth(4); // mobun ne kadar canı gidiyor
				}
				else if(airID == Value.airTowerLaser3) {
					Screen.mobss[shotMob].loseHealth(10); // mobun ne kadar canı gidiyor
				}
				loseFrame = 0;
			} 
			else {
				loseFrame += 1;
			}

			if (Screen.mobss[shotMob].isDead()) {
				shotingMob2 = false;
				shotMob = 0;
				Screen.hasWon();
			}
		}    
		
		if (!shotingMob3) {
			if (airID == Value.airTowerLaser ||airID == Value.airTowerLaser2||airID == Value.airTowerLaser3 )  { // başka tower için ya da ile ekle
				for (int i = 0; i < Screen.mobsss.length; i++) {
					if (Screen.mobsss[i].inGame) {
						if (towerSquare.intersects(Screen.mobsss[i])) {
							shotingMob3 = true;
							shotMob = i;
						}
					}
				}
			}
		}
		
		
		
		
		if (shotingMob3) {
			if (loseFrame >= loseTime) {
				if(airID == Value.airTowerLaser) {
					Screen.mobsss[shotMob].loseHealth(2); // mobun ne kadar canı gidiyor
				}
				else if(airID == Value.airTowerLaser2) {
					Screen.mobsss[shotMob].loseHealth(4); // mobun ne kadar canı gidiyor
				}
				else if(airID == Value.airTowerLaser3) {
					Screen.mobsss[shotMob].loseHealth(10); // mobun ne kadar canı gidiyor
				}

				loseFrame = 0;
			} 
			else {
				loseFrame += 1;
			}

			if (Screen.mobsss[shotMob].isDead()) {
				shotingMob3 = false;
				shotMob = 0;
				Screen.hasWon();
			}
		}
	}
	
	void getMoney(int mobID) {
		if (mobID >= 0 && mobID < Value.deathReward.length) // avoid ArrayOutOfBounds exception
			Screen.coinage += Value.deathReward[mobID]; // get money when killing mobs
	}
	
	void fight(Graphics g) {

		Graphics2D g2d = (Graphics2D) g; // Ép kiểu sang Graphics2D để dùng được các tính năng vẽ nâng cao
		float[] dashPattern = {10.0f, 10.0f}; // Định nghĩa độ dài của nét gạch và khoảng trống (ví dụ: 10 pixel mực, 10 pixel trống)
		BasicStroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f); // Tạo cấu hình nét đứt (độ dày 2.0f)
		g2d.setStroke(dashed);

		if(Screen.isDebug) {
			if(airID == Value.airTowerLaser) {
				g2d.drawRect(towerSquare.x, towerSquare.y, towerSquare.width, towerSquare.height);
			}
			
			if(airID == Value.airTowerLaser2) { // 2.kule menzil
				g2d.drawRect(towerSquare2.x, towerSquare2.y, towerSquare2.width, towerSquare2.height);
			}
			
			if(airID == Value.airTowerLaser3) { // 3.kule menzil
				g2d.drawRect(towerSquare3.x, towerSquare3.y, towerSquare3.width, towerSquare3.height);
			}
		}

		//MOB1
		if(shotingMob1&&airID == Value.airTowerLaser) {    // kulenin lazeri
			g2d.setColor((new Color(255,255,0)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobs[shotMob].x + (Screen.mobs[shotMob].width/2), Screen.mobs[shotMob].y+ (Screen.mobs[shotMob].height/2));
		}
		else if(shotingMob1&&airID == Value.airTowerLaser2) {    // kulenin lazeri
			g2d.setColor((new Color(0,153,0)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobs[shotMob].x + (Screen.mobs[shotMob].width/2), Screen.mobs[shotMob].y+ (Screen.mobs[shotMob].height/2));
		}
		else if(shotingMob1&&airID == Value.airTowerLaser3) {    // kulenin lazeri
			g2d.setColor((new Color(51,153,255)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobs[shotMob].x + (Screen.mobs[shotMob].width/2), Screen.mobs[shotMob].y+ (Screen.mobs[shotMob].height/2));
		}
		
		//MOB2
		if(shotingMob2 && airID == Value.airTowerLaser) {    // kulenin lazeri
			g2d.setColor((new Color(255,255,0)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobss[shotMob].x + (Screen.mobss[shotMob].width/2), Screen.mobss[shotMob].y+ (Screen.mobss[shotMob].height/2));
		}
		else if(shotingMob2 && airID == Value.airTowerLaser2) {    // kulenin lazeri
			g2d.setColor((new Color(0,153,0)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobss[shotMob].x + (Screen.mobss[shotMob].width/2), Screen.mobss[shotMob].y+ (Screen.mobss[shotMob].height/2));
		}
		else if(shotingMob2 && airID == Value.airTowerLaser3) {    // kulenin lazeri
			g2d.setColor((new Color(51,153,255)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobss[shotMob].x + (Screen.mobss[shotMob].width/2), Screen.mobss[shotMob].y+ (Screen.mobss[shotMob].height/2));
		}
		
		
		//MOB3
		if(shotingMob3&&airID == Value.airTowerLaser) {    // kulenin lazeri
			g2d.setColor((new Color(255,255,0)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobsss[shotMob].x + (Screen.mobsss[shotMob].width/2), Screen.mobsss[shotMob].y+ (Screen.mobsss[shotMob].height/2));
		}
		else if(shotingMob3&&airID == Value.airTowerLaser2) {    // kulenin lazeri
			g2d.setColor((new Color(0,153,0)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobsss[shotMob].x + (Screen.mobsss[shotMob].width/2), Screen.mobsss[shotMob].y+ (Screen.mobsss[shotMob].height/2));
		}
		else if(shotingMob3&&airID == Value.airTowerLaser3) {    // kulenin lazeri
			g2d.setColor((new Color(51,153,255)));  //lazerin rengi
			g2d.drawLine(x + (width/2), y + (height/2), Screen.mobsss[shotMob].x + (Screen.mobsss[shotMob].width/2), Screen.mobsss[shotMob].y+ (Screen.mobsss[shotMob].height/2));
		}
	}
}