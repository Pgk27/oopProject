package entity.enemy;
import java.awt.*;
import core.Screen;


/**
 * Lớp cha cho tất cả các con quái
 * (Orc, Demon, Slime...)
 */

public class OrcMob extends Mob{
	// scale kích thước cho một số mob có frame ảnh rộng
	public OrcMob() {
		this.walkSpeed = 2; // lower is faster
		this.spawnTime = 1600;
		this.renderScale = 1.5;
		this.dmgReduction = 0;
		this.attackDmg = 10;
		this.attackSpeed = 0.75;
		this.totalCombatFrames = Screen.mobOrcAttack1Anim.length;
		this.hitFrame = Screen.mobOrcAttack1Anim.length - 1;
		this.attackSound = Screen.sound.sfxSlash;
		this.deathSound = Screen.sound.sfxDeath;
	}

	public Image getSprite(){
		// dying
		if (isDying){
			if (Screen.mobOrcDeadAnim != null && Screen.mobOrcDeadAnim.length > 0){
				return Screen.mobOrcDeadAnim[deadFrame];
			}
		}
		// fighting
		if (isMatching && isAttacking){
			int frame = currentAttackFrame % Screen.mobOrcAttack1Anim.length;
			if (Screen.mobOrcAttack1Anim != null && Screen.mobOrcAttack1Anim.length > 0
			&& Screen.mobOrcAttack2Anim != null && Screen.mobOrcAttack2Anim.length > 0){
				if (rand == 0)
					return Screen.mobOrcAttack1Anim[frame];
				return Screen.mobOrcAttack2Anim[frame];
			}
		}
		// idle (when in combat but waiting for next attack, or idle animation)
		if (isMatching){
			if (Screen.mobOrcIdleAnim != null && Screen.mobOrcIdleAnim.length > 0 && Screen.mobOrcIdleAnim[0] != null){
				return Screen.mobOrcIdleAnim[animFrame % Screen.mobOrcIdleAnim.length];
			}
		}
		// walking
		if (Screen.mobOrcWalkAnim != null && Screen.mobOrcWalkAnim.length > 0){
			return Screen.mobOrcWalkAnim[animFrame % Screen.mobOrcWalkAnim.length];
		}
		return Screen.mobOrcWalkAnim[0];
	}
}