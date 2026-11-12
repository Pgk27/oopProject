package core;
import java.awt.*; //Chịu trách nhiệm về kinh tế và tương tác đặt tháp
import map.Block;
import spell.BlackHole;
import spell.BomberPlane;
import spell.CursedSkull;
import spell.Enhancement;
import spell.FireSword;
import spell.GoldenPickaxe;

public class Store {
	// Khung HUD và vùng chứa hàng tower, item, skill trong ô thứ hai.
	public static final Rectangle OPTION_FRAME_BOUNDS = new Rectangle(70, 620, 1080, 140);
	private static final Rectangle ACTION_PANEL_BOUNDS = new Rectangle(
			OPTION_FRAME_BOUNDS.x + 212, OPTION_FRAME_BOUNDS.y + 40, 660, 64);
	private static final int ITEM_BUTTON_COUNT = 5;

	public static int shopWidth = 5; // số lượng ô
	public static int buttonSize = 56;
	public static int cellSpace = 8; // khoảng trống giữa các ô
	public static int itemIn = 4;   // khi vào trong game thì mỗi cạnh được ép xuống 4 pixel
	public static int heldID = -1;
	public static int realID = -1;
	public static int[] buttonID = {Value.ARCHER_TOWER, Value.MAGE_TOWER, Value.CANNON_TOWER, Value.GOLD_MINER, Value.TRASH_CAN};
	public static int[] buttonPrice = {10,30,75,40,0};
	
	public Rectangle[] button = new Rectangle[shopWidth]; // chắc là dùng cho eventListener
	
	public boolean holdsItem = false ;
	private static final long BLACK_HOLE_COOLDOWN_MS = 90_000;
	private static final long MERCENARY_COOLDOWN_MS = 15_000;
	private static final long ENHANCEMENT_COOLDOWN_MS = 45_000;
	private volatile long skillReadyAt;
	private volatile Enhancement enhancement;
	private volatile GoldenPickaxe goldenPickaxeBuff;
	private volatile FireSword fireSwordBuff;
	private volatile CursedSkull cursedSkullBuff;
	
	public Store() {
		int totalButtons = button.length + ITEM_BUTTON_COUNT;
		int size = Math.min(buttonSize, Math.min(ACTION_PANEL_BOUNDS.height - 6,
				(ACTION_PANEL_BOUNDS.width - (totalButtons - 1) * cellSpace) / totalButtons));
		int rowWidth = totalButtons * size + (totalButtons - 1) * cellSpace;
		int startX = ACTION_PANEL_BOUNDS.x + (ACTION_PANEL_BOUNDS.width - rowWidth) / 2;
		int startY = ACTION_PANEL_BOUNDS.y + (ACTION_PANEL_BOUNDS.height - size) / 2;

		for (int i = 0; i < button.length; i++) {
			button[i] = new Rectangle(startX + (size + cellSpace) * i, startY, size, size);
		}
	}

	public Rectangle getItemButtonBounds(int column) {
		Rectangle trashButton = button[button.length - 1];
		return new Rectangle(trashButton.x + (column + 1) * (trashButton.width + cellSpace),
				trashButton.y, trashButton.width, trashButton.height);
	}
	
	public boolean isSkillReady() {
		return System.currentTimeMillis() >= skillReadyAt;
	}

	public int getSkillCooldownSeconds() {
		if (getEnhancementRemainingSeconds() > 0) return 0;
		long remaining = Math.max(0, skillReadyAt - System.currentTimeMillis());
		return (int) ((remaining + 999) / 1000);
	}

	public int getEnhancementRemainingSeconds() {
		Enhancement buff = enhancement;
		return buff == null ? 0 : buff.getRemainingSeconds();
	}

	public double getHeroDamageMultiplier() {
		Enhancement buff = enhancement;
		return buff == null ? 1.0 : buff.getDamageMultiplier();
	}

	public int getGoldenPickaxeRemainingSeconds() {
		GoldenPickaxe buff = goldenPickaxeBuff;
		return buff == null ? 0 : buff.getRemainingSeconds();
	}

	public double getMineGoldMultiplier() {
		GoldenPickaxe buff = goldenPickaxeBuff;
		return buff == null ? 1.0 : buff.getGoldMultiplier();
	}

	public boolean canUseGoldenPickaxe() {
		GoldenPickaxe currentBuff = goldenPickaxeBuff;
		return Screen.gameState == Value.IN_GAME && Screen.goldenPickaxe > 0
				&& (currentBuff == null || currentBuff.isFinished());
	}

	public void useGoldenPickaxe() {
		if (!canUseGoldenPickaxe()) return;

		GoldenPickaxe buff = new GoldenPickaxe();
		goldenPickaxeBuff = buff;
		Screen.activeSpells.add(buff);
		Screen.goldenPickaxe--;
		clearSelection();
	}

	public int getFireSwordRemainingSeconds() {
		FireSword buff = fireSwordBuff;
		return buff == null ? 0 : buff.getRemainingSeconds();
	}

	public double getTowerDamageMultiplier() {
		FireSword buff = fireSwordBuff;
		return buff == null ? 1.0 : buff.getDamageMultiplier();
	}

	public boolean canUseFireSword() {
		FireSword currentBuff = fireSwordBuff;
		return Screen.gameState == Value.IN_GAME && Screen.fireSword > 0
				&& (currentBuff == null || currentBuff.isFinished());
	}

	public void useFireSword() {
		if (!canUseFireSword()) return;

		FireSword buff = new FireSword();
		fireSwordBuff = buff;
		Screen.activeSpells.add(buff);
		Screen.fireSword--;
		clearSelection();
	}

	public int getCursedSkullRemainingSeconds() {
		CursedSkull buff = cursedSkullBuff;
		return buff == null ? 0 : buff.getRemainingSeconds();
	}

	public double getMonsterSpeedMultiplier() {
		CursedSkull buff = cursedSkullBuff;
		return buff == null ? 1.0 : buff.getSpeedMultiplier();
	}

	public boolean canUseCursedSkull() {
		CursedSkull currentBuff = cursedSkullBuff;
		return Screen.gameState == Value.IN_GAME && Screen.cursedSkull > 0
				&& (currentBuff == null || currentBuff.isFinished());
	}

	public void useCursedSkull() {
		if (!canUseCursedSkull()) return;

		CursedSkull buff = new CursedSkull();
		cursedSkullBuff = buff;
		Screen.activeSpells.add(buff);
		Screen.cursedSkull--;
		clearSelection();
	}

	public void resetSkills() {
		clearSelection();
		enhancement = null;
		goldenPickaxeBuff = null;
		fireSwordBuff = null;
		cursedSkullBuff = null;
		skillReadyAt = 0;
	}

	public boolean canSelectChosenSkill() {
		return Screen.gameState == Value.IN_GAME && isSkillReady()
				&& Screen.chosenCard >= 1 && Screen.chosenCard <= 3;
	}

	public void selectChosenSkill() {
		if (!canSelectChosenSkill()) return;
		if (Screen.chosenCard == 2) {
			clearSelection();
			Enhancement buff = new Enhancement();
			enhancement = buff;
			skillReadyAt = buff.getEndTime() + ENHANCEMENT_COOLDOWN_MS;
			Screen.activeSpells.add(buff);
			return;
		}
		int skillID;
		if (Screen.chosenCard == 1) skillID = Value.SPELL_BLACK_HOLE;
		else if (Screen.chosenCard == 3) skillID = Value.SPELL_MERCENARY;
		else return;

		if (holdsItem && heldID == skillID) {
			clearSelection();
		} else {
			heldID = skillID;
			realID = -1;
			holdsItem = true;
		}
	}

	private void finishSkillPlacement() {
		long cooldown = Screen.chosenCard == 1 ? BLACK_HOLE_COOLDOWN_MS : MERCENARY_COOLDOWN_MS;
		skillReadyAt = System.currentTimeMillis() + cooldown;
		clearSelection();
	}

	private void placeChosenSkill() {
		boolean matchesCard = (heldID == Value.SPELL_BLACK_HOLE && Screen.chosenCard == 1)
			|| (heldID == Value.SPELL_MERCENARY && Screen.chosenCard == 3);
		if (!matchesCard || !isSkillReady()) {
			clearSelection();
			return;
		}
		for (int y = 0; y < Screen.room.worldHeight; y++) {
			for (int x = 0; x < Screen.room.worldWidth; x++) {
				Block cell = Screen.room.block[y][x];
				if (!cell.contains(Screen.mse)) continue;
				if (heldID == Value.SPELL_MERCENARY) {
					if (!isCellPlaceable(cell, heldID)) return;
					for (entity.mob.Mercenary mercenary : Screen.merc) {
						if (mercenary != null && !mercenary.inGame) {
							mercenary.spawnMob(cell.x, cell.y, x, y, heldID);
							cell.towerID = Value.TOWER_OCCUPIED;
							finishSkillPlacement();
							return;
						}
					}
					return;
				}
				// Black Hole chiếm cụm 2x2, con trỏ ở ô dưới bên phải.
				if (x < 1 || y < 1) return;
				for (int dy = -1; dy <= 0; dy++) {
					for (int dx = -1; dx <= 0; dx++) {
						if (!isCellPlaceable(Screen.room.block[y + dy][x + dx], heldID)) return;
					}
				}
				int rootX = x - 1;
				int rootY = y - 1;
				for (int dy = 0; dy < 2; dy++) {
					for (int dx = 0; dx < 2; dx++) {
						Block block = Screen.room.block[rootY + dy][rootX + dx];
						block.rootX = rootX;
						block.rootY = rootY;
						block.towerID = (dx == 0 && dy == 0) ? heldID : Value.TOWER_OCCUPIED;
					}
				}
				Block topLeft = Screen.room.block[rootY][rootX];
				Screen.activeSpells.add(new BlackHole(topLeft.x, topLeft.y, rootX, rootY));
				finishSkillPlacement();
				return;
			}
		}
	}

	public boolean canSelectBomberPlane() {
		return Screen.gameState == Value.IN_GAME
				&& ((holdsItem && heldID == Value.SPELL_BOMBER_PLANE) || Screen.paperPlane > 0);
	}

	public void selectBomberPlane() {
		if (!canSelectBomberPlane()) return;
		if (holdsItem && heldID == Value.SPELL_BOMBER_PLANE) {
			clearSelection();
		} else if (Screen.paperPlane > 0) {
			heldID = Value.SPELL_BOMBER_PLANE;
			realID = -1;
			holdsItem = true;
		}
	}

	public void clearSelection() {
		holdsItem = false;
		heldID = -1;
		realID = -1;
	}

	private boolean isBomberTarget(Point target) {
		if (Screen.room == null) return false;
		Block topLeft = Screen.room.block[0][0];
		Rectangle mapBounds = new Rectangle(topLeft.x, topLeft.y,
			Screen.room.worldWidth * Screen.room.blockSize,
			Screen.room.worldHeight * Screen.room.blockSize);
		return mapBounds.contains(target);
	}

	public void click(int mouseButton) {
		if (Screen.gameState != Value.IN_GAME) return;
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
				// Nút máy bay chọn kỹ năng độc lập với chỉ số ô trong shop.
				if (heldID == Value.SPELL_BOMBER_PLANE) {
					if (isBomberTarget(Screen.mse) && Screen.paperPlane > 0) {
						Screen.activeSpells.add(new BomberPlane(Screen.mse.x, Screen.mse.y));
						Screen.paperPlane--;
						clearSelection();
					}
					return;
				}
				if (heldID == Value.SPELL_BLACK_HOLE || heldID == Value.SPELL_MERCENARY) {
					placeChosenSkill();
					return;
				}
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
				else if(realID >= 0 && realID < buttonPrice.length && Screen.coinage >= buttonPrice[realID]) {
					for(int y= 0; y < Screen.room.block.length; y++) {
						for(int x = 0; x < Screen.room.block[0].length; x++) {
							if(Screen.room.block[y][x].contains(Screen.mse) && 
								y >= 1 && y < Screen.room.worldHeight && x >= 1 && x < Screen.room.worldWidth) {
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

		// stop holding item when pressing right click
		else if (mouseButton == 3){
			clearSelection();
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
				Screen.tiles.touchedButton,
				button[i].x,
				button[i].y,
				button[i].width,
				button[i].height,
				null
				);
			}
			else{
				g.drawImage(
				Screen.tiles.button,
				button[i].x,
				button[i].y,
				button[i].width,
				button[i].height,
				null
				);
			}
			
			// Vẽ khung cho tất cả ô shop
			

			// g.drawImage(
			// 	Screen.tiles.button,
			// 	secondButtonRow[i].x,
			// 	secondButtonRow[i].y,
			// 	secondButtonRow[i].width,
			// 	secondButtonRow[i].height,
			// 	null
			// );

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

			else if (buttonID[i] == Value.TRASH_CAN) {
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
				g.setColor(new Color(0, 0, 0));
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
		
		g.setFont(new Font("Corier New", Font.BOLD,14));
		g.setColor(new Color(255,255,255));
		g.drawString("Level: "+ Screen.rielLevel, 980, 680);
		g.drawString("Killed = "+ Screen.killed, 980,695); // killed / show
		
		if (Screen.waveManager != null) {
			g.drawString("Wave: " + Screen.waveManager.getCurrentWave() + " / " + Screen.waveManager.getTotalWaves(), 980,710);
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
				if (!isBomberTarget(Screen.mse)) return;
				// Chỉ vẽ vòng ngắm trong vùng bản đồ có thể gọi máy bay.
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
							if(y >= 0 && y < Screen.room.worldHeight && x >= 0 && x < Screen.room.worldWidth) {
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
