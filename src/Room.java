import java.awt.Graphics;

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
				block[y][x] = new Block((Screen.myWidth/2) - ((worldWidth*blockSize)/2) + (x * blockSize), y * blockSize, blockSize, blockSize, Value.groundVoid, Value.PLACEHOLDER);
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
	
	public void draw(Graphics g) { // kuleler ve groundları çiziyor.
		for(int y=0;y<block.length;y++) {
			for(int x=0;x<block[0].length;x++) {
				block[y][x].draw(g);
			}
		}
		for(int y=0;y<block.length;y++) { // lazerlerin görünmesini sağlıyor, çiziyor
			for(int x=0;x<block[0].length;x++) {
				block[y][x].fight(g);
			}	
		}
	}
}
