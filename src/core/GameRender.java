package core;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import entity.Projectiles;
import entity.mob.Mob;
import spell.BomberPlane;
import spell.Spell;

public class GameRender {
    
    public void render(Graphics g, int width, int height) {
        int state = Screen.gameState;

        if (state == Value.TITLE_SCREEN) {
            drawTitleScreen(g, width, height);
        } else if (state == Value.IN_GAME) {
            drawInGame(g, width, height);
        } else if (state == Value.SHOP) {
            drawStore(g, width, height);
        } else if (state == Value.SETTING) {
            drawSetting(g, width, height);
        } else if (state == Value.GACHA) {
            drawGachaScreen(g, width, height);
        } else if (state == Value.GACHA_RATE) {
            drawGachaRateScreen(g, width, height);
        } else if (state == Value.SHARD_SHOP) {
            drawShardShop(g, width, height);
        } else if (state == Value.NOTI) {
            drawThongBao(g, width, height);
        } else if (state == Value.VICTORY_SCREEN) {
            drawTongKetWin(g, width, height);
        } else if (state == Value.DEFEAT_SCREEN) {
            drawTongKetLose(g, width, height);
        }

        // vẽ con trỏ chuột custom vào game
        boolean isHover = KeyHandel.isHoveringButton(Screen.mse.x, Screen.mse.y);
        BufferedImage currentCursor;

        if (KeyHandel.isMousePressed) {
            // 1. Đang click vào button
            currentCursor = Screen.tiles.cursorPressed;
        } else if (isHover) {
            // 2. Đang hover trên button
            currentCursor = Screen.tiles.cursorHover;
        } else {
            // 3. Chuột bình thường
            currentCursor = Screen.tiles.cursorNormal;
        }

        if (currentCursor != null){
            int cursorSize = 32;
            g.drawImage(currentCursor, Screen.mse.x, Screen.mse.y, cursorSize, cursorSize, null);
        }
    }

    private void drawInGame(Graphics g, int width, int height) {
        
        g.setColor(new Color(70, 70, 70)); 
        g.fillRect(0, 0, width, height);
        
        Screen.room.draw(g); 

        g.drawImage(Screen.tiles.backButton, 5, 700, 50, 35, null);
        
        // Vẽ Spell mặt đất (như BlackHole) trước quái
        for (Spell s : Screen.activeSpells) {
            if (!(s instanceof BomberPlane)) {
                s.draw(g);
            }
        }
        
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

        for (int i = 0; i < Screen.merc.length; i++){
            if (Screen.merc[i] != null && Screen.merc[i].inGame){
                renderMobs.add(Screen.merc[i]);
            }
        }

        // sắp xếp quái theo tọa độ y --> con nào có tọa độ y nhỏ hơn thì được vẽ trước
        // tránh trường hợp con ở trên đè lên con ở dưới
        renderMobs.sort(Comparator.comparingInt(m -> m.y));
        // thực hiện vẽ
        for (Mob m : renderMobs) {
            m.draw(g);
        }

        // Vẽ Spell trên không / bom nổ (như BomberPlane) phía trên quái
        for (Spell s : Screen.activeSpells) {
            if (s instanceof BomberPlane) {
                s.draw(g);
            }
        }
        
        Screen.store.draw(g); 

        for (Projectiles a : Screen.pjt) {
            a.draw(g);
        }
        
    }

    private void drawTitleScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.titleScreen, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.settingsButton, 5, 5, 35 , 35, null);
        g.drawImage(Screen.tiles.newGameButton, 950, 450, 144, 72, null);
        g.drawImage(Screen.tiles.storeButton, 950, 520, 144, 72, null);
        g.drawImage(Screen.tiles.quitGameButton, 950, 590, 144, 72, null);
    }

    private void drawStore(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.storeTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);
        g.drawImage(Screen.tiles.storeOptions, 550, 20, 650, height-50, null);
        g.setColor(new Color(0, 0, 0, 200));
    }    

    private void drawSetting(Graphics g, int width, int height){
        int offsetFromCenterX = 300;
        int offsetFromCenterY = 375;
        int buttonSize = 100;
        int buttonWidth = 72;
        int buttonHeight = 80;
        int longButtonHeight = 70;
        int longButtonWidth = 240;
        int awayFromLeftEdge = 75;
        int awayFromTopEdge = 250;
        int count = 0;
        int space = 100;

        int menuX = width / 2 - offsetFromCenterX; // 310
        int menuY = height / 2 - offsetFromCenterY; // 15
        int menuWidth = 2 * offsetFromCenterX;
        int menuHeight = 2 * offsetFromCenterY; 

        // vẽ background menu setting
        g.drawImage(Screen.tiles.settingMenu, menuX, menuY, menuWidth, menuHeight, null);
        
        // vẽ các nút
        g.setFont(Screen.tiles.pixelFontMedi);
        g.drawString("MUSIC", menuX + awayFromLeftEdge, menuY + awayFromTopEdge + 35);
        if (!KeyHandel.isMusicMuted)
            g.drawImage(Screen.tiles.buttonSliderOn, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        else
            g.drawImage(Screen.tiles.buttonSliderOff, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        drawSlider(g, menuX + awayFromLeftEdge + 125, menuY + awayFromTopEdge + space * (count++), Screen.musicVolume, KeyHandel.musicSliderBox);
        
        g.drawString("SFX", menuX + awayFromLeftEdge, menuY + awayFromTopEdge + space*count + 35);
        if (!KeyHandel.isSfxMuted)
            g.drawImage(Screen.tiles.buttonSliderOn, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        else
            g.drawImage(Screen.tiles.buttonSliderOff, menuX + awayFromLeftEdge + 350, menuY + awayFromTopEdge - 25 + space * count, buttonSize, buttonSize, null);
        drawSlider(g, menuX + awayFromLeftEdge + 125, menuY + awayFromTopEdge + space * (count++), Screen.sfxVolume, KeyHandel.sfxSliderBox);

        g.drawString("FULLSCREEN", menuX + awayFromLeftEdge, menuY + awayFromTopEdge + space * count + 35);
        if (Frame.isFullscreen){
            g.drawImage(Screen.tiles.tickPressedButton, menuX + awayFromLeftEdge + 200, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
            g.drawImage(Screen.tiles.xButton, menuX + awayFromLeftEdge + 300, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
        }
        else{
            g.drawImage(Screen.tiles.tickButton, menuX + awayFromLeftEdge + 200, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
            g.drawImage(Screen.tiles.xPressedButton, menuX + awayFromLeftEdge + 300, menuY + awayFromTopEdge - 15 + space * count, buttonWidth, buttonHeight, null);
        }

        
        if (!KeyHandel.isSettingCancelPressed){
            g.drawImage(Screen.tiles.longButton, menuX + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CANCEL", menuX + 115, menuY + 638 + longButtonHeight / 2);
        }
        else{
            g.drawImage(Screen.tiles.longPressedButton, menuX + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CANCEL", menuX + 115, menuY + 640 + longButtonHeight / 2);
        }
        
        if (!KeyHandel.isSettingConfirmPressed){
            g.drawImage(Screen.tiles.longButton, menuX + 40 + longButtonWidth + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CONFIRM", menuX + longButtonWidth + 140, menuY + 638 + longButtonHeight / 2);
        }
        else{
            g.drawImage(Screen.tiles.longPressedButton, menuX + 40 + longButtonWidth + 40, menuY + 630, longButtonWidth, longButtonHeight, null);
            g.drawString("CONFIRM", menuX + longButtonWidth + 140, menuY + 640 + longButtonHeight / 2);
        }
        
        
    }

    private void drawGachaScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);


        g.drawImage(Screen.tiles.gachaButt, 500, 345,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 475,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 610,  90, 53, null);
        g.drawImage(Screen.tiles.summonHeroButt, 800, 545, 125, 104, null);


        g.drawImage(Screen.tiles.in4Butt, 1110, 20, 40, 40, null);
        g.drawImage(Screen.tiles.iconShardShop, 1100, 80, 65, 65, null);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);
    }

    private void drawGachaRateScreen(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);


        g.drawImage(Screen.tiles.gachaButt, 500, 345,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 475,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 610,  90, 53, null);
        g.drawImage(Screen.tiles.summonHeroButt, 800, 545, 125, 104, null);


        g.drawImage(Screen.tiles.in4Butt, 1110, 20, 40, 40, null);
        g.drawImage(Screen.tiles.iconShardShop, 1100, 80, 65, 65, null);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);
        

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);
        g.drawImage(Screen.tiles.gachaInfoFrame, 300, 5, 600, 750, null);
        g.drawImage(Screen.tiles.gachaRateText, 475, 110, 250, 50, null);


        


        g.setColor(Screen.tiles.royalGold);
        g.setFont(Screen.tiles.pixelFontv2);
        g.drawString("1. Skill Chest", 415, 185);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString("Skill: 2%", 430, 207);
        g.drawString("x1 Mystery Shard: 35%", 430, 225);
        g.drawString("x2 Mystery Shard: 20%", 430, 243);
        g.drawString("x3 Mystery Shard: 15%", 430, 261);
        g.drawString("x5 Mystery Shard: 10%", 430, 279);
        g.drawString("x10 Mystery Shard: 10%", 430, 297);
        g.drawString("x20 Mystery Shard: 5%", 430, 315);
        g.drawString("x50 Mystery Shard: 3%", 430, 333);

        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.royalGold);
        g.drawString("2. Summon Hero", 415, 360);

        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString("Energy Core: 1%", 430, 382);
        g.drawString("x3 Mystery Shard: 20%", 430, 400);
        g.drawString("x5 Mystery Shard: 20%", 430, 418);
        g.drawString("x10 Mystery Shard: 20%", 430, 436);
        g.drawString("x20 Mystery Shard: 20%", 430, 454);
        g.drawString("x35 Mystery Shard: 15%", 430, 472);
        g.drawString("x50 Mystery Shard: 3%", 430, 490);
        g.drawString("x100 Mystery Shard: 1%", 430, 508);

    }

    private void drawShardShop(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);


        g.drawImage(Screen.tiles.gachaButt, 500, 345,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 475,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 610,  90, 53, null);
        g.drawImage(Screen.tiles.summonHeroButt, 800, 545, 125, 104, null);


        g.drawImage(Screen.tiles.in4Butt, 1110, 20, 40, 40, null);
        g.drawImage(Screen.tiles.iconShardShop, 1100, 80, 65, 65, null);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);

        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);




        g.drawImage(Screen.tiles.shardShopBigFrame, 160, 30, 900, 680, null);
        g.drawImage(Screen.tiles.awakeningText, 435, 130, 350, 75, null);


        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontvSmall);

        for (int i = 0; i < 4; i++) {
            int oy = i * 80; // tluc thông minh vl :>
            g.drawImage(Screen.tiles.mysteryShard, 310, 200 + oy, 50, 50, null);
            g.drawImage(Screen.tiles.dauCong, 395, 210 + oy, 30, 30, null);
            g.drawImage(Screen.tiles.iconCoin, 440, 200 + oy, 50, 50, null);
            g.drawImage(Screen.tiles.muiTen, 525, 210 + oy, 50, 30, null);
    
            g.drawString("Mystery Shard", 295, 260 + oy);
            if( i == 3) {
                g.drawString("x200", 360, 248 + oy);
                g.drawString("x200", 485, 248 + oy);
            }
            else {
                g.drawString("x100", 360, 248 + oy);
                g.drawString("x100", 485, 248 + oy);
            }
            
            g.drawString("Coin", 452, 260 + oy);
        }

        g.drawImage(Screen.tiles.iconBH, 590, 200, 50, 50, null);
        g.drawImage(Screen.tiles.iconEnhance, 590, 280, 50, 50, null);
        g.drawImage(Screen.tiles.iconMercenary, 590, 360, 50, 50, null);
        g.drawImage(Screen.tiles.iconCore, 590, 440, 50, 50, null);

       
        g.setColor(Screen.tiles.gloomyPurple);
        g.drawString("Black Hole", 585, 260);
        g.setColor(Screen.tiles.enhanceRed);
        g.drawString("Enhancement", 580, 340);
        g.setColor(Screen.tiles.mercBlue);
        g.drawString("Mercenary", 585, 420);
        g.setColor(Screen.tiles.gloomyPurple);
        g.drawString("Energy Core", 580, 500);

        drawButtTraoDoi(g, 670, 210, 95, 30, Screen.unlockBH);
        drawButtTraoDoi(g, 670, 290, 95, 30, Screen.unlockEnhance);
        drawButtTraoDoi(g, 670, 370, 95, 30, Screen.unlockMercenary);
        drawButtTraoDoi(g, 670, 450, 95, 30);


        g.setFont(Screen.tiles.pixelFontv2);
        g.setColor(Screen.tiles.royalGold);

        // g.drawString("Remaining: ", 320, 600);
        g.drawImage(Screen.tiles.mysteryShard, 300, 580, 35, 35, null);
        g.setColor(Screen.tiles.darkSilver);
        g.setFont(Screen.tiles.pixelFontMini);
        g.drawString("x" + Screen.shard + " remaining", 335, 613);


    }

    private void drawThongBao(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.gachaTile, 0, 0, width, height, null);
        g.drawImage(Screen.tiles.backButton, 5, 5, 50, 35, null);


        g.drawImage(Screen.tiles.BlackHoleC, 470, 310, 135, 30, null);
        g.drawImage(Screen.tiles.EnhanceC, 470, 430, 150, 35, null);
        g.drawImage(Screen.tiles.MercenaryC, 470, 565, 140, 38, null);


        g.drawImage(Screen.tiles.gachaButt, 500, 345,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 475,  85, 50, null);
        g.drawImage(Screen.tiles.gachaButt, 500, 610,  90, 53, null);
        g.drawImage(Screen.tiles.summonHeroButt, 800, 545, 125, 104, null);


        g.drawImage(Screen.tiles.in4Butt, 1110, 20, 40, 40, null);
        g.drawImage(Screen.tiles.iconShardShop, 1100, 80, 65, 65, null);

        g.drawImage(Screen.tiles.iconCoin, 10, 735, 30, 30, null);
        g.setFont(Screen.tiles.pixelFontv1);
        g.setColor(Screen.tiles.darkSilver);
        g.drawString(": " + Screen.coinage, 45, 760);



        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, 0, width, height);

        g.drawImage(Screen.tiles.shardShopBigFrame, 360, 140, 500, 400, null);
        

        if(Screen.ok == false) {
            //Nhắc mấy con nghẹo gacha vừa thôi, giữ tiền ván sau còn mua tháp nữa :))))
            g.drawImage(Screen.tiles.oopss, 460, 220, 300, 70, null);
            g.setColor(Screen.tiles.darkSilver);
            g.setFont(Screen.tiles.pixelFontMedi);
            g.drawString("Save your coins!", 440, 320);
            g.drawString("You need at least 10 coins", 440, 350);
            g.drawString("to buy an archer tower for the", 440, 380);
            g.drawString("next level.", 440, 410);
            g.drawString("Spend wisely!", 440, 450);


        } 
        else {
            g.drawImage(Screen.tiles.congra, 440, 200, 340, 120, null);
            g.drawImage(Screen.tiles.mysteryShard, 560, 330, 100, 100, null);
            g.setFont(Screen.tiles.pixelFontv1);
            g.setColor(Screen.tiles.darkSilver);

            if(Screen.gachaType == Screen.summon) {
                if(Screen.randomNum == 1) {
                    g.drawImage(Screen.tiles.iconCore, 559, 329, 102, 102, null);
                    g.setColor(Screen.tiles.gloomyPurple);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Energy Core", 542, 455);
                }
                else if(Screen.randomNum == 2) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x100", 660, 428);
                }
                else if(Screen.randomNum <= 5) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x50", 660, 428);
                }
                else if(Screen.randomNum <= 20) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x35", 660, 428);
                }
                else if(Screen.randomNum <= 40) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x20", 660, 428);
                }
                else if(Screen.randomNum <= 60) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x10", 660, 428);
                }
                else if(Screen.randomNum <= 80) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x5", 660, 428);
                }
                else {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x3", 660, 428);
                }
            }
            else {
                if(Screen.randomNum <= 2) {
                    if(Screen.gachaType == Screen.gachaBH) {
                        g.drawImage(Screen.tiles.iconBH, 559, 329, 102, 102, null);
                        g.setColor(Screen.tiles.gloomyPurple);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Black Hole", 550, 455);
                    }
                    else if(Screen.gachaType == Screen.gachaEnhance) {
                        g.drawImage(Screen.tiles.iconEnhance, 559, 329, 102, 102, null);
                        g.setColor(Screen.tiles.enhanceRed);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Enhancement", 540, 455);
                    }
                    else if(Screen.gachaType == Screen.gachaMercenary) {
                        g.drawImage(Screen.tiles.iconMercenary, 559, 329, 102, 102, null);
                        g.setColor(Screen.tiles.mercBlue);
                        g.setFont(Screen.tiles.pixelFontMedi);
                        g.drawString("Mercenary", 555, 455);
                    }

                }
                else if(Screen.randomNum <=5 ) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x50", 660, 428);
                }
                else if(Screen.randomNum <=10) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x20", 660, 428);
                }
                else if(Screen.randomNum <= 20) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x10", 660, 428);
                }
                else if(Screen.randomNum <= 30) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x5", 660, 428);
                }
                else if(Screen.randomNum <= 45) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x3", 660, 428);
                }
                else if(Screen.randomNum <= 65) {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x2", 660, 428);
                }
                else {
                    g.setColor(Screen.tiles.darkSilver);
                    g.setFont(Screen.tiles.pixelFontMedi);
                    g.drawString("Mystery Shard", 540, 453);
                    g.setFont(Screen.tiles.pixelFontv1);
                    g.drawString("x1", 660, 428);
                }

            }
        }
        

    }
   
    private void drawButtTraoDoi(Graphics g, int x, int y, int dx, int dy, boolean checkingValue) {
        if(checkingValue == true) {
            g.drawImage(Screen.tiles.awaButtV1, x, y, dx, dy, null);
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

    private void drawButtTraoDoi(Graphics g, int x, int y, int dx, int dy) {
        if(Screen.coinage >= 200 && Screen.shard >=200) {
            g.drawImage(Screen.tiles.purchaseSang, x, y, dx, dy, null);
            }
        else{
            g.drawImage(Screen.tiles.purchaseToi, x, y, dx, dy, null);
        }
    } 

    private void drawTongKetWin(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tongKetWin, 0, 0, width, height, null);

    }
    
    private void drawTongKetLose(Graphics g, int width, int height) {
        g.drawImage(Screen.tiles.tongKetLose, 0, 0, width, height, null);

        
    }

    /** 
     * Vẽ thanh slider (dành cho sfx và music)
    */
    private void drawSlider(Graphics g, int startX, int startY, int currentVolume, Rectangle outHitBox){
        int barW = 16;
        int barH = 48;
        int spacing = 5; // giãn cách giữa các thanh 
        int totalBar = 10; // tổng số thanh muốn vẽ

        int totalWidth = totalBar * barW + (totalBar - 1) * spacing;
        outHitBox.setBounds(startX, startY, totalWidth, barH);

        for (int i = 0; i < totalBar; i++){
            int x = startX + i * (barW + spacing);
            
            if (i < currentVolume){
                g.drawImage(Screen.tiles.volumeBarOn, x, startY, barW, barH, null);
            }
            else
                g.drawImage(Screen.tiles.volumeBarOff, x, startY, barW, barH, null);
        }
    }
}