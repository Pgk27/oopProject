import java.awt.*;

public class Minion extends Mob {

    Minion(){    
		this.renderScale = 2.0;
		this.dmgReduction = 0;     
	}
	
	@Override
	void physic(){
		if (isDying){ // tính toán frame cho animation mob chết
			if (Screen.mobOrcDeadAnim != null && Screen.mobOrcDeadAnim.length > 0){
				if (deadFrame < Screen.mobOrcDeadAnim.length - 1){
					deadTick++;
					if (deadTick >= deadSpeed){
						deadFrame++;
						deadTick = 0;
					}
				}
				else{
					deadDelay++;
					if(deadDelay >= deadDelayLimit){
						deleteMob();
					}
				}
			}
			else{
				deleteMob();
			}
			return;
		}
	}

	@Override
	void mobDead(){
		isDying = true;
		deadFrame = 0;
		deadTick = 0;
		deadDelay = 0;
	}

	@Override
	void deleteMob() {
		isDying = false;
		inGame = false;
	}


	void spawnMob(int mobID) {
		int guardX = -1;
		int guardY = -1;
		int[] deltaX = {0, 0, -1, 1};
		int[] deltaY = {-1, 1, 0, 0};

		for (int y = 0; y < Screen.room.block.length; y++) {
			for (int x = 0; x < Screen.room.block[y].length; x++) {
				if (Screen.room.block[y][x].towerID != Value.BLACK_HOLE) {
					continue;
				}

				for (int direction = 0; direction < deltaX.length; direction++) {
					int roadX = x + deltaX[direction];
					int roadY = y + deltaY[direction];
					if (roadY < 0 || roadY >= Screen.room.block.length
							|| roadX < 0 || roadX >= Screen.room.block[roadY].length
							|| Screen.room.block[roadY][roadX].groundID != Value.groundRoad) {
						continue;
					}

					if (guardX != -1 && (guardX != roadX || guardY != roadY)) {
						throw new IllegalStateException("The gate has more than one adjacent road block.");
					}
					guardX = roadX;
					guardY = roadY;
				}
			}
		}

		if (guardX == -1) {
			throw new IllegalStateException("Could not find a road block next to the gate for the Minion.");
		}

		Block guardBlock = Screen.room.block[guardY][guardX];
		setBounds(guardBlock.x, guardBlock.y, mobSize, mobSize);
		xC = guardX;
		yC = guardY;

		this.mobID = mobID;
		this.health = mobSize;
		this.maxHealth = health;

		this.isDying = false;
		this.deadFrame = 0;
		this.deadTick = 0;
		this.deadDelay = 0;
		
		inGame = true;
	}

	@Override
	void checkMatching(Minion[] mob){
	}

	Image getSprite(){
		if (isDying){ // dying mob animation
			if (Screen.minionDeadAnim != null && Screen.minionDeadAnim.length > 0){
				return Screen.minionDeadAnim[deadFrame];
			}
		}
		// mob's normal walking animation
		if (Screen.minionIdleAnim != null && Screen.minionIdleAnim.length > 0){
			return Screen.minionIdleAnim[Screen.AnimFrame % Screen.minionIdleAnim.length];
		}
		return Screen.minionIdleAnim[0];
	}

	void draw(Graphics g) {
		if (!inGame) return;
        // Draws the current animation frame scaled to the tile/mob size

		Image Sprite = getSprite();
		if (Sprite != null){
			// 1. Tính kích thước vẽ dựa trên hệ số phóng to
            int drawW = (int) (width * renderScale);
            int drawH = (int) (height * renderScale);

            // 2. Căn giữa theo trục X, và giữ đáy chân quái chạm sàn (không bị bay lơ lửng)
            int drawX = x - (drawW - width) / 2;
            int drawY = y - (drawH - height);

            g.drawImage(Sprite, drawX, drawY, drawW, drawH, null);
		}

		if (isDying) return;

		int barY = y - (healthSpace + healthHeight);
		double healthPercent = (double) health / maxHealth;
		int currentBarWidth = (int) (healthPercent * width);

		// 1. Dark gray / black background (empty track)
		g.setColor(new Color(40, 40, 40));
		g.fillRect(x, barY, width, healthHeight);

		// 2. Dynamic Color Selection based on health percentage
		if (healthPercent > 0.50) {
			g.setColor(new Color(50, 205, 50));   // Green (above 50%)
		} else if (healthPercent > 0.25) {
			g.setColor(new Color(255, 165, 0));  // Orange (25% - 50%)
		} else {
			g.setColor(new Color(220, 20, 60));   // Red (below 25% / Critical)
		}

		if (currentBarWidth > 0) {
			g.fillRect(x, barY, currentBarWidth, healthHeight);
		}

		// 3. Static border around the entire bar
		g.setColor(Color.BLACK);
		g.drawRect(x, barY, width - 1, healthHeight - 1);
	}
}
