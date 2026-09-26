import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GameRender {
    
    public void render(Graphics g, int width, int height) {
        switch (Screen.gameState) {
            case Screen.tileScreen:
                drawTileScreen(g, width, height);
                break;
            case Screen.playGame:
                drawPlayGame(g, width, height);
                break;
            case Screen.gameShop:
                drawStore(g, width, height);
                break;


            case Screen.settings:
                break;
            case Screen.selectSkill:
                break;
            
            case Screen.buyItem:
                break;
            case Screen.gachaHero:
                break;
        }
    }


    private void drawTileScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tileScreen, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.settingsButton, 5, 5, 35 , 35, null);
        g.drawImage(Screen.tiles.newGameButton, 500, 300, 144 , 72, null);
        g.drawImage(Screen.tiles.storeButton, 500, 375, 144 , 72, null);
        g.drawImage(Screen.tiles.quitGameButton, 500, 450, 144 , 72, null);

    }

    private void drawStore(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.storeTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);

    }    



    private void drawPlayGame(Graphics g, int width, int height) {
        
        g.setColor(new Color(70, 70, 70)); 
        g.fillRect(0, 0, width, height);
        
        Screen.room.draw(g); 
        
        for( int i = 0 ; i<Screen.mobs.length;i++) { 
              if(Screen.mobs[i].inGame) {
                  Screen.mobs[i].draw(g);
              }
        }
        
        for( int i = 0 ; i<Screen.mobss.length;i++) { 
              if(Screen.mobss[i].inGame) {
                  Screen.mobss[i].draw(g);
              }
        }
        
        for( int i = 0 ; i<Screen.mobsss.length;i++) { 
              if(Screen.mobsss[i].inGame) {
                  Screen.mobsss[i].draw(g);
              }
        }

        for (int i = 0; i < Screen.mini.length; i++){
            if (Screen.mini[i].inGame){
                Screen.mini[i].draw(g);
            }
        }
        
        Screen.store.draw(g); 
        
        if(Screen.health < 1) {
            g.setColor(new Color(240,20,20));
            g.fillRect(0, 0, Screen.myWidth, Screen.myHeight);
            g.setColor(new Color(225,255,255));
            g.setFont(new Font("Courier New",Font.BOLD,14));
            g.drawString("Game Over, Unlucky...:(", 10, 20);
        }
        
        if(Screen.isWin) {
            g.setColor(new Color(255,255,255)); 
            g.fillRect(0, 0, Screen.myWidth, Screen.myHeight);  // Đổi thành width, height
            g.setColor(new Color(0,0,0));  
            g.setFont(new Font("Courier New",Font.BOLD,14));                    
            if(Screen.level  >   Screen.maxlevel) {           
                g.drawString("You won the whole game! Please wait and the window will close...", 10, 20);
            }else {
                g.drawString("You won! Congratulations! Please wait for the next level...", 10, 20);
            }
        }
    }


}