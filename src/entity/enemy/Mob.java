package entity.enemy;

import java.awt.*;
import java.net.URL;
import core.Screen;
import map.Block;
import core.Constants;

public abstract class Mob {
    // Composition instead of extending Rectangle
    protected Rectangle bounds = new Rectangle();
    protected Rectangle attackRangeBox = new Rectangle();

    // Frame calculation
    protected int animFrame = 0;
	protected int animTick = 0;
	protected int animSpeed = 40;

    // Logic & Status
    protected int spawnTime;
    protected int rand = 0;
    protected long lastAttackTime = 0;
    protected int currentAttackFrame = 0;
	protected int totalCombatFrames;
	protected int hitFrame;
    public boolean inGame = false;
    protected boolean isDying = false;
	public long lastGotDamagedTime;
	protected int deadFrame = 0;
	protected int deadSpeed = 35; // Giá trị càng lớn càng chậm
	protected int deadTick = 0;
	protected int deadDelay = 0;
	protected int deadDelayLimit = 1500; // Delay tại frame cuối trước khi biến mất (~1.5 đến 2 giây)
    
    // Stats
    protected double renderScale;
    public double health;
    protected double maxHealth;
    protected int mobSize = 32;
    protected int healthSpace = 3, healthHeight = 6;
    public double dmgReduction = 0;
    public double attackDmg;
    public double attackSpeed = 1.0;

    // Movement & Lane
    protected int mobID = Constants.PLACEHOLDER;
	protected int laneID = Constants.PLACEHOLDER;
    protected int upward = 0, downward = 1, right = 2, left = 3;
    protected int walkSpeed;
    protected int xC, yC;
    protected int direction = right;
    protected int mobWalk = 0;
    protected double walkFrame = 0;
    protected int increPos = 0;

    // Combat
    protected Mob target;
    protected boolean isMatching = false;
    protected boolean isAttacking = false;
    protected boolean hasDealtDamage = false;
    public boolean gotDamage = false;

    // Sfx
    protected URL attackSound;
	protected URL deathSound;

    // Bounds & coordinates getters
    public void setBounds(int x, int y, int width, int height) {
        bounds.setBounds(x, y, width, height);
    }

    // Check intersection with Tower/AttackRangeBox
    public boolean intersects(Rectangle r) {
        return r != null && bounds.intersects(r);
    }

    // Check intersection with another Mob/Defender
    public boolean intersects(Mob other) {
        return other != null && bounds.intersects(other.bounds);
    }

    public void playerLoseHealth() {
		Screen.health -= 1;   
	}

    // Common enemy movement, combat, and damage logic
    public void physic(){
        // set tầm đánh cho lớp Mob và các lớp con
		this.attackRangeBox.setBounds(this.bounds.x + mobSize / 2, this.bounds.y + 2, mobSize / 2 - 2, mobSize - 4); 

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

		// tăng frame, xúc tiến chuyển động
		animTick++;
		if (animTick >= animSpeed){
			animFrame++;
			animTick = 0;
		}
		
		isMatching = checkMatching(Screen.mini, Screen.merc);
		
		// if not fighting
		if (!isMatching){
			// reset combat khi không còn tham chiến
			lastAttackTime = 0;
			hasDealtDamage = false;
			isAttacking = false;

			// Keep fractional progress so every monster moves at 70% speed during Cursed Skull.
			walkFrame += Screen.store == null ? 1.0 : Screen.store.getMonsterSpeedMultiplier();
			if(walkFrame + 0.000001 >= walkSpeed + 1) {
				if(direction == right) move(1,0);
				else if(direction == upward) move(0,-1);
				else if(direction == downward) move(0, 1);
				else if(direction == left) move (-1, 0);

				mobWalk +=1;
				
				//🦛
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
					if (currentBlock.groundID == 4 || currentBlock.towerID == Constants.CASTLE) {
						deleteMob();
						playerLoseHealth();
						return;
					}

					direction = findNextDirection();
					mobWalk = 0;
				}
				walkFrame = Math.max(0, walkFrame - (walkSpeed + 1));
			}
		}
		else{
			updateCombat(totalCombatFrames, hitFrame);
		}
    }

    public void move(int dx, int dy){
        bounds.x += dx;
        bounds.y += dy;
    }

    public int findNextDirection() {
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

    public void spawnMob(int mobID) { //full con này đơn giản chỉ spawn mob ở 1 trong 3 ô 1,2,3 tại cột đầu tiên
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
		this.health = 1000;
		this.maxHealth = health;

		// set ngẫu nhiên frame cho từng con mob (ngẫu nhiên hóa chuyển động)
		this.animFrame = Screen.rand.nextInt(8);
		this.animTick = Screen.rand.nextInt(20);
		this.animSpeed = 36 + Screen.rand.nextInt(10); // biến này là TỐC ĐỘ của frame, cho dao động nhẹ 36 - 45 ticks

		direction = right;
		mobWalk = 0;
		walkFrame = 0;
		this.isDying = false;
		this.deadFrame = 0;
		this.deadTick = 0;
		this.deadDelay = 0;

		inGame = true;
		
	}
	   
    public void updateCombat(int totalFrames, int hitFrame){
		long now = System.currentTimeMillis();
		long cooldownMs = (long) (1000.0 / attackSpeed);

		if (lastAttackTime == 0){
			lastAttackTime = now;
			hasDealtDamage = false;
			isAttacking = true;
			// Chọn kiểu đánh (attack1 hoặc attack2) một lần duy nhất cho mỗi đòn đánh
			this.rand = (int) (Math.random() * 2);
		}

		long elapsed = now - lastAttackTime;
		if (elapsed >= cooldownMs){
			lastAttackTime = now;
			hasDealtDamage = false;
			elapsed = 0;
			isAttacking = true;
			this.rand = (int) (Math.random() * 2);
		}

		// Thời gian hiển thị 1 frame hoạt ảnh đánh (ví dụ 100ms)
		long frameDurationMs = animSpeed * 3; 
		long attackAnimDurationMs = totalFrames * frameDurationMs;

		if (elapsed < attackAnimDurationMs){
			isAttacking = true;
			currentAttackFrame = (int) (elapsed / frameDurationMs);
			if (currentAttackFrame >= totalFrames){
				currentAttackFrame = totalFrames - 1;
			}
		} else {
			// Hết thời gian vung đòn -> quay về trạng thái nghỉ/idle chờ cooldown
			isAttacking = false;
			currentAttackFrame = totalFrames - 1;
		}

		// chạy đến frame ra đòn và chưa gây sát thương --> chém
		if (currentAttackFrame >= hitFrame && !hasDealtDamage){
			attack();
			hasDealtDamage = true;
		}
	}

    public void attack(){
		if (target != null){
			double dmg = getAttackDamage() - (1*target.dmgReduction);
			playAttackSound();
			target.loseHealth(dmg);
			if (target.isDead()){
				target = null;
				isMatching = false;
			}
		}
	}

    public boolean checkMatching(Mob[]... defenderGroups) {
    for (Mob[] group : defenderGroups) {
        if (group == null) continue;
        for (int i = 0; i < group.length; i++) {
            Mob m = group[i];
            if (m != null && m.inGame && !m.isDead() && this.attackRangeBox.intersects(m.getBounds())) {
                this.target = m;
                this.target.isMatching = true;
                this.target.gotDamage = true;
                this.target.lastGotDamagedTime = System.currentTimeMillis();
                return true;
            }
        }
    }
    return false;
}

	public void loseHealth(double amount) {
		if (isDying) return;
		health -= amount;
		checkDeath();
	}
	   
	public void checkDeath() {
		if (health <= 0){
			mobDead();
		}
	}

    public boolean isDead() {
		return !inGame || isDying;
	}

	public void deleteMob() {
		isDying = false;
		inGame = false;
		direction = right;
		mobWalk = 0;
		isAttacking = false;
	}

	public void mobDead(){
		playDeathSound();
		mobWalk = 0;
		isDying = true;
		isAttacking = false;
		deadFrame = 0;
		deadTick = 0;
		deadDelay = 0;
		Screen.killed++;
	}
    
    // SFX Handler
    public void playAttackSound(){
		if(attackSound != null){
			Screen.sound.playSfx(attackSound);
		}
	}

	public void playDeathSound(){
		if (deathSound != null){
			Screen.sound.playSfx(deathSound);
		}
	}

    // Rendering logic
    public abstract Image getSprite(); // Every enemy provides its own sprites polymorphically
    
    public void draw(Graphics g) {
		if (!inGame) return;
        // Draws the current animation frame scaled to the tile/mob size
		// 1. Tính kích thước vẽ dựa trên hệ số phóng to
		int drawW = (int) (bounds.width * renderScale);
		int drawH = (int) (bounds.height * renderScale);

		// 2. Căn giữa theo trục X, và giữ đáy chân quái chạm sàn (không bị bay lơ lửng)
		int drawX = bounds.x - (drawW - bounds.width) / 2;
		int drawY = bounds.y - (drawH - bounds.height);

		if (direction == right){
			drawW = (int) (bounds.width * renderScale);
			drawX = bounds.x - (drawW - bounds.width) / 2;
		}
		// đảo ảnh sang trái khi di chuyển sang trái
		// khi lên trên/xuống dưới thì vẫn hướng sang phải
		else if(direction == left){
			drawX = bounds.x - (drawW - bounds.width) / 2 + drawW;
			drawW = (int) -(bounds.width * renderScale);
		}

		Image Sprite = getSprite();
		if (Sprite != null){
            g.drawImage(Sprite, drawX, drawY, drawW, drawH, null);
		}

		if (isDying) return;

		int barY = bounds.y - (healthSpace + healthHeight);
		double healthPercent = (double) health / maxHealth;
		int currentBarWidth = (int) (healthPercent * bounds.width);

		// 1. Dark gray / black background (empty track)
		g.setColor(new Color(40, 40, 40));
		g.fillRect(bounds.x, barY, bounds.width, healthHeight);

		// 2. Dynamic Color Selection based on health percentage
		if (healthPercent > 0.50) {
			g.setColor(new Color(50, 205, 50));   // Green (above 50%)
		} else if (healthPercent > 0.25) {
			g.setColor(new Color(255, 165, 0));  // Orange (25% - 50%)
		} else {
			g.setColor(new Color(220, 20, 60));   // Red (below 25% / Critical)
		}

		if (currentBarWidth > 0) {
			g.fillRect(bounds.x, barY, currentBarWidth, healthHeight);
		}

		// 3. Static border around the entire bar
		g.setColor(Color.BLACK);
		g.drawRect(bounds.x, barY, bounds.width - 1, healthHeight - 1);
	}
    
    public void applyMultiplier(double hpMult, double speedMult) {
		this.maxHealth = (int)(this.maxHealth * hpMult);
		this.attackSpeed *= 1.5;
		this.health = this.maxHealth;
		this.walkSpeed = (int)(this.walkSpeed / speedMult); // walkSpeed is frame delay
	}

    // Getters
    public Rectangle getBounds() { return bounds; }
    public int getX() { return bounds.x; }
    public int getY() { return bounds.y; }
    public int getWidth() { return bounds.width; }
    public int getHeight() { return bounds.height; }
    public double getAttackDamage() { return attackDmg; }
}