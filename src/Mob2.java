import java.awt.*;
public class Mob2 extends Mob {
	Mob2() {
		super();
		this.walkSpeed = 5;
		this.spawnTime = 1200;
		this.renderScale = 1.0;
	}

	@Override
	Image getSprite(){
		if (isDying){
			if (Screen.mobDemonDead != null && Screen.mobDemonDead.length > 0){
				return Screen.mobDemonDead[deadFrame];
			}
		}
		if (Screen.mobDemonWalk != null && Screen.mobDemonWalk.length > 0){
			return Screen.mobDemonWalk[Screen.AnimFrame];
		}
		return Screen.tileset_mob[mobID];
	}
}