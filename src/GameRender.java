import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

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
            case Screen.gacha:
                drawGachaScreen(g, width, height);
                break;
            case Screen.gachaRate:
                drawGachaRateScreen(g, width, height);
                break;
            case Screen.shardShop:
                drawShardShop(g, width, height);
                break;
            case Screen.thongBao:
                drawThongBao(g, width, height);
                break;
            case Screen.tongKetWin:
                drawTongKetWin(g, width, height);
                break;
            case Screen.tongKetLose:
                drawTongKetLose(g, width, height);
                break;
        }
    }

    private void drawPlayGame(Graphics g, int width, int height) {
        
        g.setColor(new Color(70, 70, 70)); 
        g.fillRect(0, 0, width, height);
        
        Screen.room.draw(g); 
        
        ArrayList<Mob> renderMobs = new ArrayList<>();
        
        for (int i = 0; i < Screen.mobs.length; i++) { 
            if (Screen.mobs[i] != null && Screen.mobs[i].inGame) {
                renderMobs.add(Screen.mobs[i]);
            }
        }
        for (int i = 0; i < Screen.mobss.length; i++) { 
            if (Screen.mobss[i] != null && Screen.mobss[i].inGame) {
                renderMobs.add(Screen.mobss[i]);
            }
        }
        for (int i = 0; i < Screen.mobsss.length; i++) { 
            if (Screen.mobsss[i] != null && Screen.mobsss[i].inGame) {
                renderMobs.add(Screen.mobsss[i]);
            }
        }
        for (int i = 0; i < Screen.mini.length; i++){
            if (Screen.mini[i] != null && Screen.mini[i].inGame){
                renderMobs.add(Screen.mini[i]);
            }
        }

        // sắp xếp quái theo tọa độ y --> con nào có tọa độ y nhỏ hơn thì được vẽ trước
        // tránh trường hợp con ở trên đè lên con ở dưới
        renderMobs.sort(Comparator.comparingInt(m -> m.y));
        // thực hiện vẽ
        for (Mob m : renderMobs) {
            m.draw(g);
        }
        
        Screen.store.draw(g); 

        for (Projectiles a : Screen.pjt) {
            a.draw(g);
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
        g.drawImage(Screen.tiles.storeOptions, 280, 20, 400, height-50, null);
        

    }    

    private void drawGachaScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        
        g.drawImage(Screen.tiles.gachaButt, 270, 245, 75, 40, null);
        g.drawImage(Screen.tiles.gachaButt, 270, 340, 75, 40, null);
        g.drawImage(Screen.tiles.gachaButt, 265, 440, 75, 40, null);

        g.drawImage(Screen.tiles.TornadoC, 255, 225, 90, 20, null);
        g.drawImage(Screen.tiles.EnhanceC, 235, 310, 115, 27, null);
        g.drawImage(Screen.tiles.MercenaryC, 243, 410, 105, 30, null);

        


        g.drawImage(Screen.tiles.summonHeroButt, 438, 410, 96, 80, null);
        g.drawImage(Screen.tiles.in4Butt, 640, 20, 30, 30, null);
        g.drawImage(Screen.tiles.iconShardShop, 620, 60, 50, 50, null);

        g.drawImage(Screen.tiles.iconCoin, 5, 530, 25, 25, null);
        g.setFont(Screen.tiles.pixelFontv1);
        Color darkSilver = Color.decode("#BFC7D5");
        g.setColor(darkSilver);
        g.drawString(": " + Screen.coinage, 35, 550);
    }

    private void drawGachaRateScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);
        Color darkSilver = Color.decode("#BFC7D5");
        Color royalGold = Color.decode("#FFBF00");

        
        //g.drawImage(Screen.tiles.gachaButt, 270, 250, 75, 40, null);
        //g.drawImage(Screen.tiles.gachaButt, 270, 345, 75, 40, null);
        //g.drawImage(Screen.tiles.gachaButt, 265, 450, 75, 40, null);

        //g.drawImage(Screen.tiles.TornadoC, 255, 225, 90, 20, null);
        //g.drawImage(Screen.tiles.EnhanceC, 235, 313, 115, 27, null);
        //g.drawImage(Screen.tiles.MercenaryC, 243, 415, 105, 30, null);


        g.drawImage(Screen.tiles.summonHeroButt, 438, 410, 96, 80, null);
        g.drawImage(Screen.tiles.in4Butt, 640, 20, 30, 30, null);
        g.drawImage(Screen.tiles.iconShardShop, 620, 60, 50, 50, null);


        g.drawImage(Screen.tiles.iconCoin, 5, 530, 25, 25, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(darkSilver);
        g.drawString(": " + Screen.coinage, 35, 550);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);
        g.drawImage(Screen.tiles.gachaInfoFrame, 140, 5, 400, 555, null);
        g.drawImage(Screen.tiles.gachaRateText, 250, 85, 180, 30, null);


        


        g.setColor(royalGold);
        g.setFont(Screen.tiles.pixelFontv2);
        g.drawString("1. Skill Chest", 215, 140);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(darkSilver);
        g.drawString("Skill: 2%", 230, 155);
        g.drawString("x1 Mystery Shard: 35%", 230, 170);
        g.drawString("x2 Mystery Shard: 20%", 230, 185);
        g.drawString("x3 Mystery Shard: 15%", 230, 200);
        g.drawString("x5 Mystery Shard: 10%", 230, 215);
        g.drawString("x10 Mystery Shard: 10%", 230, 230);
        g.drawString("x20 Mystery Shard: 5%", 230, 245);
        g.drawString("x50 Mystery Shard: 3%", 230, 260);

        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(royalGold);
        g.drawString("2. Summon Hero",215, 290);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(darkSilver);
        g.drawString("Giant Orc: 1%", 230, 305);
        g.drawString("x3 Mystery Shard: 20%", 230, 320);
        g.drawString("x5 Mystery Shard: 20%", 230, 335);
        g.drawString("x10 Mystery Shard: 20%", 230, 350);
        g.drawString("x20 Mystery Shard: 20%", 230, 365);
        g.drawString("x35 Mystery Shard: 15%", 230, 380);
        g.drawString("x50 Mystery Shard: 3%", 230, 395);
        g.drawString("x100 Mystery Shard: 1%", 230, 410);

    }

    private void drawShardShop(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        
        //g.drawImage(Screen.tiles.gachaButt, 270, 250, 75, 40, null);
        //g.drawImage(Screen.tiles.gachaButt, 270, 345, 75, 40, null);
        //g.drawImage(Screen.tiles.gachaButt, 265, 450, 75, 40, null);

        //g.drawImage(Screen.tiles.TornadoC, 255, 225, 90, 20, null);
        //g.drawImage(Screen.tiles.EnhanceC, 235, 313, 115, 27, null);
        //g.drawImage(Screen.tiles.MercenaryC, 243, 415, 105, 30, null);


        // g.drawImage(Screen.tiles.summonHeroButt, 438, 410, 96, 80, null);
        g.drawImage(Screen.tiles.in4Butt, 640, 20, 30, 30, null);
        g.drawImage(Screen.tiles.iconShardShop, 620, 60, 50, 50, null);

        g.drawImage(Screen.tiles.iconCoin, 5, 530, 25, 25, null);
        g.setFont(Screen.tiles.pixelFontv1);
        Color darkSilver = Color.decode("#BFC7D5");
        g.setColor(darkSilver);
        g.drawString(": " + Screen.coinage, 35, 550);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);

        g.drawImage(Screen.tiles.shardShopBigFrame, 90, 15, 500, 500, null);
        g.drawImage(Screen.tiles.awakeningText, 250, 90, 180, 35, null);



        g.setColor(darkSilver);
        g.setFont(Screen.tiles.pixelFontvSmall);

        //1. Tornado
        g.drawImage(Screen.tiles.mysteryShard, 175, 135, 40, 40, null);
        g.drawImage(Screen.tiles.dauCong, 240, 145, 20, 20, null);
        g.drawImage(Screen.tiles.iconCoin, 275, 135, 40, 40, null);
        g.drawImage(Screen.tiles.muiTen, 340, 145, 35, 20, null);
        g.drawImage(Screen.tiles.iconTornado, 390, 135, 40, 40, null);
        drawButtTraoDoi(g, 450, 145, 70, 25, Screen.gachaTornado, Screen.unlockTornado);
        g.drawString("Mystery Shard", 160, 185);
        g.drawString("x100", 215, 175);
        g.drawString("x100", 315, 175);
        g.drawString("Coin", 285, 185);


        //2. Enhance
        g.drawImage(Screen.tiles.mysteryShard, 175, 200, 40, 40, null);
        g.drawImage(Screen.tiles.dauCong, 240, 210, 20, 20, null);
        g.drawImage(Screen.tiles.iconCoin, 275, 200, 40, 40, null);
        g.drawImage(Screen.tiles.muiTen, 340, 210, 35, 20, null);
        g.drawImage(Screen.tiles.iconEnhance, 390, 200, 40, 40, null);
        drawButtTraoDoi(g, 450, 210, 70, 25, Screen.gachaEnhance, Screen.unlockEnhance);
        g.drawString("Mystery Shard", 160, 250);
        g.drawString("x100", 215, 240);
        g.drawString("x100", 315, 240);
        g.drawString("Coin", 285, 250); 


        //3. Mercenary
        g.drawImage(Screen.tiles.mysteryShard, 175, 265, 40, 40, null);
        g.drawImage(Screen.tiles.dauCong, 240, 275, 20, 20, null);
        g.drawImage(Screen.tiles.iconCoin, 275, 265, 40, 40, null);
        g.drawImage(Screen.tiles.muiTen, 340, 275, 35, 20, null);
        g.drawImage(Screen.tiles.iconMercenary, 390, 265, 40, 40, null);
        drawButtTraoDoi(g, 450, 275, 70, 25, Screen.gachaMercenary, Screen.unlockMercenary);
        g.drawString("Mystery Shard", 160, 315);
        g.drawString("x100", 215, 305);
        g.drawString("x100", 315, 305);
        g.drawString("Coin", 285, 315);

        //4. Giant Orc
        g.drawImage(Screen.tiles.mysteryShard, 175, 330, 40, 40, null);
        g.drawImage(Screen.tiles.dauCong, 240, 340, 20, 20, null);
        g.drawImage(Screen.tiles.iconCoin, 275, 330, 40, 40, null);
        g.drawImage(Screen.tiles.muiTen, 340, 340, 35, 20, null);
        g.drawImage(Screen.tiles.iconGiantOrc, 390, 330, 40, 40, null);
        drawButtTraoDoi(g, 450, 340, 70, 25, Screen.summonHero, Screen.unlockGiantOrc);
        g.drawString("Mystery Shard", 160, 380);
        g.drawString("x250", 215, 370);
        g.drawString("x100", 315, 370);
        g.drawString("Coin", 285, 380);


        // Màu chữ vipppp
        Color tornadoGreen = Color.decode("#32CD32");
        Color enhanceRed = Color.decode("#FF4444");
        Color mercBlue = Color.decode("#3399FF");
        Color royalGold = Color.decode("#FFBF00");
        g.setColor(tornadoGreen);
        g.drawString("Tornado", 390, 185);
        g.setColor(enhanceRed);
        g.drawString("Enhance", 390, 250);
        g.setColor(mercBlue);
        g.drawString("Mercenary", 383, 315);
        g.setColor(royalGold);
        g.drawString("Giant Orc", 387, 380);

        g.drawString("Remaining: ", 450, 95);
        g.drawImage(Screen.tiles.mysteryShard, 450, 100, 20, 20, null);
        g.setColor(darkSilver);
        g.drawString("x" + Screen.shard, 475, 115);


    }

    private void drawThongBao(Graphics g, int width, int height) {

        Color darkSilver = Color.decode("#BFC7D5");
        Color tornadoGreen = Color.decode("#32CD32");
        Color enhanceRed = Color.decode("#FF4444");
        Color mercBlue = Color.decode("#3399FF");
        Color royalGold = Color.decode("#FFBF00");
        
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        
        g.drawImage(Screen.tiles.gachaButt, 270, 250, 75, 40, null);
        g.drawImage(Screen.tiles.gachaButt, 270, 345, 75, 40, null);
        g.drawImage(Screen.tiles.gachaButt, 265, 450, 75, 40, null);

        g.drawImage(Screen.tiles.TornadoC, 255, 225, 90, 20, null);
        g.drawImage(Screen.tiles.EnhanceC, 235, 313, 115, 27, null);
        g.drawImage(Screen.tiles.MercenaryC, 243, 415, 105, 30, null);


        g.drawImage(Screen.tiles.summonHeroButt, 438, 410, 96, 80, null);
        g.drawImage(Screen.tiles.in4Butt, 640, 20, 30, 30, null);
        g.drawImage(Screen.tiles.iconShardShop, 620, 60, 50, 50, null);

        g.drawImage(Screen.tiles.iconCoin, 5, 530, 25, 25, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(darkSilver);
        g.drawString(": " + Screen.coinage, 35, 550);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);
        g.drawImage(Screen.tiles.shardShopBigFrame, 168, 130, 350, 250, null);
        

        if(Screen.ok == false) {
            //Nhắc mấy con nghẹo gacha vừa thôi, giữ tiền ván sau còn mua tháp nữa :))))
            g.drawImage(Screen.tiles.oopss, 243, 170, 200, 50, null);
            g.setColor(darkSilver);
            g.setFont(Screen.tiles.pixelFontMedi);
            g.drawString("Save your coins!", 230, 250);
            g.drawString("You need at least 10 coins", 230, 270);
            g.drawString("to buy an archer tower for the", 230, 290);
            g.drawString("next level.", 230, 310);
            g.drawString("Spend wisely!", 230, 330);


        } 
        else {
            g.drawImage(Screen.tiles.congra, 243, 170, 200, 70, null);
            g.drawImage(Screen.tiles.mysteryShard, 308, 245, 70, 70, null);
            g.setFont(Screen.tiles.pixelFontv1);
            g.setColor(darkSilver);

            if(Screen.gachaType == Screen.summonHero) {
                if(Screen.randomNum == 1) {
                    g.drawImage(Screen.tiles.iconGiantOrc, 308, 245, 70, 70, null);
                    g.setColor(royalGold);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Giant Orc", 307, 333);
                }
                else if(Screen.randomNum == 2) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x100", 378, 310);
                }
                else if(Screen.randomNum <= 5) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x50", 378, 310);
                }
                else if(Screen.randomNum <= 20) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x35", 378, 310);
                }
                else if(Screen.randomNum <= 40) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x20", 378, 310);
                }
                else if(Screen.randomNum <= 60) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x10", 378, 310);
                }
                else if(Screen.randomNum <= 80) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x5", 378, 310);
                }
                else {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x3", 378, 310);
                }
            }
            else {
                if(Screen.randomNum <= 2) {
                    if(Screen.gachaType == Screen.gachaTornado) {
                        g.drawImage(Screen.tiles.iconTornado, 308, 245, 70, 70, null);
                        g.setColor(tornadoGreen);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Tornado", 312, 333);
                    }
                    else if(Screen.gachaType == Screen.gachaEnhance) {
                        g.drawImage(Screen.tiles.iconEnhance, 308, 245, 70, 70, null);
                        g.setColor(enhanceRed);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Enhancement", 295, 333);
                    }
                    else if(Screen.gachaType == Screen.gachaMercenary) {
                        g.drawImage(Screen.tiles.iconMercenary, 308, 245, 70, 70, null);
                        g.setColor(mercBlue);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Mercenary", 304, 333);
                    }

                }
                else if(Screen.randomNum <=5 ) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x50", 378, 310); 
                }
                else if(Screen.randomNum <=10) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x20", 378, 310);
                }
                else if(Screen.randomNum <= 20) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x10", 378, 310);
                }
                else if(Screen.randomNum <= 30) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x5", 378, 310);
                }
                else if(Screen.randomNum <= 45) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x3", 378, 310);
                }
                else if(Screen.randomNum <= 65) {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x2", 378, 310);
                }
                else {
                    g.setColor(darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 288, 333);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x1", 378, 310);
                }

            }
        }
        

    }
   
    private void drawButtTraoDoi(Graphics g, int x, int y, int dx, int dy, int checkingObject, boolean checkingValue) {
        if(checkingValue == true) {
            g.drawImage(Screen.tiles.awaButtV1, x, y, dx, dy, null);
        }
        else {
            if(checkingObject == Screen.summonHero) {
                if(Screen.coinage >= 100 && Screen.shard >=250) {
                    g.drawImage(Screen.tiles.awaButtV2, x, y, dx, dy, null);
                }
                else{
                    g.drawImage(Screen.tiles.awaButtV3, x, y, dx, dy, null);
                }
            }
            else {
                if(Screen.coinage >= 100 && Screen.shard >=100) {
                    g.drawImage(Screen.tiles.awaButtV2, x, y, dx, dy, null);
                }
                else{
                    g.drawImage(Screen.tiles.awaButtV3, x, y, dx, dy, null);
                }
            }
        }
    }   


    private void drawTongKetWin(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tongKetWin, 0, 0, width, height, null);

    }
    private void drawTongKetLose(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tongKetLose, 0, 0, width, height, null);
        
    }

}