import java.awt.*;

public class Mob extends Rectangle{

	int xC, yC; //  x,y kordinatları for mob
	protected int health;
	protected int maxHealth;
	protected int healthSpace = 3, healthHeight = 6;
	protected int mobSize = 52;
	protected int upward = 0, downward = 1, right = 2, left = 3;
	protected int walkFrame = 0;
	int walkSpeed;
	int spawnTime;
	int mobWalk = 0;
	int direction = right;
	int mobID = Value.mobAir;
	boolean inGame = false;
	boolean hasUpward = false;
	boolean hasDownward = false;
	boolean hasLeft = false;
	boolean hasRight = false;
	
	Mob() {
		this.walkSpeed = 20; // lower is faster
		this.spawnTime = 1600;
	}
    
	void spawnMob(int mobID) { // 0,0 da başlıyacağını belirliyor
		 //determines that the mob will start at coordinate 0 a 0
		for(int y= 0; y<Screen.room.block.length; y++ ) { // loop through the left edge to check if there's a ground tile to spawn mob
			if(Screen.room.block[y][0].groundID == Value.groundRoad) {
				setBounds(Screen.room.block[y][0].x, Screen.room.block[y][0].y, mobSize, mobSize);
				xC = 0;
				yC = y;
			}
		}

		this.mobID = mobID;
		this.health = mobSize;
		this.maxHealth = health;
		
		inGame = true;
		
	}
	   
	void deleteMob() {
		inGame = false;
		direction = right;
		mobWalk = 0;
	}

	void mobDead(){
		inGame = false;
		direction = right;
		mobWalk = 0;
		Screen.room.block[0][0].getMoney(mobID);
	}
	
	void playerLoseHealth() {
		Screen.health -= 1;   
	}

	void physic() {
		if(walkFrame >= walkSpeed) {
			if(direction == right) 
				x+=1;
			else if(direction == upward)
				y-=1;
			else if(direction == downward) 
				y+=1;
			else if(direction == left)
				x-=1;

			mobWalk +=1;
			
			if(mobWalk == Screen.room.blockSize) { // sağ yönüne gitmesini sağlıyor
				if(direction==right) {
					xC+=1;
					hasRight = true;
					}else if(direction ==upward){
						yC-=1;
						hasUpward = true;
					}
					else if(direction == downward) {
						yC+=1;
						hasDownward = true;
					}else if(direction ==left) {
						xC -=1;
						hasLeft = true;
					}
				
				if(!hasUpward) {  // yolu izlemesini sağlıyor
				try {
					if(Screen.room.block[yC+1][xC].groundID == Value.groundRoad) {
						direction = downward;
						}
					}catch(Exception e) {}
				}  
			
				if(!hasDownward) {
					try {
						if(Screen.room.block[yC-1][xC].groundID == Value.groundRoad) {
							direction = upward;
						}
					}catch(Exception e) {}
				}
				
				if(!hasLeft) {
					try {
						if(Screen.room.block[yC][xC+1].groundID == Value.groundRoad) {
							direction = right;
						}
					}catch(Exception e) {}
				}

				if(!hasRight) {
					try {
						if(Screen.room.block[yC][xC-1].groundID == Value.groundRoad) {
							direction = left;
						}
					}
					catch(Exception e) {}
				}
				
				if(Screen.room.block[yC][xC].airID == Value.airblackHole) {// mob disappears when walking to the end point
					deleteMob();
					playerLoseHealth();
				}

				hasUpward= false;
				hasDownward = false;
				hasLeft = false;
				hasRight = false;
				mobWalk = 0;
			}
			walkFrame=0;
		}
		else {
			walkFrame+=1;
		}
	}
	   
	void loseHealth(int amo) {
		health -= amo;
		checkDeath();
	}
	   
	void checkDeath() {
		if(health <= 0)
			mobDead();
	}
	   
	   
	boolean isDead() {
		if(inGame)
			return false;
		else
			return true;
	}
	   
	Image getSprite(){
		if (Screen.mobOrcWalk != null && Screen.mobOrcWalk.length > 0){
			return Screen.mobOrcWalk[Screen.AnimFrame];
		}
		return Screen.tileset_mob[mobID];
	}
	   
	void draw(Graphics g) {
		if (!inGame) return;
        // Draws the current animation frame scaled to the tile/mob size

		Image Sprite = getSprite();
		if (Sprite != null)
        	g.drawImage(Sprite, x, y, width, height, null);


		int barY = y - (healthSpace + healthHeight);
		double healthPercent = (double) health / maxHealth;
		int currentBarWidth = (int) (healthPercent * width);

		// 1. Dark gray / black background (empty track)
		g.setColor(new Color(40, 40, 40));
		g.fillRect(x, barY, width, healthHeight);

		// 2. Dynamic Color Selection based on health percentage
		if (healthPercent > 0.50) {
			g.setColor(new Color(50, 205, 50));   // Green (above 50%)
		} else if (healthPercent > 0.25) {
			g.setColor(new Color(255, 165, 0));  // Orange (25% - 50%)
		} else {
			g.setColor(new Color(220, 20, 60));   // Red (below 25% / Critical)
		}

		if (currentBarWidth > 0) {
			g.fillRect(x, barY, currentBarWidth, healthHeight);
		}

		// 3. Static border around the entire bar
		g.setColor(Color.BLACK);
		g.drawRect(x, barY, width - 1, healthHeight - 1);
	}

	int getSpawnTime(){
		return spawnTime;
	}
}