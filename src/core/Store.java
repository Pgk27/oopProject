package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import map.Block;
import spell.BlackHole;
import spell.BomberPlane;

/**
 * Quan ly Cua hang (Shop), dat thap (Placement Controller)
 * va tuong tac HUD dat linh/phep tren ban do.
 */
public class Store {
	// Khung HUD va vung chua hang tower, item, skill
	public static final Rectangle OPTION_FRAME_BOUNDS = new Rectangle(70, 620, 1080, 140);
	private static final Rectangle ACTION_PANEL_BOUNDS = new Rectangle(
			OPTION_FRAME_BOUNDS.x + 212, OPTION_FRAME_BOUNDS.y + 40, 660, 64);
	private static final int ITEM_BUTTON_COUNT = 5;

	public static int shopWidth = 5;
	public static int buttonSize = 56;
	public static int cellSpace = 8;
	public static int itemIn = 4;
	public static int heldID = -1;
	public static int realID = -1;
	public static int[] buttonID = {Constants.ARCHER_TOWER, Constants.MAGE_TOWER, Constants.CANNON_TOWER, Constants.GOLD_MINER, Constants.TRASH_CAN};
	public static int[] buttonPrice = {10, 30, 75, 40, 0};

	public Rectangle[] button = new Rectangle[shopWidth];
	public boolean holdsItem = false;

	// Quan ly Buff va Cooldown
	public final BuffManager buffManager = new BuffManager();
	private final BomberPlane previewBomberPlane = new BomberPlane(0, 0);

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

	// --- DELEGATE DEN BUFF MANAGER ---
	public boolean isSkillReady() { return buffManager.isSkillReady(); }
	public int getSkillCooldownSeconds() { return buffManager.getSkillCooldownSeconds(); }
	public int getEnhancementRemainingSeconds() { return buffManager.getEnhancementRemainingSeconds(); }
	public double getHeroDamageMultiplier() { return buffManager.getHeroDamageMultiplier(); }
	public int getGoldenPickaxeRemainingSeconds() { return buffManager.getGoldenPickaxeRemainingSeconds(); }
	public double getMineGoldMultiplier() { return buffManager.getMineGoldMultiplier(); }
	public boolean canUseGoldenPickaxe() { return buffManager.canUseGoldenPickaxe(); }
	public int getFireSwordRemainingSeconds() { return buffManager.getFireSwordRemainingSeconds(); }
	public double getTowerDamageMultiplier() { return buffManager.getTowerDamageMultiplier(); }
	public boolean canUseFireSword() { return buffManager.canUseFireSword(); }
	public int getCursedSkullRemainingSeconds() { return buffManager.getCursedSkullRemainingSeconds(); }
	public double getMonsterSpeedMultiplier() { return buffManager.getMonsterSpeedMultiplier(); }
	public boolean canUseCursedSkull() { return buffManager.canUseCursedSkull(); }
	public boolean canSelectChosenSkill() { return buffManager.canSelectChosenSkill(); }

	public void useGoldenPickaxe() {
		if (buffManager.useGoldenPickaxe()) clearSelection();
	}

	public void useFireSword() {
		if (buffManager.useFireSword()) clearSelection();
	}

	public void useCursedSkull() {
		if (buffManager.useCursedSkull()) clearSelection();
	}

	public void resetSkills() {
		clearSelection();
		buffManager.resetSkills();
	}

	public void selectChosenSkill() {
		if (!buffManager.canSelectChosenSkill()) return;
		if (Screen.chosenCard == 2) {
			clearSelection();
			buffManager.activateEnhancement();
			return;
		}
		int skillID;
		if (Screen.chosenCard == 1) skillID = Constants.SPELL_BLACK_HOLE;
		else if (Screen.chosenCard == 3) skillID = Constants.SPELL_MERCENARY;
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
		buffManager.finishSkillPlacement(Screen.chosenCard);
		clearSelection();
	}

	private void placeChosenSkill() {
		boolean matchesCard = (heldID == Constants.SPELL_BLACK_HOLE && Screen.chosenCard == 1)
			|| (heldID == Constants.SPELL_MERCENARY && Screen.chosenCard == 3);
		if (!matchesCard || !buffManager.isSkillReady()) {
			clearSelection();
			return;
		}
		for (int y = 0; y < Screen.room.worldHeight; y++) {
			for (int x = 0; x < Screen.room.worldWidth; x++) {
				Block cell = Screen.room.block[y][x];
				if (!cell.contains(Screen.mse)) continue;
				if (heldID == Constants.SPELL_MERCENARY) {
					if (!isCellPlaceable(cell, heldID)) return;
					for (entity.defender.Mercenary mercenary : Screen.merc) {
						if (mercenary != null && !mercenary.inGame) {
							mercenary.spawnMob(cell.x, cell.y, x, y, heldID);
							cell.towerID = Constants.TOWER_OCCUPIED;
							finishSkillPlacement();
							return;
						}
					}
					return;
				}
				// Black Hole chiem cum 2x2, con tro o o duoi ben phai.
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
						block.towerID = (dx == 0 && dy == 0) ? heldID : Constants.TOWER_OCCUPIED;
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
		return Screen.gameState == Constants.IN_GAME
				&& ((holdsItem && heldID == Constants.SPELL_BOMBER_PLANE) || Screen.paperPlane > 0);
	}

	public void selectBomberPlane() {
		if (!canSelectBomberPlane()) return;
		if (holdsItem && heldID == Constants.SPELL_BOMBER_PLANE) {
			clearSelection();
		} else if (Screen.paperPlane > 0) {
			heldID = Constants.SPELL_BOMBER_PLANE;
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
		if (Screen.gameState != Constants.IN_GAME) return;
		if (mouseButton == 1) {
			boolean clickedShop = false;
			for (int i = 0; i < button.length; i++) {
				if (button[i].contains(Screen.mse)) {
					if (buttonID[i] != Constants.PLACEHOLDER) {
						clickedShop = true;
						if (buttonID[i] == Constants.TRASH_CAN) {
							if (holdsItem) {
								clearSelection();
							} else {
								heldID = Constants.TRASH_CAN;
								realID = i;
								holdsItem = true;
							}
						} else {
							if (holdsItem && heldID == buttonID[i]) {
								clearSelection();
							} else {
								heldID = buttonID[i];
								realID = i;
								holdsItem = true;
							}
						}
					}
				}
			}

			if (holdsItem && !clickedShop) {
				if (heldID == Constants.SPELL_BOMBER_PLANE) {
					if (isBomberTarget(Screen.mse) && Screen.paperPlane > 0) {
						Screen.activeSpells.add(new BomberPlane(Screen.mse.x, Screen.mse.y));
						Screen.paperPlane--;
						clearSelection();
					}
					return;
				}
				if (heldID == Constants.SPELL_BLACK_HOLE || heldID == Constants.SPELL_MERCENARY) {
					placeChosenSkill();
					return;
				}
				if (heldID == Constants.TRASH_CAN) {
					for (int y = 0; y < Screen.room.block.length; y++) {
						for (int x = 0; x < Screen.room.block[0].length; x++) {
							if (Screen.room.block[y][x].contains(Screen.mse)) {
								int rx = Screen.room.block[y][x].rootX;
								int ry = Screen.room.block[y][x].rootY;
								if (rx != -1 && ry != -1) {
									for (int dy = 0; dy < 2; dy++) {
										for (int dx = 0; dx < 2; dx++) {
											int ny = ry + dy;
											int nx = rx + dx;
											if (ny >= 0 && ny < Screen.room.worldHeight && nx >= 0 && nx < Screen.room.worldWidth) {
												Screen.room.block[ny][nx].towerID = Constants.PLACEHOLDER;
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
				} else if (realID >= 0 && realID < buttonPrice.length && Screen.coinage >= buttonPrice[realID]) {
					for (int y = 0; y < Screen.room.block.length; y++) {
						for (int x = 0; x < Screen.room.block[0].length; x++) {
							if (Screen.room.block[y][x].contains(Screen.mse) &&
								y >= 1 && y < Screen.room.worldHeight && x >= 1 && x < Screen.room.worldWidth) {
								boolean canPlace = true;
								for (int dy = 0; dy < 2; dy++) {
									for (int dx = 0; dx < 2; dx++) {
										Block b = Screen.room.block[y - 1 + dy][x - 1 + dx];
										if (!isCellPlaceable(b, heldID)) {
											canPlace = false;
											break;
										}
									}
									if (!canPlace) break;
								}
								if (canPlace) {
									int rootY = y - 1;
									int rootX = x - 1;

									Screen.room.block[rootY][rootX].rootX = rootX;
									Screen.room.block[rootY][rootX].rootY = rootY;
									Screen.room.block[rootY][rootX].towerID = heldID;

									for (int dy = 0; dy < 2; dy++) {
										for (int dx = 0; dx < 2; dx++) {
											if (dy == 0 && dx == 0) continue;
											int ny = rootY + dy;
											int nx = rootX + dx;
											Screen.room.block[ny][nx].rootX = rootX;
											Screen.room.block[ny][nx].rootY = rootY;
											Screen.room.block[ny][nx].towerID = Constants.TOWER_OCCUPIED;
										}
									}

									Screen.coinage -= buttonPrice[realID];
									if (Screen.coinage < buttonPrice[realID]) {
										clearSelection();
									}
								}
							}
						}
					}
				}
			}
		} else if (mouseButton == 3) {
			clearSelection();
		}
	}

	private boolean isCellPlaceable(Block b, int itemID) {
		if (b.towerID == Constants.PLACEHOLDER) {
			if (itemID == Constants.SPELL_BLACK_HOLE || itemID == Constants.SPELL_MERCENARY) {
				return b.isRoad();
			}
			return !b.isRoad();
		}
		return false;
	}

	public void draw(Graphics g) {
		for (int i = 0; i < button.length; i++) {
			Image btnImg = button[i].contains(Screen.mse) ? Screen.tiles.touchedButton : Screen.tiles.button;
			g.drawImage(btnImg, button[i].x, button[i].y, button[i].width, button[i].height, null);

			Image icon = null;
			if (buttonID[i] == Constants.ARCHER_TOWER && Screen.archerTower != null && Screen.archerTower.length > 0) {
				icon = Screen.archerTower[0];
			} else if (buttonID[i] == Constants.MAGE_TOWER && Screen.mageTower != null && Screen.mageTower.length > 0) {
				icon = Screen.mageTower[0];
			} else if (buttonID[i] == Constants.CANNON_TOWER && Screen.cannon != null && Screen.cannon.length > 0) {
				icon = Screen.cannon[0];
			} else if (buttonID[i] == Constants.GOLD_MINER && Screen.goldMiner != null && Screen.goldMiner.length > 0) {
				icon = Screen.goldMiner[0];
			} else if (buttonID[i] == Constants.TRASH_CAN && Screen.tileset_air != null && Screen.tileset_air.length > buttonID[i]) {
				icon = Screen.tileset_air[buttonID[i]];
			}

			if (icon != null) {
				g.drawImage(icon, button[i].x + itemIn, button[i].y + itemIn,
						button[i].width - itemIn * 2, button[i].height - itemIn * 2, null);
			}

			if (buttonPrice[i] > 0) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Courier New", Font.BOLD, 14));
				g.drawString("$" + buttonPrice[i], button[i].x + itemIn, button[i].y + itemIn + 10);
			}

			if (holdsItem && (heldID == buttonID[i])) {
				g.setColor(new Color(255, 215, 0, 70));
				g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
				g.setColor(Color.YELLOW);
				g.drawRect(button[i].x, button[i].y, button[i].width, button[i].height);
				g.drawRect(button[i].x + 1, button[i].y + 1, button[i].width - 2, button[i].height - 2);
			}
		}

		g.setFont(new Font("Courier New", Font.BOLD, 14));
		g.setColor(Color.WHITE);
		g.drawString("Level: " + Screen.rielLevel, 980, 680);
		g.drawString("Killed = " + Screen.killed, 980, 695);

		if (Screen.waveManager != null) {
			g.drawString("Wave: " + Screen.waveManager.getCurrentWave() + " / " + Screen.waveManager.getTotalWaves(), 980, 710);
		}

		drawPlacementPreview(g);
	}

	private void drawPlacementPreview(Graphics g) {
		if (!holdsItem) return;

		if (heldID == Constants.TRASH_CAN) {
			for (int y = 0; y < Screen.room.block.length; y++) {
				for (int x = 0; x < Screen.room.block[0].length; x++) {
					if (Screen.room.block[y][x].contains(Screen.mse)) {
						int rx = Screen.room.block[y][x].rootX;
						int ry = Screen.room.block[y][x].rootY;
						if (rx != -1 && ry != -1 && ry >= 0 && ry + 1 < Screen.room.worldHeight && rx >= 0 && rx + 1 < Screen.room.worldWidth) {
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
		} else if (heldID == Constants.SPELL_BOMBER_PLANE) {
			if (!isBomberTarget(Screen.mse)) return;
			int r = previewBomberPlane.getBlastRadius();
			int alpha = (int) (140 + 60 * Math.sin(System.currentTimeMillis() / 150.0));
			alpha = Math.max(0, Math.min(255, alpha));

			g.setColor(new Color(0, 255, 0, 45));
			g.fillOval(Screen.mse.x - r, Screen.mse.y - r, r * 2, r * 2);

			g.setColor(new Color(0, 255, 0, alpha));
			g.drawOval(Screen.mse.x - r, Screen.mse.y - r, r * 2, r * 2);
			g.drawOval(Screen.mse.x - r + 1, Screen.mse.y - r + 1, (r * 2) - 2, (r * 2) - 2);

			g.drawLine(Screen.mse.x - r - 12, Screen.mse.y, Screen.mse.x - 8, Screen.mse.y);
			g.drawLine(Screen.mse.x + 8, Screen.mse.y, Screen.mse.x + r + 12, Screen.mse.y);
			g.drawLine(Screen.mse.x, Screen.mse.y - r - 12, Screen.mse.x, Screen.mse.y - 8);
			g.drawLine(Screen.mse.x, Screen.mse.y + 8, Screen.mse.x, Screen.mse.y + r + 12);
			g.fillOval(Screen.mse.x - 3, Screen.mse.y - 3, 6, 6);
		} else if (heldID == Constants.SPELL_MERCENARY) {
			Image holdingItem = Screen.mercenaryIdleAnim != null && Screen.mercenaryIdleAnim.length > 0 ? Screen.mercenaryIdleAnim[0] : null;
			for (int y = 0; y < Screen.room.block.length; y++) {
				for (int x = 0; x < Screen.room.block[0].length; x++) {
					if (Screen.room.block[y][x].contains(Screen.mse)) {
						if (y >= 0 && y < Screen.room.worldHeight && x >= 0 && x < Screen.room.worldWidth) {
							Block b = Screen.room.block[y][x];
							boolean canPlace = isCellPlaceable(b, heldID);
							int blockSize = Screen.room.blockSize;

							g.setColor(canPlace ? new Color(0, 255, 0, 80) : new Color(255, 0, 0, 80));
							g.fillRect(b.x, b.y, blockSize, blockSize);
							g.setColor(canPlace ? Color.GREEN : Color.RED);
							g.drawRect(b.x, b.y, blockSize, blockSize);

							if (holdingItem != null) {
								g.drawImage(holdingItem, b.x, b.y, blockSize, blockSize, null);
							}
						}
					}
				}
			}
		} else {
			Image holdingItem = null;
			if (heldID == Constants.MAGE_TOWER && Screen.mageTower != null && Screen.mageTower.length > 0) {
				holdingItem = Screen.mageTower[0];
			} else if (heldID == Constants.ARCHER_TOWER && Screen.archerTower != null && Screen.archerTower.length > 0) {
				holdingItem = Screen.archerTower[0];
			} else if (heldID == Constants.GOLD_MINER && Screen.goldMiner != null && Screen.goldMiner.length > 0) {
				holdingItem = Screen.goldMiner[0];
			} else if (heldID == Constants.CANNON_TOWER && Screen.cannon != null && Screen.cannon.length > 0) {
				holdingItem = Screen.cannon[0];
			} else if (heldID == Constants.SPELL_BLACK_HOLE && Screen.blackHole != null && Screen.blackHole.length > 0) {
				int frame = (int) ((System.currentTimeMillis() / 80) % Screen.blackHole.length);
				holdingItem = Screen.blackHole[frame];
			}

			for (int y = 0; y < Screen.room.block.length; y++) {
				for (int x = 0; x < Screen.room.block[0].length; x++) {
					if (Screen.room.block[y][x].contains(Screen.mse)) {
						if (y >= 1 && y < Screen.room.worldHeight && x >= 1 && x < Screen.room.worldWidth) {
							boolean canPlace = true;
							for (int dy = 0; dy < 2; dy++) {
								for (int dx = 0; dx < 2; dx++) {
									Block b = Screen.room.block[y - 1 + dy][x - 1 + dx];
									if (!isCellPlaceable(b, heldID)) {
										canPlace = false;
										break;
									}
								}
								if (!canPlace) break;
							}
							Block topLeft = Screen.room.block[y - 1][x - 1];
							int pw = Screen.room.blockSize * 2;
							int ph = Screen.room.blockSize * 2;

							g.setColor(canPlace ? new Color(0, 255, 0, 80) : new Color(255, 0, 0, 80));
							g.fillRect(topLeft.x, topLeft.y, pw, ph);
							g.setColor(canPlace ? Color.GREEN : Color.RED);
							g.drawRect(topLeft.x, topLeft.y, pw, ph);

							if (holdingItem != null) {
								g.drawImage(holdingItem, topLeft.x, topLeft.y, pw, ph, null);
							}
						}
					}
				}
			}
		}
	}
}
