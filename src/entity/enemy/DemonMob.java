package entity.enemy;
import java.awt.*;

import core.Screen;
public class DemonMob extends Mob {
	public DemonMob() {
		super();
		this.walkSpeed = 5;
		this.spawnTime = 1200;
		this.renderScale = 1.3;
		this.dmgReduction = 0.25;
		this.attackDmg = 1.5;
		this.attackSpeed = 0.75;
		this.totalCombatFrames = Screen.mobDemonAttack1Anim.length;
		this.hitFrame = Screen.mobDemonAttack1Anim.length-2;
		this.attackSound = Screen.sound.sfxSlash;
	}

	@Override
	public Image getSprite(){
		if (isDying){
			if (Screen.mobDemonDeadAnim != null && Screen.mobDemonDeadAnim.length > 0){
				return Screen.mobDemonDeadAnim[deadFrame];
			}
		}
		if (isMatching && isAttacking){
			int frame = currentAttackFrame % Screen.mobDemonAttack1Anim.length;
			if (Screen.mobDemonAttack1Anim != null && Screen.mobDemonAttack1Anim.length > 0
			&& Screen.mobDemonAttack2Anim != null && Screen.mobDemonAttack2Anim.length > 0){
				if (rand == 0)
					return Screen.mobDemonAttack1Anim[frame];
				return Screen.mobDemonAttack2Anim[frame];
			}
		}
		if (isMatching){
			if (Screen.mobDemonIdleAnim != null && Screen.mobDemonIdleAnim.length > 0 && Screen.mobDemonIdleAnim[0] != null){
				return Screen.mobDemonIdleAnim[animFrame % Screen.mobDemonIdleAnim.length];
			}
		}
		if (Screen.mobDemonWalkAnim != null && Screen.mobDemonWalkAnim.length > 0){
			return Screen.mobDemonWalkAnim[animFrame % Screen.mobDemonWalkAnim.length];
		}
		return Screen.mobDemonWalkAnim[0];
	}
}