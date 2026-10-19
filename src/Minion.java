import java.awt.*;

public class Minion extends Mob {
	private final long HEAL_COOL_DOWN = 1000; // bỏ final nếu sau này muốn chỉnh cooldown xuống bằng gacha!?
	private int healAmount = 5;
	private long lastHealTime = 0;

	private boolean isShooting = false;
	private boolean hasShotArrow = false;
	private Mob shootTarget = null;
	private double arrowDmg = 10;
	private int shootRange = 300;
	private Rectangle rangeBox = new Rectangle();

    Minion(){    
		this.renderScale = 1.8;
		this.dmgReduction = 0;
		this.attackDmg = 20;
		this.mobSize = 80;
	}

	void spawnMob(int mobID) {
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
					if (block.towerID == Value.CASTLE) {
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
		this.rangeBox.setBounds(x - shootRange, y - shootRange/ 2, width + shootRange * 2, height + shootRange);
		// set tầm đánh là sprite của minion luôn
		this.attackRangeBox.setBounds(x, y, mobSize, mobSize); 

		this.mobID = mobID;
		this.health = mobSize;
		this.maxHealth = health;

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

		isMatching = checkMatching(Screen.mobs, Screen.mobss, Screen.mobsss);
		isShooting = !isMatching; // nếu không bắn cung thì đánh cận chiến và ngược lại

		if(isMatching){
			int currentAttackFrame = animFrame % 6;

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
		} else{
			// check if target is still alive and still inside the rangeBox before finding a new one
			if (shootTarget != null && (shootTarget.isDead() || !shootTarget.inGame || !rangeBox.intersects(shootTarget))){
				shootTarget = null;
			}

			if (shootTarget == null)
				shootTarget = findTargetInRange(Screen.mobs, Screen.mobss, Screen.mobsss);
			isShooting = (shootTarget != null); // nếu target chết thì isShooting = false
		}
		
		if (isShooting){
			int currentShootFrame = animFrame % 8;
			if (currentShootFrame == 7){
				shootArrow();
				hasShotArrow = true;
			}
			else hasShotArrow = false;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastGotDamagedTime >= 2000 && gotDamage) gotDamage = false; 
		if (currentTime - lastHealTime >= HEAL_COOL_DOWN && !gotDamage){
			selfHeal();
			lastHealTime = currentTime;
		}
	}

	private void shootArrow(){
		double finalDamage = arrowDmg * (1 - shootTarget.dmgReduction);
		double startX = x + width / 2;
		double startY = y + height / 2;
		if (shootTarget != null && !hasShotArrow){
			Screen.pjt.add(new Projectiles(startX, startY, shootTarget, finalDamage, Value.MINION_SOLDIER));
		}
	}

	private Mob findTargetInRange(Mob[]... mobGroups){
		Mob closest = null;
		double minDist = Integer.MAX_VALUE;

		double myCenterX = this.x + this.width / 2.0;
		double myCenterY = this.y + this.height / 2.0;

		for (Mob[] group : mobGroups){
			for (int i = 0; i < group.length; i++){
				Mob m = group[i];
				if (m != null && m.inGame && !m.isDead()){
					if (rangeBox.intersects(m)){
						double targetCenterX = m.x + m.width;
						double targetCenterY = m.y + m.height;
						double dx = myCenterX - targetCenterX;
						double dy = myCenterY - targetCenterY;
						double distSq = dx * dx + dy *dy;
						if (distSq < minDist){
							minDist = distSq;
							closest = m;
						}
					}
				}
			}
		}
		return closest;
	}

	@Override
	public void mobDead(){
		isDying = true;
		deadFrame = 0;
		deadTick = 0;
		deadDelay = 0;
	}

	@Override
	public void deleteMob() {
		isDying = false;
		inGame = false;
	}

	private void selfHeal(){
		if (health + healAmount >= maxHealth){
			health = maxHealth;
		}
		else health += healAmount;
	}
	
	@Override
	boolean checkMatching(Mob[]... mobGroups){
		// if the current target is still in Game & in range & not dead, LOCK ON it
		if (this.target != null && this.target.inGame && this.intersects(this.target) &&  !this.target.isDead()){
			return true;
		}

		// otherwise, switch to a new target
		for (Mob[] mob : mobGroups){
			for (int i = 0; i < mob.length; i++){
				if (this.intersects(mob[i]) && mob[i] != null && mob[i].inGame && !mob[i].isDead()){
					this.isMatching = true;
					this.target = mob[i];
					this.target.gotDamage = true;
					this.target.lastGotDamagedTime = System.currentTimeMillis();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Image getSprite(){
		if (isDying){ // dying mob animation
			if (Screen.minionDeadAnim != null && Screen.minionDeadAnim.length > 0){
				return Screen.minionDeadAnim[deadFrame];
			}
		}
		// fighting
		if (isMatching){
			if (Screen.minionAttack1Anim != null && Screen.minionAttack1Anim.length > 0
			&& Screen.minionAttack2Anim != null && Screen.minionAttack2Anim.length > 0){
				if (animFrame % Screen.minionAttack1Anim.length == 0) this.rand = (int) (Math.random()*2);
				if (rand == 0)
					return Screen.minionAttack1Anim[animFrame % Screen.minionAttack1Anim.length];
				return Screen.minionAttack2Anim[animFrame % Screen.minionAttack2Anim.length];
			}
		}
		if (isShooting){
			if (Screen.minionShootingAnim != null && Screen.minionShootingAnim.length > 0){
				return Screen.minionShootingAnim[animFrame % Screen.minionShootingAnim.length];
			}
		}
		// mob's normal walking animation
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
            int drawW = (int) (width * renderScale);
            int drawH = (int) (height * renderScale);

            // 2. Căn giữa theo trục X, và giữ đáy chân quái chạm sàn (không bị bay lơ lửng)
            int drawX = x - (drawW - width) / 2;
            int drawY = y - (drawH - height);

			// quay sang trái
            g.drawImage(Sprite, drawX + drawW, drawY, -drawW, drawH, null);
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
