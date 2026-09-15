import java.awt.*;
public class Mob3 extends Mob {
	Mob3(){
		super();
		this.walkSpeed = 3;
	}

	@Override 
	public Image getSprite(){
		return Screen.tileset_mobbb[0];
	}
}

