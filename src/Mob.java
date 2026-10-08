import java.awt.*;

public class Mob extends Rectangle{

	int xC, yC; //  x,y kordinatları for mob
	protected int health;
	protected int maxHealth;
	protected int healthSpace = 3, healthHeight = 6;
	protected int mobSize = 32;
	protected int upward = 0, downward = 1, right = 2, left = 3;
	protected int walkFrame = 0;
	protected int walkSpeed;
	protected int spawnTime;
	protected int mobWalk = 0;
	protected int direction = right;
	protected int mobID = Value.MOB_PLACEHOLDER;
	protected int laneID = Value.MOB_PLACEHOLDER;
	protected int increPos = 0;
	protected int rand = 0;
	protected double attackDmg;
	protected Mob target;

	protected int coinDuration = 250; // thời gian animation đồng xu bay lên
	protected int coinTick = 0;
	protected int coinMaxFlyDist = 15; // độ cao đồng xu bay lên
	

	protected boolean inGame = false;
	protected boolean isMatching = false;
	protected boolean hasDealtDamage = false;

	// scale kích thước cho một số mob có frame ảnh rộng
	protected double renderScale;
	protected double dmgReduction;
	protected boolean isDying = false;
	protected int deadFrame = 0;
	protected int deadSpeed = 35; // Giá trị càng lớn càng chậm
	protected int deadTick = 0;
	protected int deadDelay = 0;
	protected int deadDelayLimit = 1500; // Delay tại frame cuối trước khi biến mất (~1.5 đến 2 giây)
	
	Mob() {
		this.walkSpeed = 20; // lower is faster
		this.spawnTime = 1600;
		this.renderScale = 1.5;
		this.dmgReduction = 0;
		this.attackDmg = 1;
	}
    
	void spawnMob(int mobID) { //full con này đơn giản chỉ spawn mob ở 1 trong 3 ô 1,2,3 tại cột đầu tiên
		int[] spawnYByLane = {-1, -1, -1, -1};
		int laneCount = 0;
		for (int y = 0; y < Screen.room.block.length; y++) {
			int lane = Screen.room.block[y][0].groundID;
			if (lane >= 1 && lane <= 3 && spawnYByLane[lane] == -1) {
				spawnYByLane[lane] = y;
				laneCount++;
			}
		}
		if (laneCount == 0) {
			throw new IllegalStateException("Could not find a spawn lane in the first map column.");
		}

		int selectedLane = Screen.rand.nextInt(laneCount); 	
		//random đây nàyyyy
		// chỗ còn lại cơ bản thì nó đi đúng làn như trc thôi
		// tluc vip vl :>
		for (int lane = 1; lane <= 3; lane++) {
			if (spawnYByLane[lane] != -1 && selectedLane-- == 0) {
				laneID = lane;
				break;
			}
		}

		int spawnY = spawnYByLane[laneID];
		if (spawnY == -1) {
			throw new IllegalStateException("Could not find a spawn tile for lane " + laneID + ".");
		}

		Block spawnBlock = Screen.room.block[spawnY][0];
		setBounds(spawnBlock.x, spawnBlock.y, mobSize, mobSize);
		xC = 0;
		yC = spawnY;

		this.mobID = mobID;
		this.health = mobSize;
		this.maxHealth = health;

		direction = right;
		mobWalk = 0;
		walkFrame = 0;
		this.isDying = false;
		this.deadFrame = 0;
		this.deadTick = 0;
		this.deadDelay = 0;
		
		inGame = true;
		
	}
	
	void playerLoseHealth() {
		Screen.health -= 1;   
	}

	void physic() {
		if (isDying){ // tính toán frame cho animation mob chết
			if (coinTick < coinDuration){
				coinTick++;
			}

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
		
		checkMatching(Screen.mini);
		
		// if not fighting
		if (!isMatching){
			if(walkFrame >= walkSpeed) {
				if(direction == right) 
					x+=1;
				else if(direction == upward)
					y-=1;
				else if(direction == downward) 
					y+=1;
				else if(direction == left)
					x-=1;

				mobWalk +=1;
				
				if(mobWalk == Screen.room.blockSize) { // sağ yönüne gitmesini sağlıyor
					if(direction==right) {
						xC+=1;
					}else if(direction ==upward){
							yC-=1;
					}else if(direction == downward) {
							yC+=1;
					}else if(direction ==left) {
							xC -=1;
					}
					
					Block currentBlock = Screen.room.block[yC][xC];
					if (currentBlock.groundID == 4 || currentBlock.towerID == Value.BLACK_HOLE) {
						deleteMob();
						playerLoseHealth();
						return;
					}

					direction = findNextDirection();
					mobWalk = 0;
				}
				walkFrame=0;
			}
			else {
				walkFrame+=1;
			}
		}

		else{
			// Lấy chỉ số frame đánh hiện tại (chu kỳ animation đánh có 6 frame: 0 -> 5)
			int currentAttackFrame = Screen.AnimFrame % 6; 

			// Khi animation chạm frame thứ 5 và chưa gây sát thương trong lượt chém này
			if (currentAttackFrame == 5) {
				if (!hasDealtDamage) {
					attack();
					hasDealtDamage = true; // Đánh dấu đã chém trúng, không chém thêm trong frame này nữa
				}
			} else {
				// Khi animation chuyển sang frame khác (0, 1, 2, 3, 4), reset cờ để chuẩn bị cho đòn kế tiếp
				hasDealtDamage = false;
			}
		}
	}

	private int findNextDirection() {
		int[] directions = {right, upward, downward, left};
		int[] deltaX = {1, 0, 0, -1};
		int[] deltaY = {0, -1, 1, 0};
		// Avoid immediately reversing into the tile the mob just came from.
		int reverseDirection = direction == right ? left
				: direction == left ? right
				: direction == upward ? downward : upward;

		for (int i = 0; i < directions.length; i++) {
			if (directions[i] == reverseDirection) {
				continue;
			}
			int nextX = xC + deltaX[i];
			int nextY = yC + deltaY[i];
			if (nextY >= 0 && nextY < Screen.room.block.length
					&& nextX >= 0 && nextX < Screen.room.block[nextY].length) {
				int nextLane = Screen.room.block[nextY][nextX].groundID;
				if (nextLane == laneID || nextLane == 4) {
					return directions[i];
				}
			}
		}

		throw new IllegalStateException("Mob on lane " + laneID + " at (" + xC + ", " + yC
				+ ") has no valid next tile.");
	}
	   
	void loseHealth(double amo) {
		if (isDying) return;
		health -= amo;
		checkDeath();
	}
	   
	void checkDeath() {
		if (health <= 0){
			mobDead();
		}
	}
	  
	void checkMatching(Minion[] mini){
		for (int i = 0; i < Screen.mini.length; i++){
			if (this.intersects(mini[i]) && mini[i] != null && mini[i].inGame && !mini[i].isDead()){
				this.isMatching = true;
				this.target = mini[i];
				return;
			}
		}
		this.isMatching = false;
	}
	   
	boolean isDead() {
		return !inGame || isDying;
	}

	void deleteMob() {
		isDying = false;
		inGame = false;
		direction = right;
		mobWalk = 0;
	}

	void mobDead(){
		mobWalk = 0;
		isDying = true;
		deadFrame = 0;
		deadTick = 0;
		deadDelay = 0;
		
		this.coinTick = 0;
		Screen.killed++;
		// Screen.room.block[0][0].getMoney(mobID);
	}

	Image getSprite(){
		// dying
		if (isDying){
			if (Screen.mobOrcDeadAnim != null && Screen.mobOrcDeadAnim.length > 0){
				return Screen.mobOrcDeadAnim[deadFrame];
			}
		}
		// fighting
		if (isMatching){
			if (Screen.mobOrcAttack1Anim != null && Screen.mobOrcAttack1Anim.length > 0
			&& Screen.mobOrcAttack2Anim != null && Screen.mobOrcAttack2Anim.length > 0){
				if (Screen.AnimFrame % Screen.mobOrcAttack1Anim.length == 0) this.rand = (int) (Math.random()*2);
				if (rand == 0)
					return Screen.mobOrcAttack1Anim[Screen.AnimFrame % Screen.mobOrcAttack1Anim.length];
				return Screen.mobOrcAttack2Anim[Screen.AnimFrame % Screen.mobOrcAttack2Anim.length];
			}
		}
		// walking
		if (Screen.mobOrcWalkAnim != null && Screen.mobOrcWalkAnim.length > 0){
			return Screen.mobOrcWalkAnim[Screen.AnimFrame % Screen.mobOrcWalkAnim.length];
		}
		return Screen.mobOrcWalkAnim[0];
	}
	   
	void attack(){
		double dmg = attackDmg - (1*target.dmgReduction);
		if (target != null){
			target.loseHealth(dmg);
			if (target.isDead()){
				target = null;
				isMatching = false;
			}
		}
	}
	
	void draw(Graphics g) {
		if (!inGame) return;
        // Draws the current animation frame scaled to the tile/mob size
		// 1. Tính kích thước vẽ dựa trên hệ số phóng to
		int drawW = (int) (width * renderScale);
		int drawH = (int) (height * renderScale);

		// 2. Căn giữa theo trục X, và giữ đáy chân quái chạm sàn (không bị bay lơ lửng)
		int drawX = x - (drawW - width) / 2;
		int drawY = y - (drawH - height);

		Image Sprite = getSprite();
		if (Sprite != null){
            g.drawImage(Sprite, drawX, drawY, drawW, drawH, null);
		}

		if (isDying){
			if (coinDuration > 0 && coinTick < coinDuration) {
				double progress = (double) coinTick / coinDuration; // Giá trị từ 0.0 -> 1.0
				int coinOffsetY = (int) (progress * coinMaxFlyDist); // Bay dần lên trên
				
				int coinX = x + (width / 2) - (Store.iconSize / 2);
				int coinY = (y - coinOffsetY);

				g.drawImage(Screen.tileset_res[2], coinX, coinY, Store.iconSize, Store.iconSize, null);
			}
			return;
		}
		

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

	int getSpawnTime(){
		return spawnTime;
	}
	
	public void applyMultiplier(double hpMult, double speedMult) {
		this.maxHealth = (int)(this.maxHealth * hpMult);
		this.health = this.maxHealth;
		this.walkSpeed = (int)(this.walkSpeed / speedMult); // walkSpeed is frame delay
	}
}