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
	public static int[] buttonID = {Value.ARCHER_TOWER, Value.MAGE_TOWER ,Value.CANNON_TOWER ,Value.GOLD_MINER ,Value.SPELL_BLACK_HOLE ,Value.SPELL_BOMBER_PLANE ,Value.SPELL_MERCENARY ,Value.TRASH_CAN  };  //thứ tự hiển thị trong shop
	public static int[] buttonPrice = {10,30,75,40,50,75,100,0};
	
	public Rectangle[] button = new Rectangle[shopWidth]; // chắc là dùng cho eventListener
	public Rectangle[] secondButtonRow = new Rectangle[shopWidth];
	public Rectangle buttonHealth;
	public Rectangle buttonCoins;
	
	public boolean holdsItem = false ;
	
	public Store() {
		for(int i = 0; i < button.length; i++) {
			button[i] = new Rectangle((Screen.myWidth/2) - ((shopWidth*(buttonSize+cellSpace))/2) + ((buttonSize+cellSpace)*i), 
									(Screen.room.block[Screen.room.worldHeight-1][0].y) + Screen.room.blockSize + awayFromRoom, 
									buttonSize, buttonSize);
		}
		for(int i = 0; i < secondButtonRow.length; i++) {
			secondButtonRow[i] = new Rectangle((Screen.myWidth/2) - ((shopWidth*(buttonSize+cellSpace))/2) + ((buttonSize+cellSpace)*i), 
									(Screen.room.block[Screen.room.worldHeight-1][0].y) + Screen.room.blockSize + awayFromRoom + ((buttonSize + cellSpace)), 
									buttonSize, buttonSize);
		}
		
		buttonHealth = new Rectangle(Screen.room.block[0][0].x-1, button[0].y, iconSize,iconSize);
		buttonCoins = new Rectangle(Screen.room.block[0][0].x-1, button[0].y + button[0].height-iconSize, iconSize,iconSize);
	}
	
	public void click(int mouseButton) {
		if(mouseButton == 1) {
			boolean clickedShop = false;
			for(int i = 0;i < button.length; i++) {
				if(button[i].contains(Screen.mse)) { // Screen.mse = new Point(e.getX(), e.getY());	
					if(buttonID[i] != Value.PLACEHOLDER) {
						clickedShop = true;
						// Bấm vào THÙNG RÁC
						if(buttonID[i] == Value.TRASH_CAN) {
							// 1. Nếu đang cầm bất kỳ vật phẩm nào (tháp hoặc thùng rác) -> Vứt đi
							if(holdsItem) {
								holdsItem = false;
								heldID = -1;
								realID = -1;
							}
							// 2. Nếu chưa cầm gì -> Cầm thùng rác lên
							else {
								heldID = Value.TRASH_CAN;
								realID = i;
								holdsItem = true;
							}
						}
						// Bấm vào THÁP
						else {
							// Nếu đang cầm đúng tháp này -> Bấm lại lần nữa thì hủy cầm
							if(holdsItem && heldID == buttonID[i]) {
								holdsItem = false;
								heldID = -1;
								realID = -1;
							}
							// Nếu chưa cầm hoặc đang cầm tháp khác/thùng rác -> Cầm tháp này lên
							else {
								heldID = buttonID[i];
								realID = i;
								holdsItem = true;
							}
						}
					} 
				}
			}
			
			if(holdsItem && !clickedShop) { // logic for holding item
				if (heldID == Value.TRASH_CAN) {
					// Xóa tháp 2x2 bằng thùng rác
					for(int y = 0; y < Screen.room.block.length; y++) {
						for(int x = 0; x < Screen.room.block[0].length; x++) {
							if(Screen.room.block[y][x].contains(Screen.mse)) {
								int rx = Screen.room.block[y][x].rootX;
								int ry = Screen.room.block[y][x].rootY;
								if (rx != -1 && ry != -1) {
									for(int dy = 0; dy < 2; dy++) {
										for(int dx = 0; dx < 2; dx++) {
											int ny = ry + dy;
											int nx = rx + dx;
											if(ny >= 0 && ny < Screen.room.worldHeight && nx >= 0 && nx < Screen.room.worldWidth) {
												Screen.room.block[ny][nx].towerID = Value.PLACEHOLDER;
												Screen.room.block[ny][nx].rootX = -1;
												Screen.room.block[ny][nx].rootY = -1;
												Screen.room.block[ny][nx].currentTower = null;
											}
										}
									}
								}
							}
						}
					}
				}
				else if(Screen.coinage >= buttonPrice[realID]) {
					// Nếu là Máy bay thả bom: Cho phép click thả bất kỳ đâu trên bản đồ (cả đường và cỏ)
					if (heldID == Value.SPELL_BOMBER_PLANE) {
						int mapBottom = Screen.room.block[Screen.room.worldHeight - 1][0].y + Screen.room.blockSize;
						if (Screen.mse.y < mapBottom && Screen.mse.y >= 0 && Screen.mse.x >= 0 && Screen.mse.x < Screen.myWidth) {
							Screen.activeSpells.add(new BomberPlane(Screen.mse.x, Screen.mse.y));
							Screen.coinage -= buttonPrice[realID];
							
							// Nếu bỏ thêm cái bên dưới thì sau khi bấm sẽ ngay lập tức biến mất cái vòng tròn
							// holdsItem = false;
							// realID = -1;
							// heldID = -1;
						}
					}
					else {
						for(int y= 0; y < Screen.room.block.length; y++) {
							for(int x = 0; x < Screen.room.block[0].length; x++) {
								if(Screen.room.block[y][x].contains(Screen.mse) && 
									y >= 1 && y < Screen.room.worldHeight && x >= 1 && x < Screen.room.worldWidth) {
									// nếu là spell mercenary thì  khi đặt, nó chỉ được chiếm 1 ô
									if (heldID == Value.SPELL_MERCENARY){
										Block b = Screen.room.block[y][x];
										if (isCellPlaceable(b, heldID)){
											Screen.room.block[y][x].towerID = Value.TOWER_OCCUPIED;
											for (int i = 0; i < Screen.merc.length; i++){
												if (!Screen.merc[i].inGame){
													int rootX = x;
													int rootY = y;
													Screen.merc[i].spawnMob(b.x, b.y, rootX, rootY, Value.SPELL_MERCENARY);
													Screen.coinage -= buttonPrice[realID];

													if (Screen.coinage < buttonPrice[realID]){
														holdsItem = false;
														realID = -1;
														heldID = -1;
													}
													break; // CỰC QUAN TRỌNG: mỗi lần click đảm bảo sinh ra ĐÚNG MỘT CON
												}
											}
										}
										continue;
									}
									boolean canPlace = true;
									for(int dy = 0; dy < 2; dy++) {
										for(int dx = 0; dx < 2; dx++) {
											Block b = Screen.room.block[y - 1 + dy][x - 1 + dx];
											if(!isCellPlaceable(b, heldID)) {
												canPlace = false;
												break;
											}
										}
										if(!canPlace) break;
									}
									// Ô (y, x) là Ô DƯỚI - BÊN PHẢI của tháp 2x2
									// Cụm 2x2 gồm: (y - 1, x - 1), (y - 1, x), (y, x - 1), (y, x)
									if(canPlace) {
										int rootY = y - 1;
										int rootX = x - 1;

										// Ô Trên - Bên Trái [rootY][rootX] lưu towerID để khởi tạo tháp
										Screen.room.block[rootY][rootX].rootX = rootX;
										Screen.room.block[rootY][rootX].rootY = rootY;
										Screen.room.block[rootY][rootX].towerID = heldID;

										// 3 ô còn lại nhận TOWER_OCCUPIED
										for(int dy = 0; dy < 2; dy++) {
											for(int dx = 0; dx < 2; dx++) {
												if(dy == 0 && dx == 0) continue;
												int ny = rootY + dy;
												int nx = rootX + dx;
												Screen.room.block[ny][nx].rootX = rootX;
												Screen.room.block[ny][nx].rootY = rootY;
												Screen.room.block[ny][nx].towerID = Value.TOWER_OCCUPIED;
											}
										}

										// Nếu là Spell Black Hole: Thêm vào activeSpells
										if (heldID == Value.SPELL_BLACK_HOLE) {
											Block topLeft = Screen.room.block[rootY][rootX];
											Screen.activeSpells.add(new BlackHole(topLeft.x, topLeft.y, rootX, rootY));
										}

										Screen.coinage -= buttonPrice[realID];  
										// delete sprite if current coin is less than the held item
										if (Screen.coinage < buttonPrice[realID]){
											holdsItem = false;
											realID = -1;
											heldID = -1;
										}
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
			heldID = -1;
		}
	}
	
	private boolean isCellPlaceable(Block b, int itemID) {
		if (b.towerID == Value.PLACEHOLDER) {
			if (itemID == Value.SPELL_BLACK_HOLE || itemID == Value.SPELL_MERCENARY) {
				return b.isRoad();
			}
			return !b.isRoad();
		}
		return false;
	}

	BomberPlane previewBomberPlane = new BomberPlane(0, 0);
	// lưu tạm để tí dùng cho việc xác định bán kính r
	
	public void draw(Graphics g) {
		for(int i = 0; i < button.length; i++) {
		
			if(button[i].contains(Screen.mse)) {
				g.drawImage(
				Tiles.touchedButton,
				button[i].x,
				button[i].y,
				button[i].width,
				button[i].height,
				null
				);
			}
			else{
				g.drawImage(
				Tiles.button,
				button[i].x,
				button[i].y,
				button[i].width,
				button[i].height,
				null
				);
			}
			
			// Vẽ khung cho tất cả ô shop
			

			g.drawImage(
				Tiles.button,
				secondButtonRow[i].x,
				secondButtonRow[i].y,
				secondButtonRow[i].width,
				secondButtonRow[i].height,
				null
			);

			// Chỉ vẽ tháp ở các ô chứa tháp
			if (buttonID[i] == Value.ARCHER_TOWER) {
				g.drawImage(
					Screen.cacherTower[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.MAGE_TOWER) {
				g.drawImage(
					Screen.mageTower[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.CANNON_TOWER) {
				g.drawImage(
					Screen.cannon[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.GOLD_MINER) {
				g.drawImage(
					Screen.goldMiner[0],
					button[i].x + itemIn,
					button[i].y + itemIn,
					button[i].width - itemIn * 2,
					button[i].height - itemIn * 2,
					null
				);
			}

			else if (buttonID[i] == Value.SPELL_BLACK_HOLE) {
				if (Screen.blackHole != null && Screen.blackHole[0] != null) {
					g.drawImage(
						Screen.blackHole[0],
						button[i].x + itemIn,
						button[i].y + itemIn,
						button[i].width - itemIn * 2,
						button[i].height - itemIn * 2,
						null
					);
				}
			}

			else if (buttonID[i] == Value.SPELL_BOMBER_PLANE) {
				if (Screen.airplane != null) {
					g.drawImage(
						Screen.airplane,
						button[i].x + itemIn,
						button[i].y + itemIn,
						button[i].width - itemIn * 2,
						button[i].height - itemIn * 2,
						null
					);
				}
			}

			if (buttonID[i] == Value.SPELL_MERCENARY){
				if (Screen.mercenaryIdleAnim != null){
					g.drawImage(
						Screen.mercenaryIcon,
						button[i].x + itemIn,
						button[i].y + itemIn,
						button[i].width - itemIn * 2,
						button[i].height - itemIn * 2,
						null
					);
				}
			}

			if (buttonID[i] != Value.PLACEHOLDER && buttonID[i] != Value.SPELL_BLACK_HOLE && buttonID[i] != Value.SPELL_BOMBER_PLANE && i > 3) {
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

			// Highlight ô đang được cầm trên tay (viền vàng phát sáng)
			if (holdsItem && (heldID == buttonID[i])) {
				g.setColor(new Color(255, 215, 0, 70));
				g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
				g.setColor(Color.YELLOW);
				g.drawRect(button[i].x, button[i].y, button[i].width, button[i].height);
				g.drawRect(button[i].x + 1, button[i].y + 1, button[i].width - 2, button[i].height - 2);
			}
		}
		
		g.drawImage(Screen.tileset_res[1], buttonHealth.x, buttonHealth.y, buttonHealth.width, buttonHealth.height,null);
		g.drawImage(Screen.tileset_res[2], buttonCoins.x, buttonCoins.y, buttonCoins.width, buttonCoins.height,null);
		g.setFont(new Font("Corier New", Font.BOLD,14));
		g.setColor(new Color(255,255,255));
		g.drawString(""+ Screen.health, buttonHealth.x + buttonHealth.width + iconSpace, buttonHealth.y + iconTextY);
		
		g.drawString(""+ Screen.coinage, buttonCoins.x + buttonCoins.width + iconSpace, buttonCoins.y + iconTextY);
		
		
		g.drawString("Killed = "+ Screen.killed, 870,640); // killed / show
		
		if (Screen.waveManager != null) {
			g.drawString("Wave: " + Screen.waveManager.getCurrentWave() + " / " + Screen.waveManager.getTotalWaves(), 870,670);
		}
		
		// xử lí logic khi đang giữ tháp/item
		if(holdsItem) {
			if (heldID == Value.TRASH_CAN) {
				// Highlight tháp 2x2 khi rê thùng rác vào
				for(int y = 0; y < Screen.room.block.length; y++) {
					for(int x = 0; x < Screen.room.block[0].length; x++) {
						if(Screen.room.block[y][x].contains(Screen.mse)) {
							int rx = Screen.room.block[y][x].rootX;
							int ry = Screen.room.block[y][x].rootY;
							if(rx != -1 && ry != -1 && ry >= 0 && ry + 1 < Screen.room.worldHeight && rx >= 0 && rx + 1 < Screen.room.worldWidth) {
								Block topLeft = Screen.room.block[ry][rx];
								int pw = Screen.room.blockSize * 2;
								int ph = Screen.room.blockSize * 2;
								g.setColor(new Color(255, 0, 0, 100));
								g.fillRect(topLeft.x, topLeft.y, pw, ph);
								g.setColor(Color.RED);
								g.drawRect(topLeft.x, topLeft.y, pw, ph);
							}
						}
					}
				}
			}
			else if (heldID == Value.SPELL_BOMBER_PLANE) {
				// Bán kính nổ mặc định tương đương 2x2 ô
				int r = previewBomberPlane.getBlastRadius(); 
				// Hiệu ứng nhấp nháy xanh lá dịu
				int alpha = (int) (140 + 60 * Math.sin(System.currentTimeMillis() / 150.0));
				if (alpha < 0) alpha = 0;
				if (alpha > 255) alpha = 255;

				// 1. Vùng phủ sóng sát thương hình tròn XANH LÁ MỜ (Green preview area)
				g.setColor(new Color(0, 255, 0, 45));
				g.fillOval(Screen.mse.x - r, Screen.mse.y - r, r * 2, r * 2);

				// 2. Viền tròn XANH LÁ nhấp nháy đánh dấu
				g.setColor(new Color(0, 255, 0, alpha));
				g.drawOval(Screen.mse.x - r, Screen.mse.y - r, r * 2, r * 2);
				g.drawOval(Screen.mse.x - r + 1, Screen.mse.y - r + 1, (r * 2) - 2, (r * 2) - 2);

				// 3. Dấu thập ngắm bắn màu xanh lá (Crosshair)
				g.drawLine(Screen.mse.x - r - 12, Screen.mse.y, Screen.mse.x - 8, Screen.mse.y);
				g.drawLine(Screen.mse.x + 8, Screen.mse.y, Screen.mse.x + r + 12, Screen.mse.y);
				g.drawLine(Screen.mse.x, Screen.mse.y - r - 12, Screen.mse.x, Screen.mse.y - 8);
				g.drawLine(Screen.mse.x, Screen.mse.y + 8, Screen.mse.x, Screen.mse.y + r + 12);
				g.fillOval(Screen.mse.x - 3, Screen.mse.y - 3, 6, 6);
			}
			else if (heldID == Value.SPELL_MERCENARY){ // vẽ 1 ô only
				Image holdingItem = Screen.mercenaryIdleAnim[0];
				for(int y = 0; y < Screen.room.block.length; y++) {
					for(int x = 0; x < Screen.room.block[0].length; x++) {
						if(Screen.room.block[y][x].contains(Screen.mse)) {
							// Ô (y, x) là ô Dưới - Bên Phải của cụm 2x2
							if(y >= 1 && y < Screen.room.worldHeight && x >= 1 && x < Screen.room.worldWidth) {
								Block b = Screen.room.block[y][x];
								boolean canPlace = isCellPlaceable(b, heldID);
								int blockSize = Screen.room.blockSize;

								if(canPlace) {
									g.setColor(new Color(0, 255, 0, 80));
								} else {
									g.setColor(new Color(255, 0, 0, 80));
								}
								g.fillRect(b.x, b.y, blockSize, blockSize);
								g.setColor(canPlace ? Color.GREEN : Color.RED);
								g.drawRect(b.x, b.y, blockSize, blockSize);

								if(holdingItem != null) {
									g.drawImage(holdingItem, b.x, b.y, blockSize, blockSize, null);
								}
							}
						}
					}
				}
			}
			else {
				Image holdingItem = null;
				if (heldID == Value.MAGE_TOWER) {
					holdingItem = Screen.mageTower[0];
				} 
				else if (heldID == Value.ARCHER_TOWER) {
					holdingItem = Screen.cacherTower[0];
				}
				else if(heldID == Value.GOLD_MINER) {
					holdingItem = Screen.goldMiner[0];
				}
				else if(heldID == Value.CANNON_TOWER) {
					holdingItem = Screen.cannon[0];
				}
				else if(heldID == Value.SPELL_BLACK_HOLE) {
					if (Screen.blackHole != null && Screen.blackHole.length > 0) {
						int frame = (int) ((System.currentTimeMillis() / 80) % Screen.blackHole.length);
						holdingItem = Screen.blackHole[frame];
					}
				}

				for(int y = 0; y < Screen.room.block.length; y++) {
					for(int x = 0; x < Screen.room.block[0].length; x++) {
						if(Screen.room.block[y][x].contains(Screen.mse)) {
							// Ô (y, x) là ô Dưới - Bên Phải của cụm 2x2
							if(y >= 1 && y < Screen.room.worldHeight && x >= 1 && x < Screen.room.worldWidth) {
								boolean canPlace = true;
								for(int dy = 0; dy < 2; dy++) {
									for(int dx = 0; dx < 2; dx++) {
										Block b = Screen.room.block[y - 1 + dy][x - 1 + dx];
										if(!isCellPlaceable(b, heldID)) {
											canPlace = false;
											break;
										}
									}
									if(!canPlace) break;
								}
								Block topLeft = Screen.room.block[y - 1][x - 1];
								int pw = Screen.room.blockSize * 2;
								int ph = Screen.room.blockSize * 2;

								if(canPlace) {
									g.setColor(new Color(0, 255, 0, 80));
								} else {
									g.setColor(new Color(255, 0, 0, 80));
								}
								g.fillRect(topLeft.x, topLeft.y, pw, ph);
								g.setColor(canPlace ? Color.GREEN : Color.RED);
								g.drawRect(topLeft.x, topLeft.y, pw, ph);

								if(holdingItem != null) {
									g.drawImage(holdingItem, topLeft.x, topLeft.y, pw, ph, null);
								}
							}
						}
					}
				}
			}
		}
	}
}
