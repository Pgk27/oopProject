import java.awt.*;
public class Mob2 extends Mob {
	Mob2() {
		super();
		this.walkSpeed = 5;
		this.spawnTime = 1200;
		this.renderScale = 1.3;
		this.dmgReduction = 0.25;
		this.attackDmg = 1.2;
	}

	@Override
	Image getSprite(){
		if (isDying){
			if (Screen.mobDemonDeadAnim != null && Screen.mobDemonDeadAnim.length > 0){
				return Screen.mobDemonDeadAnim[deadFrame];
			}
		}
		if (isMatching){
			if (Screen.mobDemonAttack1Anim != null && Screen.mobDemonAttack1Anim.length > 0
			&& Screen.mobDemonAttack2Anim != null && Screen.mobDemonAttack2Anim.length > 0){
				if (Screen.AnimFrame % Screen.mobDemonAttack1Anim.length == 0) this.rand = (int) (Math.random()*2);
				if (rand == 0)
					return Screen.mobDemonAttack1Anim[Screen.AnimFrame % Screen.mobDemonAttack1Anim.length];
				return Screen.mobDemonAttack2Anim[Screen.AnimFrame % Screen.mobDemonAttack2Anim.length];
			}
		}
		if (Screen.mobDemonWalkAnim != null && Screen.mobDemonWalkAnim.length > 0){
			return Screen.mobDemonWalkAnim[Screen.AnimFrame];
		}
		return Screen.mobDemonWalkAnim[0];
	}
}