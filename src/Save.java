import java.io.*;
import java.util.*;

public class Save {
	public void loadSave(File loadPath) {
		try {
			Scanner loadScanner = new Scanner(loadPath);
		
			while(loadScanner.hasNext()) {
				Screen.killsToWin = loadScanner.nextInt();	
				// Screen.killsToWin = Screen.level * 10;
				Screen.killsToWin = 10;
				
				int width = loadScanner.nextInt();
				int height = loadScanner.nextInt();
				Screen.room = new Room(width, height);

				for(int y=0;y<Screen.room.block.length;y++) {
					for(int x=0;x<Screen.room.block[0].length;x++) {
						Screen.room.block[y][x].groundID = loadScanner.nextInt();
					}
				}
			
				for(int y=0;y<Screen.room.block.length;y++) {
					for(int x=0;x<Screen.room.block[0].length;x++) {
						Screen.room.block[y][x].airID = loadScanner.nextInt();
					}
				}
			}
			
			loadScanner.close();
		} catch(Exception e) { }
	}
}
