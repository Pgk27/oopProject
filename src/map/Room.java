package map;
import java.awt.Graphics;

import core.Screen;
import core.Constants;

public class Room {
	public int worldWidth = 40; 	//số cột
	public int worldHeight = 20;  	//số hàng
	public int blockSize = 30;		//độ dài cạnh tính theo pixel

	
	public Block[][] block;
	
	public Room() {
		define();
	}
	
	public void define() { // oyun alanını açılan penceredeki deki konumu
		block = new Block[worldHeight][worldWidth];

		for(int y=0;y<block.length;y++) {
			for(int x=0;x<block[0].length;x++) {
				block[y][x] = new Block((Screen.myWidth/2) - ((worldWidth*blockSize)/2) + (x * blockSize), y * blockSize, blockSize, blockSize, Constants.groundVoid, Constants.PLACEHOLDER);
			}
			
		}
	}
	
	public void physic() { // kulelerin oyun alanı içersindeki her yerde çalışmasını sağlıyor
		  for(int y = 0; y < block.length;y++) {	
			  for(int x = 0; x < block[0].length;x++) {
				  block[y][x].physic();
			  }
		  }
	}
	private void drawMapDanhNhau(Graphics g) {
		g.drawImage(Screen.tiles.mapDanhNhau[Screen.level], 10, 0, 1200, 600, null);
	}
	
	public void draw(Graphics g) { // kuleler ve groundları çiziyor.
		// for(int y=0;y<block.length;y++) {
		// 	for(int x=0;x<block[0].length;x++) {
		// 		block[y][x].drawGround(g);
		// 	}
		// }
		drawMapDanhNhau(g);
		for(int y=0;y<block.length;y++) {
			for(int x=0;x<block[0].length;x++) {
				block[y][x].drawTower(g);
			}
		}
	}
}
