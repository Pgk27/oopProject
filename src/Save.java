import java.io.*;
import java.util.*;

public class Save {
	public void loadSave(File loadPath) {
		try {
			Scanner loadScanner = new Scanner(loadPath);
		
			while(loadScanner.hasNext()) {
				Screen.killsToWin = loadScanner.nextInt();	
				if(Screen.level == 1 ) {
					Screen.killsToWin = 10;
					Screen.level += 1;		
				}else if(Screen.level == 2) {                                  
					Screen.killsToWin = 20;
					Screen.level += 1;
				}else if(Screen.level == 3) {
					Screen.killsToWin = 30;
					Screen.level += 1;
				}

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
