import java.awt.*;
public class Mob3 extends Mob {
	Mob3(){
		super();
		this.walkSpeed = 3;
		this.spawnTime = 1200;
		this.renderScale = 1.8;
		this.dmgReduction = 0.50;
		this.attackDmg = 1.5;
		this.attackSpeed = 0.65;
		this.totalCombatFrames = Screen.mobSlimeAttack1Anim.length;
		this.hitFrame = Screen.mobSlimeAttack1Anim.length - 1;
		this.attackSound = Screen.sound.sfxBite;
	}
	
	@Override 
	Image getSprite(){
		if (isDying){
			if (Screen.mobSlimeDeadAnim != null && Screen.mobSlimeDeadAnim.length > 0){
				return Screen.mobSlimeDeadAnim[deadFrame];
			}
		}
		if (isMatching){
			int frame = currentAttackFrame % Screen.mobSlimeAttack1Anim.length;
			if (Screen.mobSlimeAttack1Anim != null && Screen.mobSlimeAttack1Anim.length > 0
			&& Screen.mobSlimeAttack2Anim != null && Screen.mobSlimeAttack2Anim.length > 0){
				if (animFrame % Screen.mobSlimeAttack1Anim.length == 0) this.rand = (int) (Math.random()*2);
				if (rand == 0)
					return Screen.mobSlimeAttack1Anim[frame];
				return Screen.mobSlimeAttack2Anim[frame];
			}
		}
		if (Screen.mobSlimeWalkAnim != null && Screen.mobSlimeWalkAnim.length > 0){
			return Screen.mobSlimeWalkAnim[animFrame % Screen.mobSlimeWalkAnim.length];
		}
		return Screen.mobSlimeWalkAnim[0];
	}
}

