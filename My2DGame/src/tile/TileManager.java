package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile;        //Array chứa ảnh từng ô
    int[][] mapTileNums; //Bảng chứa ma trận map




    public BufferedImage tileImage; //Ảnh nền
    public BufferedImage newGameImage;
    public BufferedImage quitGameImage;







    public TileManager(GamePanel gp) {
        this.gp = gp;           //Vẽ trên gamePanel nào?
        tile = new Tile[10];    //file ảnh max 10 ảnh (đá, cỏ, nước, đường đi, ...)
        mapTileNums = new int[gp.maxScreenRow][gp.maxScreenCol];

        getTileImage();
        loadMap();
    }

    public void loadMap(){
        InputStream is = getClass().getResourceAsStream("/res/maps/map01.txt");     //Format đọc file txt
        BufferedReader br = new BufferedReader(new InputStreamReader(is));  

        int col = 0;
        int row = 0;
        while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
            try {
                String line = br.readLine(); //Đọc từng dòng
                while (col < gp.maxScreenCol) {
                    String numbers[] = line.split(" "); //Tách từng số trong dòng
                    int num = Integer.parseInt(numbers[col]); //Chuyển từ String sang int
                    mapTileNums[row][col] = num; //Gán vào mảng mapTileNums
                    col++;
                }
                if (col == gp.maxScreenCol) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    public void getTileImage() {
        try {

            tileImage = ImageIO.read(getClass().getResourceAsStream("/res/tiles/game_tilee.png"));
            newGameImage = ImageIO.read(getClass().getResourceAsStream("/res/tiles/newGame3.png"));
            quitGameImage = ImageIO.read(getClass().getResourceAsStream("/res/tiles/quitGame3.png"));

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/Grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall_grass.png"));
            
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/StonePath.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall_path.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall_1.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {


        if (gp.gameState == gp.playState) {
            int col = 0;
            int row = 0;
            int x=0;
            int y=0;

            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                int tileNum = mapTileNums[row][col];
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
                col++;
                x += gp.tileSize;

                if (col == gp.maxScreenCol) {
                    col = 0;
                     x = 0;
                    row++;
                    y += gp.tileSize;
                }
                
            } 
        }
        else if (gp.gameState == gp.tileState) {
            drawTileScreen(g2);
        }
        
    }  

    public void drawTileScreen(Graphics2D g2) {
        g2.drawImage(tileImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(newGameImage, 11*gp.tileSize, 7*gp.tileSize,4*gp.tileSize, 2*gp.tileSize, null);
        g2.drawImage(quitGameImage, 11*gp.tileSize, 9*gp.tileSize, 4*gp.tileSize, 2*gp.tileSize, null);
    }
}

    

