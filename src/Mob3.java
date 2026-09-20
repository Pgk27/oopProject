import java.awt.*;
public class Mob3 extends Mob {
	Mob3(){
		super();
		this.walkSpeed = 3;
		this.spawnTime = 1200;
		this.renderScale = 1;
	}

	@Override 
	Image getSprite(){
		if (isDying){
			if (Screen.mobSlimeDead != null && Screen.mobSlimeDead.length > 0){
				return Screen.mobSlimeDead[deadFrame];
			}
		}
		if (Screen.mobSlimeWalk != null && Screen.mobSlimeWalk.length > 0){
			return Screen.mobSlimeWalk[Screen.AnimFrame];
		}
		return Screen.tileset_mobbb[0];
	}
}

