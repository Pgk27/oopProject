import java.awt.Image;

public class Mercenary extends Minion{
	int rootX = -1;
	int rootY = -1;

    public Mercenary() {
        this.renderScale = 1.8;
		this.dmgReduction = 0; 
		this.attackDmg = 2;
		this.attackSpeed = 1.5;
        this.mobSize = 32;
		this.totalCombatFrames = Screen.mercenaryAttackAnim.length;
		this.hitFrame = Screen.mercenaryAttackAnim.length - 1;
		this.attackSound = Screen.sound.sfxPierce;
    }

    public void spawnMob(int x, int y, int rootX, int rootY, int mobID){
        setBounds(x, y, mobSize, mobSize);
        this.mobID = mobID;
		this.health = mobSize;
		this.maxHealth = health;

		// set ngẫu nhiên frame cho từng con mercenary (ngẫu nhiên hóa chuyển động)
		this.animFrame = Screen.rand.nextInt(8);
		this.animTick = Screen.rand.nextInt(20);
		this.animSpeed = 36 + Screen.rand.nextInt(10);

		this.isDying = false;
		this.deadFrame = 0;
		this.deadTick = 0;
		this.deadDelay = 0;
		this.rootX = rootX;
		this.rootY = rootY;
		this.attackRangeBox.setBounds(x + 2, y + 2, mobSize / 2 - 2, mobSize - 4); 
		
        inGame = true;
    }

    @Override
    public void physic() {
        if (isDying){ // tính toán frame cho animation mob chết
			if (Screen.mercenaryDeadAnim != null && Screen.mercenaryDeadAnim.length > 0){
				if (deadFrame < Screen.mercenaryDeadAnim.length - 1){
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
		
		if (this.target != null && (this.target.isDead() || !this.target.inGame || !this.attackRangeBox.intersects(this.target))){
			this.target = null;
		}

		isMatching = checkMatching(Screen.mobs, Screen.mobss, Screen.mobsss);

		if(isMatching){
			// int currentAttackFrame = animFrame % 6;

			// // Khi animation chạm frame thứ 5 và chưa gây sát thương trong lượt chém này
			// if (currentAttackFrame == 5) {
			// 	if (!hasDealtDamage) {
			// 		attack();
			// 		hasDealtDamage = true; // Đánh dấu đã chém trúng, không chém thêm trong frame này nữa
			// 	}
			// } else {
			// 	// Khi animation chuyển sang frame khác (0, 1, 2, 3, 4), reset cờ để chuẩn bị cho đòn kế tiếp
			// 	hasDealtDamage = false;
			// }
			updateCombat(totalCombatFrames, hitFrame);
		}
		else{
			lastAttackTime = 0;
			hasDealtDamage = false;
		}
    }

	@Override
	public void mobDead(){
		isDying = true;
		deadFrame = 0;
		deadTick = 0;
		deadDelay = 0;
		Screen.room.block[rootY][rootX].towerID = Value.PLACEHOLDER;
	}

	@Override
	boolean checkMatching(Mob[]... mobGroups){
		// if the current target is still in Game & in range & not dead, LOCK ON it
		if (this.target != null && this.target.inGame && this.attackRangeBox.intersects(this.target) &&  !this.target.isDead()){
			return true;
		}

		// otherwise, switch to a new target
		for (Mob[] mob : mobGroups){
			for (int i = 0; i < mob.length; i++){
				if (this.attackRangeBox.intersects(mob[i]) && mob[i] != null && mob[i].inGame && !mob[i].isDead()){
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
			if (Screen.mercenaryDeadAnim != null && Screen.mercenaryDeadAnim.length > 0){
				return Screen.mercenaryDeadAnim[deadFrame];
			}
		}
		if (isMatching){
			int frame = currentAttackFrame % Screen.mobOrcAttack1Anim.length;
			if (Screen.mercenaryAttackAnim != null && Screen.mercenaryAttackAnim.length > 0){
				return Screen.mercenaryAttackAnim[frame];
			}
		}
        if (Screen.mercenaryIdleAnim != null && Screen.mercenaryIdleAnim.length > 0){
			return Screen.mercenaryIdleAnim[animFrame % Screen.mercenaryIdleAnim.length];
		}
		return Screen.mercenaryIdleAnim[0];
    }
}
