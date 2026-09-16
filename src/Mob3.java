import java.awt.*;
public class Mob3 extends Mob {
	Mob3(){
		super();
		this.walkSpeed = 3;
		this.spawnTime = 1200;
	}

	@Override 
	Image getSprite(){
		if (Screen.mobCatRun != null && Screen.mobCatRun.length > 0){
			return Screen.mobCatRun[Screen.AnimFrame];
		}
		return Screen.tileset_mobbb[0];
	}
}

