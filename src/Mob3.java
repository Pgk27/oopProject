import java.awt.*;
public class Mob3 extends Mob {
	Mob3(){
		super();
		this.walkSpeed = 3;
		this.spawnTime = 1200;
		this.renderScale = 1.8;
		this.dmgReduction = 0.50;
		this.attackDmg = 1.5;
	}
	
	@Override 
	Image getSprite(){
		if (isDying){
			if (Screen.mobSlimeDeadAnim != null && Screen.mobSlimeDeadAnim.length > 0){
				return Screen.mobSlimeDeadAnim[deadFrame];
			}
		}
		if (isMatching){
			if (Screen.mobSlimeAttack1Anim != null && Screen.mobSlimeAttack1Anim.length > 0
			&& Screen.mobSlimeAttack2Anim != null && Screen.mobSlimeAttack2Anim.length > 0){
				if (Screen.AnimFrame % Screen.mobSlimeAttack1Anim.length == 0) this.rand = (int) (Math.random()*2);
				if (rand == 0)
					return Screen.mobSlimeAttack1Anim[Screen.AnimFrame % Screen.mobSlimeAttack1Anim.length];
				return Screen.mobSlimeAttack2Anim[Screen.AnimFrame % Screen.mobSlimeAttack2Anim.length];
			}
		}
		if (Screen.mobSlimeWalkAnim != null && Screen.mobSlimeWalkAnim.length > 0){
			return Screen.mobSlimeWalkAnim[Screen.AnimFrame];
		}
		return Screen.mobSlimeWalkAnim[0];
	}
}

