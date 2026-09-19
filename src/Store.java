import java.awt.*; //Chịu trách nhiệm về kinh tế và tương tác đặt tháp

public class Store {
	public static int shopWidth = 8; // số lượng ô
	public static int buttonSize = 52;
	public static int cellSpace = 2; // khoảng trống giữa các ô
	public static int awayFromRoom = 29; // khoảng cách từ map đến store
	public static int iconSize = 20; 
	public static int iconSpace = 6; // khoảng cách nữa số và icon tim với coin
	public static int iconTextY = 15; // ép lui xuống để đẹp mắt:))	
	public static int itemIn = 4;   // khi vào trong game thì mỗi cạnh được ép xuống 4 pixel
	public static int heldID = -1;
	public static int realID = -1;
	public static int[] buttonID = {Value.airTowerLaser, Value.airTowerLaser2 ,Value.airTowerLaser3 ,Value.airTowerLaser4 ,Value.airAir ,Value.airAir ,Value.airAir ,Value.airTrashCan  };  //thứ tự hiển thị trong shop
	public static int[] buttonPrice = {10,30,75,40,0,0,0,0};
	
	public Rectangle[] button = new Rectangle[shopWidth]; // chắc là dùng cho eventListener
	public Rectangle buttonHealth ;
	public Rectangle buttonCoins ;
	
	public boolean holdsItem = false ;
	
	public Store() {
		for(int i = 0; i < button.length; i++) {
			button[i] = new Rectangle((Screen.myWidth/2) - ((shopWidth*(buttonSize+cellSpace))/2) + ((buttonSize+cellSpace)*i), (Screen.room.block[Screen.room.worldHeight-1][0].y) +Screen.room.blockSize + awayFromRoom, buttonSize, buttonSize);
		}
		
		buttonHealth = new Rectangle(Screen.room.block[0][0].x-1, button[0].y, iconSize,iconSize);
		buttonCoins = new Rectangle(Screen.room.block[0][0].x-1, button[0].y + button[0].height-iconSize, iconSize,iconSize);
	}
	
	public void click(int mouseButton) {
		if(mouseButton == 1) {
			for(int i = 0;i < button.length; i++) {
				if(button[i].contains(Screen.mse)) { // nghe tọa độ xem cái nào juan	
					if(buttonID[i] != Value.airAir) {
						if(buttonID[i] == Value.airTrashCan) { // if touch TrashCan, stop holding item
							holdsItem = false;
						}
						else {
							heldID = buttonID[i];
							realID = i;
							holdsItem = true;
						}
					} 
				}
			}
			
			if(holdsItem) { // logic for holding item
				if(Screen.coinage >=buttonPrice[realID]) {
					for(int y= 0; y < Screen.room.block.length; y++) {
						for(int x = 0; x < Screen.room.block[0].length; x++) {
							if(Screen.room.block[y][x].contains(Screen.mse)) {
								if(Screen.room.block[y][x].groundID != Value.groundRoad && Screen.room.block[y][x].airID == Value.airAir) {
									Screen.room.block[y][x].airID = heldID;
									Screen.coinage -= buttonPrice[realID];  
									// delete sprite if current coin is less than the held item
									if (Screen.coinage < buttonPrice[realID]){
										holdsItem = false;
										realID = -1;
									}
								}
							}
						}
					}
				}
			}
		}

		// stop holding item when pressing right click
		else if (mouseButton == 3){
			holdsItem = false;
			realID = -1;
		}
	}
	
	
	public void draw(Graphics g) {
		for(int i = 0; i < button.length; i++) {
		
			if(button[i].contains(Screen.mse)) {
				g.setColor(new Color(51,204,255,100));  //  shoptaki slotların highlights 
				g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height); // içini dolduruyor
			}

			//g.drawImage(Screen.tileset_res[0] ,button[i].x,  button[i].y, button[i].width, button[i].height,null); // store daki slotların çizilmesi
			
			// Vẽ khung cho tất cả ô shop
			g.drawImage(
				Screen.tileset_res[0],
				button[i].x,
				button[i].y,
				button[i].width,
				button[i].height,
				null
			);

			// Chỉ vẽ tháp ở các ô chứa tháp
			if (buttonID[i] == Value.airTowerLaser) {
				g.drawImage(
					Screen.cacherTower[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.airTowerLaser2) {
				g.drawImage(
					Screen.mageTower[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.airTowerLaser3) {
				g.drawImage(
					Screen.cannon[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.airTowerLaser4) {
				g.drawImage(
					Screen.goldMiner[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			if (buttonID[i] != Value.airAir && i > 3) {
				g.drawImage(
					Screen.tileset_air[buttonID[i]],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}
			
			if(buttonPrice[i]> 0) {
				g.setColor(new Color(255,255,255));
				g.setFont(new Font("Courier New", Font.BOLD, 14));
				g.drawString("$"+	buttonPrice[i] ,button[i].x + itemIn,  button[i].y+ itemIn+10);
			}
		}
		
		g.drawImage(Screen.tileset_res[1], buttonHealth.x, buttonHealth.y, buttonHealth.width, buttonHealth.height,null);//kalp resmini koyuyor
		g.drawImage(Screen.tileset_res[2], buttonCoins.x, buttonCoins.y, buttonCoins.width, buttonCoins.height,null);//coin resmini koyuyor
		g.setFont(new Font("Corier New", Font.BOLD,14));
		g.setColor(new Color(255,255,255));
		g.drawString(""+ Screen.health, buttonHealth.x + buttonHealth.width + iconSpace, buttonHealth.y + iconTextY);//kac canımız kaldığını kalbin yanına yazıyor
		
		g.drawString(""+ Screen.coinage, buttonCoins.x + buttonCoins.width + iconSpace, buttonCoins.y + iconTextY); //kac coin kaldığını coinin yanına yazıyor
		
		
		g.drawString("Killed = "+ Screen.killed, 570,460); // killed / show
		g.drawString("Kills to win = "+ (Screen.killsToWin-Screen.killed), 570,490);
		
		
		if(holdsItem) {// store dan elimize aldığımız kule vs. mouseda çiziyor(durmasını sağlıyor)
			//g.drawImage(Screen.tileset_air[heldID], Screen.mse.x - ((button[0].width- (itemIn*2) )/2) + itemIn, Screen.mse.y -((button[0].width- (itemIn*2) )/2)+ itemIn, button[0].width- (itemIn*2), button[0].height-(itemIn*2),null);
			Image holdingItem;
			if (heldID == Value.airTowerLaser2) {
				holdingItem = Screen.mageTower[0];
			} 
			else if (heldID == Value.airTowerLaser) {
				holdingItem = Screen.cacherTower[0];
			}
			else if(heldID == Value.airTowerLaser4) {
				holdingItem = Screen.goldMiner[0];
			}
			else {
				holdingItem = Screen.cannon[0];
			}

			g.drawImage(
				holdingItem,
				Screen.mse.x - ((button[0].width - itemIn * 2) / 2) + itemIn,
				Screen.mse.y - ((button[0].width - itemIn * 2) / 2) + itemIn,
				button[0].width - itemIn * 2,
				button[0].height - itemIn * 2,
				null
			);



			// update lại hình ảnh của các tháp thành tháp cung tên hết
			
		}
	}
}
