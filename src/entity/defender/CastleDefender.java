package entity.defender;
import java.awt.*;
import java.net.URL;

import core.Screen;
import core.Constants;
import entity.Projectiles;
import entity.enemy.Mob;
import map.Block;
import java.util.List;

public class CastleDefender extends Mob {
	// Stats
	private long HEAL_COOL_DOWN = getHealCooldown(); //tgian giữa các lần hồi hp
	private int healAmount = getHealAmount();  //lượng hp hồi 1 cục
	private long lastHealTime = 0;
	private double arrowDmg = getArrowDmg();  //st cung
	private int shootRange = 300;

	// Shooting Logic
	private long lastShootTime;
	private int currentShootFrame;
	private boolean isShootingArrow = false;
	private boolean isShooting = false;
	private boolean hasShotArrow = false;
	private Mob shootTarget = null;
	
	private Rectangle rangeBox = new Rectangle();

	protected boolean hasSpawned = false;

	private URL bowReleaseSound;

    public CastleDefender(){    
		this.renderScale = 1.8;
		this.dmgReduction = getDmgReduction(); //giảm st trừ thẳng
		this.attackDmg = getAttackDamage(); //st kiếm
		this.maxHealth = getMaxHealth();
		this.health = maxHealth;
		this.mobSize = 80;
		this.totalCombatFrames = Screen.minionAttack1Anim.length;
		this.hitFrame = Screen.minionAttack1Anim.length - 1;
		this.bowReleaseSound = Screen.sound.bowRelease;
	}

	public void spawnMob(int mobID) {
		if (this.hasSpawned) {
			return; // Only allow spawning once per minion instance
		}
		this.hasSpawned = true;

		Block destination = null;
		int destinationX = -1;
		int destinationY = -1;
		// check destination tile
		for (int y = 0; y < Screen.room.block.length; y++) {
			for (int x = 0; x < Screen.room.block[y].length; x++) {
				Block block = Screen.room.block[y][x];
				if (block.groundID == 4) {
					if (destination != null) {
						throw new IllegalStateException("The map has more than one destination tile.");
					}
					destination = block;
					destinationX = x;
					destinationY = y;
				}
			}
		}
		// check gate tile location
		if (destination == null) {
			for (int y = 0; y < Screen.room.block.length; y++) {
				for (int x = 0; x < Screen.room.block[y].length; x++) {
					Block block = Screen.room.block[y][x];
					if (block.towerID == Constants.CASTLE) {
						if (destination != null) {
							throw new IllegalStateException("The map has more than one gate tile.");
						}
						destination = block;
						destinationX = x;
						destinationY = y;
					}
				}
			}
		}

		if (destination == null) {
			throw new IllegalStateException("Could not find the destination tile for the Minion.");
		}
		int guardX = destinationX - 1;
		int guardY = destinationY;
		if (guardX < 0 || !Screen.room.block[guardY][guardX].isRoad()) {
			throw new IllegalStateException("Could not find a road tile immediately left of the destination for the Minion.");
		}

		// chỉnh vị trí sao cho NHÌN ở giữa nhất
		Block guardBlock = Screen.room.block[guardY-1][guardX-1];
		setBounds(guardBlock.x, guardBlock.y, mobSize, mobSize);
		xC = guardX;
		yC = guardY;

		// set tầm bắn
		this.rangeBox.setBounds(bounds.x - shootRange, bounds.y - shootRange/ 2, bounds.width + shootRange * 2, bounds.height + shootRange);
		// set tầm đánh là sprite của minion luôn
		this.attackRangeBox.setBounds(bounds.x, bounds.y, mobSize, mobSize); 

		// Stats
		this.mobID = mobID;
		this.maxHealth = getMaxHealth();
		this.health = this.maxHealth;
		this.dmgReduction = getDmgReduction();
		this.attackDmg = getSwordDmg();
		this.arrowDmg = getArrowDmg();
		this.HEAL_COOL_DOWN = getHealCooldown();
		this.healAmount = getHealAmount();

		// set ngẫu nhiên frame cho từng con mob (ngẫu nhiên hóa chuyển động)
		this.animFrame = Screen.rand.nextInt(8);
		this.animTick = Screen.rand.nextInt(20);
		this.animSpeed = 36 + Screen.rand.nextInt(10);

		this.isDying = false;
		this.deadFrame = 0;
		this.deadTick = 0;
		this.deadDelay = 0;
		this.lastHealTime = System.currentTimeMillis();
		
		inGame = true;
	}
	
	@Override
	public void physic(){
		if (isDying){ // tính toán frame cho animation mob chết
			if (Screen.minionDeadAnim != null && Screen.minionDeadAnim.length > 0){
				if (deadFrame < Screen.minionDeadAnim.length - 1){
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

		animTick++;
		if (animTick >= animSpeed){
			animFrame++;
			animTick = 0;
		}
		
		if (this.target != null && (this.target.isDead() || !this.target.inGame || !this.intersects(this.target))){
			this.target = null;
		}

		isMatching = checkMatching(Screen.activeMobs);
		isShooting = !isMatching; // nếu không bắn cung thì đánh cận chiến và ngược lại

		if(isMatching){
			updateCombat(totalCombatFrames, hitFrame);
		} else{
			lastAttackTime = 0;
			hasDealtDamage = false;

			// check if target is still alive and still inside the rangeBox before finding a new one
			if (shootTarget != null && (shootTarget.isDead() || !shootTarget.inGame || !rangeBox.intersects(shootTarget.getBounds()))){
				shootTarget = null;
			}

			if (shootTarget == null)
				shootTarget = findTargetInRange(Screen.activeMobs);
			isShooting = (shootTarget != null); // nếu target chết thì isShooting = false
		}
		
		if (isShooting){
			updateShootArrow();
		}
		else{
			lastShootTime = 0;
			hasShotArrow = false;
			isShootingArrow = false;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastGotDamagedTime >= 2000 && gotDamage) gotDamage = false; 
		if (currentTime - lastHealTime >= HEAL_COOL_DOWN && !gotDamage){
			selfHeal();
			lastHealTime = currentTime;
		}
	}

	private Mob findTargetInRange(List<Mob> mob){
		Mob closest = null;
		double minDist = Integer.MAX_VALUE;

		double myCenterX = this.bounds.x + this.bounds.width / 2.0;
		double myCenterY = this.bounds.y + this.bounds.height / 2.0;

		for (Mob m : mob){
			if (m != null && m.inGame && !m.isDead()) {
				if (rangeBox.intersects(m.getBounds())) {
					double targetCenterX = m.getX() + m.getWidth() / 2.0;
					double targetCenterY = m.getY() + m.getHeight() / 2.0;
					double dx = myCenterX - targetCenterX;
					double dy = myCenterY - targetCenterY;
					double distSq = dx * dx + dy * dy;
					if (distSq < minDist) {
						minDist = distSq;
						closest = m;
					}
				}
			}
		}
		return closest;
	}

	private void updateShootArrow(){
		long now = System.currentTimeMillis();
		long cooldownMs = (long) (1000.0 / attackSpeed);

		if (lastShootTime == 0){
			lastShootTime = now;
			hasShotArrow = false;
			isShootingArrow = true;
		}

		long elapsed = now - lastShootTime;
		if (elapsed >= cooldownMs){
			lastShootTime = now;
			hasShotArrow = false;
			elapsed = 0;
			isShootingArrow = true;
		}

		int totalShootingFrames = Screen.minionShootingAnim.length;
		int shootFrame = totalShootingFrames - 2;
		long frameDurationMs = animSpeed * 2;
		long shootAnimDurationMs = totalShootingFrames * frameDurationMs;

		if (elapsed < shootAnimDurationMs){
			isShootingArrow = true;
			currentShootFrame = (int) (elapsed / frameDurationMs);
			if (currentShootFrame >= shootFrame){
				currentShootFrame = shootFrame;
			}
		} else {
			isShootingArrow = false;
			currentShootFrame = shootFrame;
		}

		// chạy đến frame ra đòn và chưa gây sát thương --> bắn
		if (currentShootFrame >= shootFrame && !hasShotArrow){
			shootArrow();
			hasShotArrow = true;
		}

	}

	private double getHeroDamageMultiplier() {
		// Mercenary kế thừa Minion nhưng không nhận buff của hero.
		return mobID == Constants.MINION_SOLDIER && Screen.store != null
			? Screen.store.getHeroDamageMultiplier() : 1.0;
	}

	@Override
	public double getAttackDamage() {
		return super.getAttackDamage() * getHeroDamageMultiplier();
	}

	public boolean checkMatching(List<Mob> mob){
		// if the current target is still in Game & in range & not dead, LOCK ON it
		if (this.target != null && this.target.inGame && this.intersects(this.target) &&  !this.target.isDead()){
			return true;
		}

		// otherwise, switch to a new target
		for (Mob m : mob){
			if (m != null && m.inGame && !m.isDead() && this.intersects(m)){
				this.isMatching = true;
				this.target = m;
				this.target.gotDamage = true;
				this.target.lastGotDamagedTime = System.currentTimeMillis();
				return true;
			}
		}
		return false;
	}

	private void shootArrow(){
		double finalDamage = arrowDmg * getHeroDamageMultiplier() * (1 - shootTarget.dmgReduction);
		double startX = bounds.x + bounds.width / 2;
		double startY = bounds.y + bounds.height / 2;
		if (shootTarget != null && !hasShotArrow){
			Screen.pjt.add(new Projectiles(startX, startY, shootTarget, finalDamage, Constants.MINION_SOLDIER));
		}
		playShootingArrowSound();
	}

	private void playShootingArrowSound(){
		if (bowReleaseSound != null){
			Screen.sound.playSfx(bowReleaseSound);
		}
	}

	@Override
	public void mobDead(){
		isDying = true;
		isAttacking = false;
		isShootingArrow = false;
		deadFrame = 0;
		deadTick = 0;
		deadDelay = 0;
	}

	@Override
	public void deleteMob() {
		isDying = false;
		inGame = false;
		isAttacking = false;
		isShootingArrow = false;
	}

	private void selfHeal(){
		if (health + healAmount >= maxHealth){
			health = maxHealth;
		}
		else health += healAmount;
	}

	@Override
	public Image getSprite(){
		if (isDying){ // dying mob animation
			if (Screen.minionDeadAnim != null && Screen.minionDeadAnim.length > 0){
				return Screen.minionDeadAnim[deadFrame];
			}
		}
		// fighting
		if (isMatching && isAttacking){
			int frame = currentAttackFrame % Screen.minionAttack1Anim.length;
			if (Screen.minionAttack1Anim != null && Screen.minionAttack1Anim.length > 0
			&& Screen.minionAttack2Anim != null && Screen.minionAttack2Anim.length > 0){
				if (rand == 0)
					return Screen.minionAttack1Anim[frame];
				return Screen.minionAttack2Anim[frame];
			}
		}
		if (isShooting && isShootingArrow){
			int frame = currentShootFrame % Screen.minionShootingAnim.length;
			if (Screen.minionShootingAnim != null && Screen.minionShootingAnim.length > 0){
				return Screen.minionShootingAnim[frame];
			}
		}
		// idle animation
		if (Screen.minionIdleAnim != null && Screen.minionIdleAnim.length > 0){
			return Screen.minionIdleAnim[animFrame % Screen.minionIdleAnim.length];
		}
		return Screen.minionIdleAnim[0];
	}

	public void draw(Graphics g) {
		if (!inGame) return;
        // Draws the current animation frame scaled to the tile/mob size

		Image Sprite = getSprite();
		if (Sprite != null){
			// 1. Tính kích thước vẽ dựa trên hệ số phóng to
            int drawW = (int) (bounds.width * renderScale);
            int drawH = (int) (bounds.height * renderScale);

            // 2. Căn giữa theo trục X, và giữ đáy chân quái chạm sàn (không bị bay lơ lửng)
            int drawX = bounds.x - (drawW - bounds.width) / 2;
            int drawY = bounds.y - (drawH - bounds.height);

			// quay sang trái
            g.drawImage(Sprite, drawX + drawW, drawY, -drawW, drawH, null);
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

	// --- Dynamic Hero Stats (Merged from MinionStats) ---
	public static int getMaxHealth() {
		return 1200 + 100 * Screen.buffHp;
	}
	public static long getHealCooldown() {
		return 1000L * (100 - 2 * Screen.buffHpRe) / 100;
	}
	public static int getHealAmount() {
		return 150 + 5 * Screen.buffHpRe;
	}
	public static int getDmgReduction() {
		return Screen.buffShield;
	}
	public static int getArrowDmg() {
		return 150 + 20 * Screen.buffADmg;
	}
	public static int getSwordDmg() {
		return 200 + 25 * Screen.buffSDmg;
	}
	
}
