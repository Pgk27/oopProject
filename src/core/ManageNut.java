
import java.awt.Color;
import java.awt.Graphics;



public class ManageNut {
    public static int[] countNut = new int[25];
    public static NutBamVip[][] heThongNut = new NutBamVip[15][15];
	public Screen screen;

    public ManageNut(Screen screen) {
		this.screen = screen;
        for(int i=0; i<25; i++) {
            this.countNut[i]=0;
        }
		// public static final int tileScreen=0;
		// public static final int playGame=1;
		// public static final int settings=2;
		// public static final int selectSkill=3;
		// public static final int gameShop=4;
		// public static final int buyItem=5;
		// public static final int gacha=6;
		// public static final int shardShop=7;
		// public static final int thongBao=8;
		// public static final int gachaRate=9;
		// public static final int tongKetWin=10;
		// public static final int tongKetLose=11;
		// public static final int updateHero=12;

		addNut(playgame, 0);
		addNut(store, 0);
		addNut(updateHero, 0);
		addNut(quitgame, 0);

		addNut(backInGameShop, 4);

		addNut(in4gacha, 6);
		addNut(shardShop, 6);
		addNut(backInGacha, 6);
		addNut(gachaBH, 6);
		addNut(gachaEnhance, 6);
		addNut(gachaMer, 6);
		addNut(summon, 6);

		

		addNut(shardShopBigFramee, 7);
		addNut(doiBH, 7);
		addNut(doiEnhance, 7);
		addNut(doiMer, 7);
		addNut(doiEC, 7);
		
		addNut(thongBaoFrame, 8);
		
		addNut(gachaRateFrame, 9);

		addNut(backInGameShop, 12);
		addNut(levelUp, 12);
		addNut(expBar, 12);
		addNut(add1, 12);
		addNut(add2, 12);
		addNut(add3, 12);
		addNut(add4, 12);
		addNut(add5, 12);
    }
   
    public static void addNut(NutBamVip x, int gState) {
        heThongNut[gState][countNut[gState]] = x;
        countNut[gState] ++;
    }

    

	







    
    public static NutBamVip playgame = new NutBamVip(950, 420, 144, 72) { 
        @Override 
        public void onClick() {
            Screen.gameState = Screen.playGame;
            Screen.startGame();
        }
    }; 


    public static NutBamVip store = new NutBamVip(950, 495, 144, 72) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.gameShop;
        }
    };

	public static NutBamVip updateHero = new NutBamVip(950, 570, 144, 72) {
        @Override
        public void onClick() {
			Screen.gameState = Screen.updateHero;
			Screen.upHeroXp[1]=200;
			Screen.upHeroCoin[1]=50;
			Screen.upHeroShard[1]=50;
			Screen.upHeroECore[1] = 1;

            for(int i=2; i<25; i++) {
				Screen.upHeroXp[i]=(int)(1.2*Screen.upHeroXp[i-1]);
				Screen.upHeroCoin[i]=(int)(1.2*Screen.upHeroCoin[i-1]);
				Screen.upHeroShard[i] = 50*i;
				Screen.upHeroECore[i] = i;
			}
        }
    };



    public static NutBamVip quitgame = new NutBamVip(950, 645, 144, 72) {
        @Override
        public void onClick() {
            System.exit(0);
        }
    };

    
    // public NutBamVip setting = new NutBamVip(  ) {
    //     @Override
    //     public void onClick() {
    //         Screen.gameState = Screen.settings;   
    //     }
    // };

	public NutBamVip backInGameShop = new NutBamVip( 5, 5, 50, 35 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.tileScreen;
            
        }
    };


    
    // public NutBamVip gacha = new NutBamVip(  ) {
    //     @Override
    //     public void onClick() {
    //         Screen.gameState = Screen.gacha;
            
    //     }
    // };

    
    // public NutBamVip buyItem = new NutBamVip(  ) {
    //     @Override
    //     public void onClick() {
    //         Screen.gameState = Screen.buyItem;
            
    //     }
    // };


    
    public NutBamVip backInGacha = new NutBamVip( 5, 5, 50, 35 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.gameShop;
            
        }
    };
    
    
    public NutBamVip in4gacha = new NutBamVip( 1110, 20, 40, 40 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.gachaRate;
            
        }
    };
    
    public NutBamVip shardShop = new NutBamVip( 1100, 80, 65, 65 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.shardShop;
            
        }
    };
    
    
    public NutBamVip gachaBH = new NutBamVip( 500, 345,  85, 50 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.thongBao;
            Screen.gachaType = Screen.gachaBH;
            logicGachaBH();
            
        }
    };
    


    
    public NutBamVip gachaEnhance = new NutBamVip( 500, 475,  85, 50 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.thongBao;
            Screen.gachaType = Screen.gachaEnhance;
            logicGachaEnhance();
            
        }
    };


    
    public NutBamVip gachaMer = new NutBamVip( 500, 610,  90, 53 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.thongBao;
            Screen.gachaType = Screen.gachaMercenary;
            logicGachaMercenary();
            
        }
    };

    
    public NutBamVip summon = new NutBamVip( 800, 545, 125, 104 ) {
        @Override
        public void onClick() {
            Screen.gameState = Screen.thongBao;
			Screen.gachaType = Screen.summon;
			logicSummon();
            
        }
    };


    
    public NutBamVip gachaRateFrame = new NutBamVip( 300, 5, 600, 750 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
        if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
            return false;
        }
        return true;
        }
        public void onClick() {
            Screen.gameState = Screen.gacha;
        }
		public void render(Graphics g) {
			g.drawImage(Screen.manageNut.gachaRateFrame.ima[0], x, y, dx, dy, null);
		}
    };


    
    public static NutBamVip shardShopBigFramee = new NutBamVip( 60, 30, 900, 680 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
            if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
                return false;
            }
            return true;
        }
        public void onClick() {
            Screen.gameState = Screen.gacha;
            
        }
		
    };


    
    public  NutBamVip doiBH = new NutBamVip( 670, 210, 95, 30 ) {
        @Override
        public void onClick() {
            if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockBH == false) {
				Screen.coinage = Screen.coinage - 100;
				Screen.shard = Screen.shard - 100;
				Screen.unlockBH = true;
			}
            
        }
		public void renderNut(Graphics g) {
			if (Screen.unlockBH == true) {
				g.drawImage(Screen.tiles.awaken, x, y, dx, dy, null);
			}
			else if (Screen.coinage >=100 && Screen.shard >=100) {
				if (state == 0) {
            		g.drawImage(ima[0], x, y, dx, dy, null);
        		} 
        		else if (state == 1) {
            		g.drawImage(ima[1], x, y, dx, dy, null);
        		} 
        		else if (state == 2) {
        		    g.drawImage(ima[1], x, y + 3, dx, dy, null);
        		}
			}
			else {
				g.drawImage(Screen.tiles.awakeT,x, y, dx, dy, null);
			}
		}
    };

    public NutBamVip doiEnhance = new NutBamVip( 670, 290, 95, 30 ) {
        @Override
        public void onClick() {
            if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockEnhance == false) {
				Screen.coinage = Screen.coinage - 100;
				Screen.shard = Screen.shard - 100;
				Screen.unlockEnhance = true;
			}
            
        }
		public void renderNut(Graphics g) {
			if (Screen.unlockEnhance == true) {
				g.drawImage(Screen.tiles.awaken, x, y, dx, dy, null);
			}
			else if (Screen.coinage >=100 && Screen.shard >=100) {
				if (state == 0) {
            		g.drawImage(ima[0], x, y, dx, dy, null);
        		} 
        		else if (state == 1) {
            		g.drawImage(ima[1], x, y, dx, dy, null);
        		} 
        		else if (state == 2) {
        		    g.drawImage(ima[1], x, y + 3, dx, dy, null);
        		}
			}
			else {
				g.drawImage(Screen.tiles.awakeT,x, y, dx, dy, null);
			}
		}
    };
    public NutBamVip doiMer = new NutBamVip( 670, 370, 95, 30 ) {
        @Override
        public void onClick() {
            if(Screen.coinage >= 100 && Screen.shard >=100  && Screen.unlockMercenary == false) {
				Screen.coinage = Screen.coinage - 100;
				Screen.shard = Screen.shard - 100;
				Screen.unlockMercenary = true;
			}
            
        }
		public void renderNut(Graphics g) {
			if (Screen.unlockMercenary == true) {
				g.drawImage(Screen.tiles.awaken, x, y, dx, dy, null);
			}
			else if (Screen.coinage >=100 && Screen.shard >=100) {
				if (state == 0) {
            		g.drawImage(ima[0], x, y, dx, dy, null);
        		} 
        		else if (state == 1) {
            		g.drawImage(ima[1], x, y, dx, dy, null);
        		} 
        		else if (state == 2) {
        		    g.drawImage(ima[1], x, y + 3, dx, dy, null);
        		}
			}
			else {
				g.drawImage(Screen.tiles.awakeT,x, y, dx, dy, null);
			}
		}
    };

    public NutBamVip doiEC = new NutBamVip( 670, 450, 95, 30 ) {
        @Override
        public void onClick() {
            if(Screen.coinage >= 200 && Screen.shard >=200 ) {
				Screen.coinage = Screen.coinage - 200;
				Screen.shard = Screen.shard - 200;
				Screen.eCore ++;
			}
            
        }
		public void renderNut(Graphics g) {
			if (Screen.coinage >=200 && Screen.shard >=200) {
				if (state == 0) {
            		g.drawImage(ima[0], x, y, dx, dy, null);
        		} 
        		else if (state == 1) {
            		g.drawImage(ima[1], x, y, dx, dy, null);
        		} 
        		else if (state == 2) {
        		    g.drawImage(ima[1], x, y + 3, dx, dy, null);
        		}
			}
			else {
				g.drawImage(Screen.tiles.purchaseT, x, y, dx, dy, null);
			}
		}
    };

    public static NutBamVip thongBaoFrame = new NutBamVip( 360, 140, 500, 400 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
            if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
                return false;
            }
            return true;
        }
        public void onClick() {
            Screen.gameState = Screen.gacha;
			Screen.manageNut.screen.paintImmediately(0, 0, Screen.WIDTH, Screen.HEIGHT);
            
        }
    };

	public NutBamVip levelUp = new NutBamVip ( 980, 180, 110, 40 ) {
		@Override
		public void onClick() {
			logicLevelUp();
		}
		public void renderNut(Graphics g) {
			if(Screen.heroLevel >=25) {
				g.drawImage(Screen.tiles.maxLevel, x, y, dx, dy, null);
			}
			else if( Screen.heroXp < Screen.upHeroXp[Screen.heroLevel] || Screen.coinage < Screen.upHeroCoin[Screen.heroLevel] || 
				Screen.shard < Screen.upHeroShard[Screen.heroLevel] || Screen.eCore < Screen.upHeroECore[Screen.heroLevel]) {
					g.drawImage (Screen.tiles.levelUpT, x, y, dx, dy, null);
			}
			else {
				if (state == 0) {
            		g.drawImage(ima[0], x, y, dx, dy, null);
        		} 
        		else if (state == 1) {
        		    g.drawImage(ima[1], x, y-3, dx, dy, null);
        		} 
       			else if (state == 2) {
            		g.drawImage(ima[1], x, y + 3, dx, dy, null);
        		}
			}
		}
	};

	public NutBamVip expBar = new NutBamVip( 700, 170, 280, 60 ){
		@Override
		public void renderNut(Graphics g) {
			if(state == 0) {
				g.drawImage(ima[0], x, y, dx, dy, null);
			}
			else {
				g.setColor(new Color(0,0,0,180));
				g.fillRect(730, 185, 220, 36);
				g.drawImage(ima[1], x, y, dx, dy, null);
				g.setFont(Screen.tiles.pixelFontv1);
				g.setColor(Color.white);
				g.drawString("Exp: " + Screen.heroXp + "/" + Screen.upHeroXp[Screen.heroLevel], 745, 208 );
			}
		}


	};

	public NutBamVip add1 = new NutBamVip( 1090, 340, 25, 25 ) {
		@Override
		public void onClick() {
			if(Screen.buffHp <Screen.maxBuff && Screen.remainingPoint > 0) {
				Screen.buffHp ++;
				Screen.remainingPoint --;
				MinionStats.maxHealth =  1200 + 100 * Screen.buffHp;
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.buffHp >= Screen.maxBuff) {
				g.drawImage(Screen.tiles.max, x-7, y, dx+14, dy, null);
			}
			else if(Screen.remainingPoint > 0) {
				if(state == 0) {
					g.drawImage(Screen.tiles.addPoint, x, y, dx, dy, null);
				}
				else if( state ==1) {
					g.drawImage(Screen.tiles.addPointS, x, y-2, dx, dy, null);
				}
				else if(state == 2) {
					g.drawImage(Screen.tiles.addPointS, x, y+1, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.addPointT, x, y, dy, dx, null);
			}
		}
	};

	public NutBamVip add2 = new NutBamVip( 1090, 370, 25, 25 ) {
		@Override
		public void onClick() {
			if(Screen.buffHpRe <Screen.maxBuff && Screen.remainingPoint > 0) {
				Screen.buffHpRe ++;
				Screen.remainingPoint --;
				MinionStats.HEAL_COOL_DOWN = 1000*(100-2*Screen.buffHpRe)/100;
				MinionStats.healAmount = 150+5*Screen.buffHpRe;
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.buffHpRe >= Screen.maxBuff) {
				g.drawImage(Screen.tiles.max, x-7, y, dx+14, dy, null);
			}
			else if(Screen.remainingPoint > 0) {
				if(state == 0) {
					g.drawImage(Screen.tiles.addPoint, x, y, dx, dy, null);
				}
				else if( state ==1) {
					g.drawImage(Screen.tiles.addPointS, x, y-2, dx, dy, null);
				}
				else if(state == 2) {
					g.drawImage(Screen.tiles.addPointS, x, y+1, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.addPointT, x, y, dy, dx, null);
			}
		}
	};

	public NutBamVip add3 = new NutBamVip( 1090, 400, 25, 25 ) {
		@Override
		public void onClick() {
			if(Screen.buffShield <Screen.maxBuff && Screen.remainingPoint > 0) {
				Screen.buffShield ++;
				Screen.remainingPoint --;
				MinionStats.dmgReduction = Screen.buffShield;
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.buffShield >= Screen.maxBuff) {
				g.drawImage(Screen.tiles.max, x-7, y, dx+14, dy, null);
			}
			else if(Screen.remainingPoint > 0) {
				if(state == 0) {
					g.drawImage(Screen.tiles.addPoint, x, y, dx, dy, null);
				}
				else if( state ==1) {
					g.drawImage(Screen.tiles.addPointS, x, y-2, dx, dy, null);
				}
				else if(state == 2) {
					g.drawImage(Screen.tiles.addPointS, x, y+1, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.addPointT, x, y, dy, dx, null);
			}
		}
	};

	public NutBamVip add4 = new NutBamVip( 1090, 430, 25, 25 ) {
		@Override
		public void onClick() {
			if(Screen.buffADmg <Screen.maxBuff && Screen.remainingPoint > 0) {
				Screen.buffADmg ++;
				Screen.remainingPoint --;
				MinionStats.arrowDmg = 150 + 20*Screen.buffADmg;
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.buffADmg >= Screen.maxBuff) {
				g.drawImage(Screen.tiles.max, x-7, y, dx+14, dy, null);
			}
			else if(Screen.remainingPoint > 0) {
				if(state == 0) {
					g.drawImage(Screen.tiles.addPoint, x, y, dx, dy, null);
				}
				else if( state ==1) {
					g.drawImage(Screen.tiles.addPointS, x, y-2, dx, dy, null);
				}
				else if(state == 2) {
					g.drawImage(Screen.tiles.addPointS, x, y+1, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.addPointT, x, y, dy, dx, null);
			}
		}
	};

	public NutBamVip add5 = new NutBamVip( 1090, 460, 25, 25 ) {
		@Override
		public void onClick() {
			if(Screen.buffSDmg <Screen.maxBuff && Screen.remainingPoint > 0) {
				Screen.buffSDmg ++;
				Screen.remainingPoint --;
				MinionStats.swordDmg = 200 + 25*Screen.buffSDmg;
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.buffSDmg >= Screen.maxBuff) {
				g.drawImage(Screen.tiles.max, x-7, y, dx+14, dy, null);
			}
			else if(Screen.remainingPoint > 0) {
				if(state == 0) {
					g.drawImage(Screen.tiles.addPoint, x, y, dx, dy, null);
				}
				else if( state ==1) {
					g.drawImage(Screen.tiles.addPointS, x, y-2, dx, dy, null);
				}
				else if(state == 2) {
					g.drawImage(Screen.tiles.addPointS, x, y+1, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.addPointT, x, y, dy, dx, null);
			}
		}
	};




    //Hàm phụ xử lí logic, thằng nào đụng vào bố chặt tay

	private void logicGachaBH() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockBH = true;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=10) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=30) {
				Screen.shard = Screen.shard + 5;
			}
			else if(Screen.randomNum <=45) {
				Screen.shard = Screen.shard + 3;
			}
			else if(Screen.randomNum <=65) {
				Screen.shard = Screen.shard + 2;
			}
			else {
				Screen.shard = Screen.shard + 1;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicGachaEnhance() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockEnhance = true;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=10) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=30) {
				Screen.shard = Screen.shard + 5;
			}
			else if(Screen.randomNum <=45) {
				Screen.shard = Screen.shard + 3;
			}
			else if(Screen.randomNum <=65) {
				Screen.shard = Screen.shard + 2;
			}
			else {
				Screen.shard = Screen.shard + 1;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicGachaMercenary() {
		if(Screen.coinage >=11) {
			Screen.coinage = Screen.coinage - 1;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;
					
			if (Screen.randomNum <=2) {
				Screen.unlockMercenary = true;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=10) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=30) {
				Screen.shard = Screen.shard + 5;
			}
			else if(Screen.randomNum <=45) {
				Screen.shard = Screen.shard + 3;
			}
			else if(Screen.randomNum <=65) {
				Screen.shard = Screen.shard + 2;
			}
			else {
				Screen.shard = Screen.shard + 1;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicSummon() {
		if(Screen.coinage >=12) {
			Screen.coinage = Screen.coinage - 2;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;

			if (Screen.randomNum ==1) {
				Screen.eCore++;
			}
			else if(Screen.randomNum ==2) {
				Screen.shard = Screen.shard + 100;
			}
			else if(Screen.randomNum <=5) {
				Screen.shard = Screen.shard + 50;
			}
			else if(Screen.randomNum <=20) {
				Screen.shard = Screen.shard + 35;
			}
			else if(Screen.randomNum <=40) {
				Screen.shard = Screen.shard + 20;
			}
			else if(Screen.randomNum <=60) {
				Screen.shard = Screen.shard + 10;
			}
			else if(Screen.randomNum <=80) {
				Screen.shard = Screen.shard + 5;
			}
			else {
				Screen.shard = Screen.shard + 3;
			}
		}
		else{
			Screen.ok=false;
		}
	}

	private void logicLevelUp() {
		if(Screen.heroLevel>=25) {
			return;
		}
		int curLv = Screen.heroLevel;
		if( Screen.heroXp>=Screen.upHeroXp[curLv] && Screen.coinage>=Screen.upHeroCoin[curLv] && 
			Screen.shard>=Screen.upHeroShard[curLv] && Screen.eCore>=Screen.upHeroECore[curLv]) {
				Screen.heroXp -= Screen.upHeroXp[curLv];
				Screen.coinage -= Screen.upHeroCoin[curLv];
				Screen.shard -= Screen.upHeroXp[curLv];
				Screen.eCore -=Screen.upHeroECore[curLv];
				Screen.heroLevel++;
				Screen.remainingPoint +=5;
				Screen.maxPoint = 5*Screen.heroLevel;
		}
	}
    





    



    

}
