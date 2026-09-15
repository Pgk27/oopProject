import java.awt.*;
public class Mob2 extends Mob {
	Mob2() {
		super();
		this.walkSpeed = 5;
	}

	@Override
	Image getSprite(){
		if (Screen.mobDemonWalk != null && Screen.mobDemonWalk.length > 0){
			return Screen.mobDemonWalk[Screen.AnimFrame];
		}
		return Screen.tileset_mob[mobID];
	}
}