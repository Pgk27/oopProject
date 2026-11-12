package core;

import entity.mob.MinionStats;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;



public class ManageNut {
    public static int[] countNut = new int[35]; // Mở rộng từ 25 -> 35
    public static NutBamVip[][] heThongNut = new NutBamVip[35][20]; // Mở rộng từ [15][15] -> [35][20]
	public Screen screen;

    public ManageNut(Screen screen) {
		this.screen = screen;
        for(int i = 0; i < countNut.length; i++) {
            countNut[i] = 0;
        }

		// Title Screen
		addNut(playgame, Value.TITLE_SCREEN);
		addNut(store, Value.TITLE_SCREEN);
		addNut(updateHero, Value.TITLE_SCREEN);
		addNut(quitgame, Value.TITLE_SCREEN);
		addNut(setting, Value.TITLE_SCREEN);
		addNut(toGacha, Value.TITLE_SCREEN);

		// In-game
		addNut(backToMenu, Value.IN_GAME);
		addNut(GoldenPickaxeButton, Value.IN_GAME);
		addNut(fireSwordButton, Value.IN_GAME);
		addNut(cursedSkullButton, Value.IN_GAME);
		addNut(bomberButton, Value.IN_GAME);
		addNut(skillButton, Value.IN_GAME);

		//Select Skill
		addNut(selectBH, Value.SKILL_SELECT);
		addNut(selectEnh, Value.SKILL_SELECT);
		addNut(selectMer, Value.SKILL_SELECT);
		addNut(selectSkillFrame, Value.SKILL_SELECT);


		// Buy Item
		addNut(backInGacha, Value.ITEM_SHOP);
		addNut(buyGoldenPickaxe, Value.ITEM_SHOP);
		addNut(buyFireSword, Value.ITEM_SHOP);
		addNut(buyCursedSkull, Value.ITEM_SHOP);
		addNut(buyPaperPlane, Value.ITEM_SHOP);
		addNut(buyHeart, Value.ITEM_SHOP);


		// Gacha
		addNut(in4gacha, Value.GACHA);
		addNut(shardShop, Value.GACHA);
		addNut(backInGacha, Value.GACHA);
		addNut(gachaBH, Value.GACHA);
		addNut(gachaEnhance, Value.GACHA);
		addNut(gachaMer, Value.GACHA);
		addNut(summon, Value.GACHA);
		addNut(iconShardShop, Value.GACHA);
		addNut(info, Value.GACHA);

		// Shard Shop
		addNut(shardShopBigFramee, Value.SHARD_SHOP);
		addNut(doiBH, Value.SHARD_SHOP);
		addNut(doiEnhance, Value.SHARD_SHOP);
		addNut(doiMer, Value.SHARD_SHOP);
		addNut(doiEC, Value.SHARD_SHOP);
		
		// Thông báo
		addNut(thongBaoFrame, Value.NOTI);
		
		// Gacha Rate
		addNut(gachaRateFrame, Value.GACHA_RATE);

		// Update Hero
		addNut(backInGameShop, Value.UPDATE_HERO);
		addNut(levelUp, Value.UPDATE_HERO);
		addNut(expBar, Value.UPDATE_HERO);
		addNut(add1, Value.UPDATE_HERO);
		addNut(add2, Value.UPDATE_HERO);
		addNut(add3, Value.UPDATE_HERO);
		addNut(add4, Value.UPDATE_HERO);
		addNut(add5, Value.UPDATE_HERO);

		// Tong Ket Thua
		addNut(menuLose, Value.DEFEAT_SCREEN);
		addNut(again, Value.DEFEAT_SCREEN);
		// addNut(expBarTK, Value.DEFEAT_SCREEN);

		// Tong Ket Win
		addNut(menuWin, Value.VICTORY_SCREEN);
		addNut(next, Value.VICTORY_SCREEN);
    }
   
    public static void addNut(NutBamVip x, int gState) {
        heThongNut[gState][countNut[gState]] = x;
        countNut[gState] ++;
    }

	

    private abstract static class InGameItemButton extends NutBamVip {
        private final int column;

        InGameItemButton(int column) {
            super(0, 0, 0, 0);
            this.column = column;
        }

        @Override
        public final boolean isEnabled() {
            return Screen.store != null && canUse();
        }

        protected abstract boolean canUse();

        private boolean updateBounds() {
            Store currentStore = Screen.store;
            if (currentStore == null) return false;
            Rectangle bounds = currentStore.getItemButtonBounds(column);
            x = bounds.x;
            y = bounds.y;
            dx = bounds.width;
            dy = bounds.height;
            return true;
        }

        @Override
        public boolean contains(int mouseX, int mouseY) {
            return updateBounds() && mouseX >= x && mouseX < x + dx
                    && mouseY >= y && mouseY < y + dy;
        }

        @Override
        public final void renderNut(Graphics g) {
            if (!isEnabled()) resetInteraction();
            if (updateBounds()) renderItem(g);
        }

        protected abstract void renderItem(Graphics g);
    }

    private static void drawBuffItem(Graphics g, NutBamVip button, Image icon,
            int quantity, int remaining) {
        int drawY = button.y + (button.state == 1 ? -3 : button.state == 2 ? 3 : 0);
        g.drawImage(button.state == 0 ? Screen.tiles.button : Screen.tiles.touchedButton,
                button.x, drawY, button.dx, button.dy, null);
        g.drawImage(icon, button.x + Store.itemIn, drawY + Store.itemIn,
                button.dx - Store.itemIn * 2, button.dy - Store.itemIn * 2, null);

        if (remaining > 0 || quantity <= 0) {
            g.setColor(remaining > 0 ? new Color(255, 180, 0, 110) : new Color(0, 0, 0, 160));
            g.fillRect(button.x, drawY, button.dx, button.dy);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.BOLD, 12));
        g.drawString("x" + quantity, button.x + Store.itemIn, drawY + Store.itemIn + 10);
        if (remaining > 0) {
            g.setFont(new Font("Courier New", Font.BOLD, 14));
            String timer = remaining + "s";
            g.drawString(timer, button.x + (button.dx - g.getFontMetrics().stringWidth(timer)) / 2,
                    drawY + button.dy / 2 + 8);
        }
    }

    public static NutBamVip skillButton = new InGameItemButton(4) {
        @Override
        protected boolean canUse() {
            return Screen.store.canSelectChosenSkill();
        }

        @Override
        public void onClick() {
            if (Screen.store != null) Screen.store.selectChosenSkill();
        }

        @Override
        protected void renderItem(Graphics g) {
            Image icon;
            int skillID = Value.PLACEHOLDER;
            switch (Screen.chosenCard) {
                case 1:
                    icon = Screen.tiles.iconBH;
                    skillID = Value.SPELL_BLACK_HOLE;
                    break;
                case 2:
                    icon = Screen.tiles.iconEnhance;
                    break;
                case 3:
                    icon = Screen.tiles.iconMercenary;
                    skillID = Value.SPELL_MERCENARY;
                    break;
                default:
                    return;
            }

            int drawY;

			if (state == 1) {
				drawY = y - 3;
			} else if (state == 2) {
				drawY = y + 3;
			} else {
				drawY = y;
			}
            g.drawImage(state == 0 ? Screen.tiles.button : Screen.tiles.touchedButton,
                    x, drawY, dx, dy, null); //nền
            g.drawImage(icon, x + Store.itemIn, drawY + Store.itemIn, //icon
                    dx - Store.itemIn * 2, dy - Store.itemIn * 2, null);

            if (Screen.store == null) return;
            int buffSeconds = Screen.store.getEnhancementRemainingSeconds();
            int cooldownSeconds = Screen.store.getSkillCooldownSeconds();
            if (buffSeconds > 0 || cooldownSeconds > 0) {
                boolean buffActive = buffSeconds > 0;
                g.setColor(buffActive ? new Color(255, 180, 0, 100) : new Color(0, 0, 0, 160));
                g.fillRect(x, drawY, dx, dy);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier New", Font.BOLD, 16));
                String timer = (buffActive ? buffSeconds : cooldownSeconds) + "s";
                g.drawString(timer, x + (dx - g.getFontMetrics().stringWidth(timer)) / 2,
                        drawY + dy / 2 + 6);
            } else if (Screen.store.holdsItem && Store.heldID == skillID) {
                g.setColor(new Color(255, 215, 0, 70));
                g.fillRect(x, drawY, dx, dy);
                g.setColor(Color.YELLOW);
                g.drawRect(x, drawY, dx, dy);
                g.drawRect(x + 1, drawY + 1, dx - 2, dy - 2);
            }
        }
    };

	public static NutBamVip GoldenPickaxeButton = new InGameItemButton(0) {
		@Override
		protected boolean canUse() {
			return Screen.store.canUseGoldenPickaxe();
		}

		@Override
		public void onClick() {
			if (Screen.store != null) Screen.store.useGoldenPickaxe();
		}

		@Override
		protected void renderItem(Graphics g) {
			drawBuffItem(g, this, Screen.tiles.iconGoldenPickaxe, Screen.goldenPickaxe,
					Screen.store.getGoldenPickaxeRemainingSeconds());
		}
	};

    public static NutBamVip fireSwordButton = new InGameItemButton(1) {
        @Override
        protected boolean canUse() {
            return Screen.store.canUseFireSword();
        }

        @Override
        public void onClick() {
            if (Screen.store != null) Screen.store.useFireSword();
        }

        @Override
        protected void renderItem(Graphics g) {
            drawBuffItem(g, this, Screen.tiles.iconFireSword, Screen.fireSword,
                    Screen.store.getFireSwordRemainingSeconds());
        }
    };

    public static NutBamVip cursedSkullButton = new InGameItemButton(2) {
        @Override
        protected boolean canUse() {
            return Screen.store.canUseCursedSkull();
        }

        @Override
        public void onClick() {
            if (Screen.store != null) Screen.store.useCursedSkull();
        }

        @Override
        protected void renderItem(Graphics g) {
            drawBuffItem(g, this, Screen.tiles.iconCursedSkull, Screen.cursedSkull,
                    Screen.store.getCursedSkullRemainingSeconds());
        }
    };

    public static NutBamVip bomberButton = new InGameItemButton(3) {
        @Override
        protected boolean canUse() {
            return Screen.store.canSelectBomberPlane();
        }

        @Override
        public void onClick() {
            if (Screen.gameState == Value.IN_GAME && Screen.store != null) {
                Screen.store.selectBomberPlane();
            }
        }

        @Override
        protected void renderItem(Graphics g) {
            int drawY = y + (state == 1 ? -3 : state == 2 ? 3 : 0);
            g.drawImage(state == 0 ? Screen.tiles.button : Screen.tiles.touchedButton,
                    x, drawY, dx, dy, null);
            g.drawImage(Screen.airplane, x + Store.itemIn, drawY + Store.itemIn,
                    dx - Store.itemIn * 2, dy - Store.itemIn * 2, null);
            if (Screen.paperPlane <= 0) {
                g.setColor(new Color(0, 0, 0, 160));
                g.fillRect(x, drawY, dx, dy);
            }

            if (Screen.store != null && Screen.store.holdsItem && Store.heldID == Value.SPELL_BOMBER_PLANE) {
                g.setColor(new Color(255, 215, 0, 70));
                g.fillRect(x, drawY, dx, dy);
                g.setColor(Color.YELLOW);
                g.drawRect(x, drawY, dx, dy);
                g.drawRect(x + 1, drawY + 1, dx - 2, dy - 2);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 12));
            g.drawString("x" + Screen.paperPlane, x + Store.itemIn, drawY + Store.itemIn + 10);
        }
    };
	

    public static NutBamVip playgame = new NutBamVip(880, 495, 144, 72) { 
        @Override 
        public void onClick() {
			if(Screen.unlockBH == false && Screen.unlockEnhance == false && Screen.unlockMercenary) {
				Screen.gameState = Value.IN_GAME;
				Screen.startGame();
			}
			else {
				Screen.gameState = Value.SKILL_SELECT;
			}
        }
    }; 

    public static NutBamVip store = new NutBamVip(180, 590, 144, 72) {
        @Override
        public void onClick() {
            Screen.gameState = Value.ITEM_SHOP;
        }
    };


	public static NutBamVip backToMenu = new NutBamVip(120, 672, 55, 37) {
		@Override
		public void onClick() {
			Screen.gameState = Value.TITLE_SCREEN;
			xuLyOutGame();
		}

	};

	public static NutBamVip updateHero = new NutBamVip(180, 440, 144, 72) {
        @Override
        public void onClick() {
			Screen.gameState = Value.UPDATE_HERO;
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

    public static NutBamVip quitgame = new NutBamVip(880, 570, 144, 72) {
        @Override
        public void onClick() {
            System.exit(0);
        }
    };

	public static NutBamVip toGacha = new NutBamVip(180, 515, 144, 72 ) {
		@Override
		public void onClick() {
			Screen.gameState = Value.GACHA;
			
		}
	};



	public static NutBamVip setting = new NutBamVip( 5, 5, 40, 45 ) {
		@Override
		public void onClick() {
			Screen.gameState = Value.SETTING;
			
		}
	};

	public static NutBamVip selectSkillFrame = new NutBamVip( 50, 50, 1120, 680 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
        if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
            return false;
        }
        return true;
        }
        public void onClick() {
            Screen.gameState = Value.TITLE_SCREEN;
        }
		public void renderNut(Graphics g){
			g.drawImage(Screen.tiles.selectSkill, x, y, dx, dy, null);
			g.drawImage(Screen.tiles.cardBH, 140, 200, 300, 415, null);
			g.drawImage(Screen.tiles.cardEnh, 460, 200, 300, 415, null);
			g.drawImage(Screen.tiles.cardMer, 780, 200, 300, 415, null);
		}
    };

	public static NutBamVip selectBH = new NutBamVip( 210, 542, 160, 35 ) {
		@Override
		public void onClick() {
			if(Screen.unlockBH == true) {
				Screen.chosenCard = 1;
				Screen.gameState = Value.IN_GAME;
				Screen.strChosenCard = "Black Hole";
				Screen.startGame();
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.unlockBH == true) {
				if (state == 0) {
					g.drawImage(Screen.tiles.selectCard, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.selectCardS, x, y-3, dx, dy, null);
				} 
	   			else if (state == 2) {
					g.drawImage(Screen.tiles.selectCardS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.lockedCard,x, y, dx, dy, null);
			}
		}
	};

	public static NutBamVip selectEnh = new NutBamVip( 530, 542, 160, 35 ) {
		@Override
		public void onClick() {
			if(Screen.unlockEnhance == true) {
				Screen.chosenCard = 2;
				Screen.strChosenCard = "Enhancement";
				Screen.gameState = Value.IN_GAME;
				Screen.startGame();
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.unlockEnhance == true) {
				if (state == 0) {
					g.drawImage(Screen.tiles.selectCard, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.selectCardS, x, y-3, dx, dy, null);
				} 
	   			else if (state == 2) {
					g.drawImage(Screen.tiles.selectCardS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.lockedCard,x, y, dx, dy, null);
			}
		}
	};

	public static NutBamVip selectMer = new NutBamVip( 850, 542, 160, 35 ) {
		@Override
		public void onClick() {
			if(Screen.unlockMercenary == true) {
				Screen.chosenCard = 3;
				Screen.strChosenCard = "Mercenary";
				Screen.gameState = Value.IN_GAME;
				Screen.startGame();
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.unlockMercenary == true) {
				if (state == 0) {
					g.drawImage(Screen.tiles.selectCard, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.selectCardS, x, y-3, dx, dy, null);
				} 
	   			else if (state == 2) {
					g.drawImage(Screen.tiles.selectCardS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.lockedCard,x, y, dx, dy, null);
			}
		}
	};

	

	public static NutBamVip backInGameShop = new NutBamVip( 5, 5, 50, 35 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.TITLE_SCREEN;
            
        }
    };
    
    public static NutBamVip backInGacha = new NutBamVip( 5, 5, 50, 35 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.TITLE_SCREEN;
            
        }
    };

	public static NutBamVip buyGoldenPickaxe = new NutBamVip( 350, 465, 80, 30 ) {
		@Override
		public void onClick() {
			if (Screen.coinage >=50) {
				Screen.coinage = Screen.coinage - 50;
				Screen.goldenPickaxe ++;
			}
		}
		public void renderNut(Graphics g) {
			if (Screen.coinage >=50) {
				if (state == 0) {
					g.drawImage(Screen.tiles.buyItem, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.buyItemS, x, y, dx, dy, null);
				} 
				else if (state == 2) {
				    g.drawImage(Screen.tiles.buyItemS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.buyItemT, x, y, dx, dy, null);
			}
		}
	};

	public static NutBamVip buyFireSword = new NutBamVip( 622, 465, 80, 30 ) {
		@Override
		public void onClick() {
			if (Screen.coinage >=50) {
				Screen.coinage = Screen.coinage - 50;
				Screen.fireSword ++;
			}
		}
		public void renderNut(Graphics g) {
			if (Screen.coinage >=50) {
				if (state == 0) {
					g.drawImage(Screen.tiles.buyItem, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.buyItemS, x, y, dx, dy, null);
				} 
				else if (state == 2) {
				    g.drawImage(Screen.tiles.buyItemS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.buyItemT, x, y, dx, dy, null);
			}
		}
	};

	public static NutBamVip buyCursedSkull = new NutBamVip( 350, 648, 80, 30 ) {
		@Override
		public void onClick() {
			if (Screen.coinage >=50) {
				Screen.coinage = Screen.coinage - 50;
				Screen.cursedSkull ++;
			}
		}
		public void renderNut(Graphics g) {
			if (Screen.coinage >=50) {
				if (state == 0) {
					g.drawImage(Screen.tiles.buyItem, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.buyItemS, x, y, dx, dy, null);
				} 
				else if (state == 2) {
				    g.drawImage(Screen.tiles.buyItemS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.buyItemT, x, y, dx, dy, null);
			}
		}
	};
	public static NutBamVip buyPaperPlane = new NutBamVip( 622, 648, 80, 30 ) {
		@Override
		public void onClick() {
			if (Screen.coinage >=50) {
				Screen.coinage = Screen.coinage - 50;
				Screen.paperPlane ++;
			}
		}
		public void renderNut(Graphics g) {
			if (Screen.coinage >=50) {
				if (state == 0) {
					g.drawImage(Screen.tiles.buyItem, x, y, dx, dy, null);
				} 
				else if (state == 1) {
					g.drawImage(Screen.tiles.buyItemS, x, y, dx, dy, null);
				} 
				else if (state == 2) {
				    g.drawImage(Screen.tiles.buyItemS, x, y + 3, dx, dy, null);
				}
			}
			else {
				g.drawImage(Screen.tiles.buyItemT, x, y, dx, dy, null);
			}
		}
	};

	public static NutBamVip buyHeart = new NutBamVip( 938, 646, 56, 35 ) {
		@Override
		public void onClick() {
			if (Screen.coinage >=35 && Screen.health < Screen.maxHealth) {
				Screen.coinage = Screen.coinage - 35;
				Screen.health ++;
			}
		}
		public void renderNut(Graphics g) {
			if(Screen.health == Screen.maxHealth) {
				g.drawImage(Screen.tiles.fullHeart, x, y, dx, dy, null);
			}
			else {
				if (Screen.coinage >=50) {
					if (state == 0) {
						g.drawImage(Screen.tiles.buyHeart, x, y, dx, dy, null);
					} 
					else if (state == 1) {
						g.drawImage(Screen.tiles.buyHeartS, x, y, dx, dy, null);
					} 
					else if (state == 2) {
						g.drawImage(Screen.tiles.buyHeartS, x, y + 3, dx, dy, null);
					}
				}
				else {
					g.drawImage(Screen.tiles.buyHeartT, x, y, dx, dy, null);
				}
			}
		}

	};
    
    public static NutBamVip in4gacha = new NutBamVip( 1110, 20, 40, 40 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.GACHA_RATE;
            
        }
    };
    
    public static NutBamVip shardShop = new NutBamVip( 1100, 80, 65, 65 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.SHARD_SHOP;
            
        }
    };
    
    
    public static NutBamVip gachaBH = new NutBamVip( 500, 345,  85, 50 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.NOTI;
            Screen.gachaType = Screen.gachaBH;
            logicGachaBH();
            
        }
    };
    
    public static NutBamVip gachaEnhance = new NutBamVip( 500, 475,  85, 50 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.NOTI;
            Screen.gachaType = Screen.gachaEnhance;
            logicGachaEnhance();
            
        }
    };

    public static NutBamVip gachaMer = new NutBamVip( 500, 610,  90, 53 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.NOTI;
            Screen.gachaType = Screen.gachaMercenary;
            logicGachaMercenary();
            
        }
    };

    public static NutBamVip summon = new NutBamVip( 800, 545, 125, 104 ) {
        @Override
        public void onClick() {
            Screen.gameState = Value.NOTI;
			Screen.gachaType = Screen.summon;
			logicSummon();
            
        }
    };

	public static NutBamVip iconShardShop = new NutBamVip( 1110, 75, 50, 60 ) {
		@Override
		public void onClick() {
			Screen.gameState = Value.SHARD_SHOP;
			
		}
	};

	public static NutBamVip info = new NutBamVip( 1110, 20, 50, 50 ) {
		@Override 
		public void onClick() {
			Screen.gameState = Value.GACHA_RATE;
		}


	};
  
    public static NutBamVip gachaRateFrame = new NutBamVip( 300, 5, 600, 750 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
        if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
            return false;
        }
        return true;
        }
        public void onClick() {
            Screen.gameState = Value.GACHA;
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
            Screen.gameState = Value.GACHA;
            
        }
		
    };


    
    public static NutBamVip doiBH = new NutBamVip( 670, 210, 95, 30 ) {
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

    public static NutBamVip doiEnhance = new NutBamVip( 670, 290, 95, 30 ) {
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
    public static NutBamVip doiMer = new NutBamVip( 670, 370, 95, 30 ) {
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

    public static NutBamVip doiEC = new NutBamVip( 670, 450, 95, 30 ) {
        @Override
        public void onClick() {
            if(Screen.coinage >= 200 && Screen.shard >=200 ) {
				Screen.coinage = Screen.coinage - 200;
				Screen.shard = Screen.shard - 200;
				Screen.energyCore ++;
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
            Screen.gameState = Value.GACHA;
			Screen.manageNut.screen.paintImmediately(0, 0, Screen.WIDTH, Screen.HEIGHT);
            
        }
    };

	public static NutBamVip levelUp = new NutBamVip ( 980, 180, 110, 40 ) {
		@Override
		public void onClick() {
			logicLevelUp();
		}
		public void renderNut(Graphics g) {
			if(Screen.heroLevel >=25) {
				g.drawImage(Screen.tiles.maxLevel, x, y, dx, dy, null);
			}
			else if( Screen.heroXp < Screen.upHeroXp[Screen.heroLevel] || Screen.coinage < Screen.upHeroCoin[Screen.heroLevel] || 
				Screen.shard < Screen.upHeroShard[Screen.heroLevel] || Screen.energyCore < Screen.upHeroECore[Screen.heroLevel]) {
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

	public static NutBamVip expBar = new NutBamVip( 700, 170, 280, 60 ){
		@Override
		public void renderNut(Graphics g) {
			if(state == 0) {
				g.drawImage(ima[0], x, y, dx, dy, null);
			}
			else {
				if(Screen.heroLevel < Screen.maxHeroLevel) {
					g.setColor(new Color(0,0,0,180));
					g.fillRect(730, 185, 220, 36);
					g.drawImage(ima[1], x, y, dx, dy, null);
					g.setFont(Screen.tiles.pixelFontv1);
					g.setColor(Color.white);
					g.drawString("Exp: " + Screen.heroXp + "/" + Screen.upHeroXp[Screen.heroLevel], 745, 208 );
				}
				else {
					g.setColor(new Color(0,0,0,180));
					g.fillRect(730, 185, 220, 36);
					g.drawImage(ima[1], x, y, dx, dy, null);
					g.setFont(Screen.tiles.pixelFontv1);
					g.setColor(Color.white);
					g.drawString("Master of Battle!", 745, 208 );
				}
			}
		}


	};


	
	public static NutBamVip add1 = new NutBamVip( 1090, 340, 25, 25 ) {
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

	public static NutBamVip add2 = new NutBamVip( 1090, 370, 25, 25 ) {
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

	public static NutBamVip add3 = new NutBamVip( 1090, 400, 25, 25 ) {
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

	public static NutBamVip add4 = new NutBamVip( 1090, 430, 25, 25 ) {
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

	public static NutBamVip add5 = new NutBamVip( 1090, 460, 25, 25 ) {
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

	public static NutBamVip menuLose = new NutBamVip( 280, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.gameState = Value.TITLE_SCREEN;
		}
	};
	public static NutBamVip menuWin = new NutBamVip( 280, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.rielLevel++;
			Screen.gameState = Value.TITLE_SCREEN;
		}
	};
	public static NutBamVip again = new NutBamVip( 720, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.gameState = Value.IN_GAME;
			Screen.startGame();
		}
	};
	public static NutBamVip next = new NutBamVip( 720, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.rielLevel++;
			Screen.gameState = Value.IN_GAME;
			Screen.startGame();
		}
	};
	

    //Hàm phụ xử lí logic, thằng nào đụng vào bố chặt tay
	private static void logicGachaBH() {
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

	private static void logicGachaEnhance() {
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

	private static void logicGachaMercenary() {
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

	private static void logicSummon() {
		if(Screen.coinage >=12) {
			Screen.coinage = Screen.coinage - 2;
			Screen.ok=true;
			Screen.randomNum = Screen.rand.nextInt(100) + 1;

			if (Screen.randomNum ==1) {
				Screen.energyCore++;
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

	private static void logicLevelUp() {
		if(Screen.heroLevel>=25) {
			return;
		}
		int curLv = Screen.heroLevel;
		if( Screen.heroXp>=Screen.upHeroXp[curLv] && Screen.coinage>=Screen.upHeroCoin[curLv] && 
			Screen.shard>=Screen.upHeroShard[curLv] && Screen.energyCore>=Screen.upHeroECore[curLv]) {
				Screen.heroXp -= Screen.upHeroXp[curLv];
				Screen.coinage -= Screen.upHeroCoin[curLv];
				Screen.shard -= Screen.upHeroXp[curLv];
				Screen.energyCore -=Screen.upHeroECore[curLv];
				Screen.heroLevel++;
				Screen.remainingPoint +=5;
				Screen.maxPoint = 5*Screen.heroLevel;
		}
	}

	public static void xuLyOutGame() {
		if(Screen.coinDauTran >=60) {
			Screen.coinage = Screen.coinDauTran - 50;
		}
		else {
			Screen.coinage = 50;
		}

		if(Screen.healthDauTran >=2 ) {
			Screen.health = Screen.healthDauTran - 1;
		}
		else {
			Screen.health = 1;
		}

	}
}
