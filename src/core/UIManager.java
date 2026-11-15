package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import service.GachaService;
import service.HeroUpgradeService;

public class UIManager {
    public static int[] countNut = new int[35]; // Mở rộng từ 25 -> 35
    public static UIButton[][] heThongNut = new UIButton[35][20]; // Mở rộng từ [15][15] -> [35][20]
	public Screen screen;

    public UIManager(Screen screen) {
		this.screen = screen;
        for(int i = 0; i < countNut.length; i++) {
            countNut[i] = 0;
        }

		// Title Screen
		addNut(playgame, Constants.TITLE_SCREEN);
		addNut(store, Constants.TITLE_SCREEN);
		addNut(updateHero, Constants.TITLE_SCREEN);
		addNut(quitgame, Constants.TITLE_SCREEN);
		addNut(setting, Constants.TITLE_SCREEN);
		addNut(toGacha, Constants.TITLE_SCREEN);

		// In-game
		addNut(backToMenu, Constants.IN_GAME);
		addNut(GoldenPickaxeButton, Constants.IN_GAME);
		addNut(fireSwordButton, Constants.IN_GAME);
		addNut(cursedSkullButton, Constants.IN_GAME);
		addNut(bomberButton, Constants.IN_GAME);
		addNut(skillButton, Constants.IN_GAME);

		//Select Skill
		addNut(selectBH, Constants.SKILL_SELECT);
		addNut(selectEnh, Constants.SKILL_SELECT);
		addNut(selectMer, Constants.SKILL_SELECT);
		addNut(selectSkillFrame, Constants.SKILL_SELECT);


		// Buy Item
		addNut(backInGacha, Constants.ITEM_SHOP);
		addNut(buyGoldenPickaxe, Constants.ITEM_SHOP);
		addNut(buyFireSword, Constants.ITEM_SHOP);
		addNut(buyCursedSkull, Constants.ITEM_SHOP);
		addNut(buyPaperPlane, Constants.ITEM_SHOP);
		addNut(buyHeart, Constants.ITEM_SHOP);


		// Gacha
		addNut(in4gacha, Constants.GACHA);
		addNut(shardShop, Constants.GACHA);
		addNut(backInGacha, Constants.GACHA);
		addNut(gachaBH, Constants.GACHA);
		addNut(gachaEnhance, Constants.GACHA);
		addNut(gachaMer, Constants.GACHA);
		addNut(summon, Constants.GACHA);
		addNut(iconShardShop, Constants.GACHA);
		addNut(info, Constants.GACHA);

		// Shard Shop
		addNut(shardShopBigFramee, Constants.SHARD_SHOP);
		addNut(doiBH, Constants.SHARD_SHOP);
		addNut(doiEnhance, Constants.SHARD_SHOP);
		addNut(doiMer, Constants.SHARD_SHOP);
		addNut(doiEC, Constants.SHARD_SHOP);
		
		// Thông báo
		addNut(thongBaoFrame, Constants.NOTI);
		
		// Gacha Rate
		addNut(gachaRateFrame, Constants.GACHA_RATE);

		// Update Hero
		addNut(backInGameShop, Constants.UPDATE_HERO);
		addNut(levelUp, Constants.UPDATE_HERO);
		addNut(expBar, Constants.UPDATE_HERO);
		addNut(add1, Constants.UPDATE_HERO);
		addNut(add2, Constants.UPDATE_HERO);
		addNut(add3, Constants.UPDATE_HERO);
		addNut(add4, Constants.UPDATE_HERO);
		addNut(add5, Constants.UPDATE_HERO);

		// Tong Ket Thua
		addNut(menuLose, Constants.DEFEAT_SCREEN);
		addNut(again, Constants.DEFEAT_SCREEN);
		// addNut(expBarTK, Value.DEFEAT_SCREEN);

		// Tong Ket Win
		addNut(menuWin, Constants.VICTORY_SCREEN);
		addNut(next, Constants.VICTORY_SCREEN);
    }
   
    public static void addNut(UIButton x, int gState) {
        heThongNut[gState][countNut[gState]] = x;
        countNut[gState] ++;
    }

	

    private abstract static class InGameItemButton extends UIButton {
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

    private static void drawBuffItem(Graphics g, UIButton button, Image icon,
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

    public static UIButton skillButton = new InGameItemButton(4) {
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
            int skillID = Constants.PLACEHOLDER;
            switch (Screen.chosenCard) {
                case 1:
                    icon = Screen.tiles.iconBH;
                    skillID = Constants.SPELL_BLACK_HOLE;
                    break;
                case 2:
                    icon = Screen.tiles.iconEnhance;
                    break;
                case 3:
                    icon = Screen.tiles.iconMercenary;
                    skillID = Constants.SPELL_MERCENARY;
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

	public static UIButton GoldenPickaxeButton = new InGameItemButton(0) {
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

    public static UIButton fireSwordButton = new InGameItemButton(1) {
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

    public static UIButton cursedSkullButton = new InGameItemButton(2) {
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

    public static UIButton bomberButton = new InGameItemButton(3) {
        @Override
        protected boolean canUse() {
            return Screen.store.canSelectBomberPlane();
        }

        @Override
        public void onClick() {
            if (Screen.gameState == Constants.IN_GAME && Screen.store != null) {
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

            if (Screen.store != null && Screen.store.holdsItem && Store.heldID == Constants.SPELL_BOMBER_PLANE) {
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
	

    public static UIButton playgame = new UIButton(880, 495, 144, 72) { 
        @Override 
        public void onClick() {
			if (!Screen.unlockBH && !Screen.unlockEnhance && !Screen.unlockMercenary) {
				Screen.gameState = Constants.IN_GAME;
				Screen.startGame();
			}
			else {
				Screen.gameState = Constants.SKILL_SELECT;
			}
        }
    }; 

    public static UIButton store = new UIButton(180, 590, 144, 72) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.ITEM_SHOP;
        }
    };


	public static UIButton backToMenu = new UIButton(120, 672, 55, 37) {
		@Override
		public void onClick() {
			Screen.gameState = Constants.TITLE_SCREEN;
			xuLyOutGame();
		}

	};

	public static UIButton updateHero = new UIButton(180, 440, 144, 72) {
        @Override
        public void onClick() {
			Screen.gameState = Constants.UPDATE_HERO;
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

    public static UIButton quitgame = new UIButton(880, 570, 144, 72) {
        @Override
        public void onClick() {
            System.exit(0);
        }
    };

	public static UIButton toGacha = new UIButton(180, 515, 144, 72 ) {
		@Override
		public void onClick() {
			Screen.gameState = Constants.GACHA;
			
		}
	};



	public static UIButton setting = new UIButton( 5, 5, 40, 45 ) {
		@Override
		public void onClick() {
			Screen.gameState = Constants.SETTING;
			
		}
	};

	public static UIButton selectSkillFrame = new UIButton( 50, 50, 1120, 680 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
        if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
            return false;
        }
        return true;
        }
        public void onClick() {
            Screen.gameState = Constants.TITLE_SCREEN;
        }
		public void renderNut(Graphics g){
			g.drawImage(Screen.tiles.selectSkill, x, y, dx, dy, null);
			g.drawImage(Screen.tiles.cardBH, 140, 200, 300, 415, null);
			g.drawImage(Screen.tiles.cardEnh, 460, 200, 300, 415, null);
			g.drawImage(Screen.tiles.cardMer, 780, 200, 300, 415, null);
		}
    };

	public static UIButton selectBH = new UIButton( 210, 542, 160, 35 ) {
		@Override
		public void onClick() {
			if(Screen.unlockBH == true) {
				Screen.chosenCard = 1;
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

	public static UIButton selectEnh = new UIButton( 530, 542, 160, 35 ) {
		@Override
		public void onClick() {
			if(Screen.unlockEnhance == true) {
				Screen.chosenCard = 2;
				Screen.strChosenCard = "Enhancement";
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

	public static UIButton selectMer = new UIButton( 850, 542, 160, 35 ) {
		@Override
		public void onClick() {
			if(Screen.unlockMercenary == true) {
				Screen.chosenCard = 3;
				Screen.strChosenCard = "Mercenary";
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

	

	public static UIButton backInGameShop = new UIButton( 5, 5, 50, 35 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.TITLE_SCREEN;
            
        }
    };
    
    public static UIButton backInGacha = new UIButton( 5, 5, 50, 35 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.TITLE_SCREEN;
            
        }
    };

	public static UIButton buyGoldenPickaxe = new UIButton( 350, 465, 80, 30 ) {
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

	public static UIButton buyFireSword = new UIButton( 622, 465, 80, 30 ) {
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

	public static UIButton buyCursedSkull = new UIButton( 350, 648, 80, 30 ) {
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
	public static UIButton buyPaperPlane = new UIButton( 622, 648, 80, 30 ) {
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

	public static UIButton buyHeart = new UIButton( 938, 646, 56, 35 ) {
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
    
    public static UIButton in4gacha = new UIButton( 1110, 20, 40, 40 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.GACHA_RATE;
            
        }
    };
    
    public static UIButton shardShop = new UIButton( 1100, 80, 65, 65 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.SHARD_SHOP;
            
        }
    };
    
    
    public static UIButton gachaBH = new UIButton( 500, 345,  85, 50 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.NOTI;
            Screen.gachaType = Screen.gachaBH;
            GachaService.rollSkillGacha(GachaService.TARGET_BLACK_HOLE);
        }
    };
    
    public static UIButton gachaEnhance = new UIButton( 500, 475,  85, 50 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.NOTI;
            Screen.gachaType = Screen.gachaEnhance;
            GachaService.rollSkillGacha(GachaService.TARGET_ENHANCE);
        }
    };

    public static UIButton gachaMer = new UIButton( 500, 610,  90, 53 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.NOTI;
            Screen.gachaType = Screen.gachaMercenary;
            GachaService.rollSkillGacha(GachaService.TARGET_MERCENARY);
        }
    };

    public static UIButton summon = new UIButton( 800, 545, 125, 104 ) {
        @Override
        public void onClick() {
            Screen.gameState = Constants.NOTI;
			Screen.gachaType = Screen.summon;
			GachaService.rollSummon();
        }
    };

	public static UIButton iconShardShop = new UIButton( 1110, 75, 50, 60 ) {
		@Override
		public void onClick() {
			Screen.gameState = Constants.SHARD_SHOP;
			
		}
	};

	public static UIButton info = new UIButton( 1110, 20, 50, 50 ) {
		@Override 
		public void onClick() {
			Screen.gameState = Constants.GACHA_RATE;
		}


	};
  
    public static UIButton gachaRateFrame = new UIButton( 300, 5, 600, 750 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
        if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
            return false;
        }
        return true;
        }
        public void onClick() {
            Screen.gameState = Constants.GACHA;
        }
    };


    
    public static UIButton shardShopBigFramee = new UIButton( 60, 30, 900, 680 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
            if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
                return false;
            }
            return true;
        }
        public void onClick() {
            Screen.gameState = Constants.GACHA;
            
        }
		
    };


    
    public static UIButton doiBH = new UIButton( 670, 210, 95, 30 ) {
        @Override
        public void onClick() {
            GachaService.exchangeBlackHole();
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

    public static UIButton doiEnhance = new UIButton( 670, 290, 95, 30 ) {
        @Override
        public void onClick() {
            GachaService.exchangeEnhance();
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
    public static UIButton doiMer = new UIButton( 670, 370, 95, 30 ) {
        @Override
        public void onClick() {
            GachaService.exchangeMercenary();
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

    public static UIButton doiEC = new UIButton( 670, 450, 95, 30 ) {
        @Override
        public void onClick() {
            GachaService.exchangeEnergyCore();
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

    public static UIButton thongBaoFrame = new UIButton( 360, 140, 500, 400 ) {
        @Override
        public boolean contains(int mouseX, int mouseY) {
            if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
                return false;
            }
            return true;
        }
        public void onClick() {
            Screen.gameState = Constants.GACHA;
			Screen.manageNut.screen.paintImmediately(0, 0, Screen.WIDTH, Screen.HEIGHT);
            
        }
    };

	public static UIButton levelUp = new UIButton ( 980, 180, 110, 40 ) {
		@Override
		public void onClick() {
			HeroUpgradeService.levelUp();
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

	public static UIButton expBar = new UIButton( 700, 170, 280, 60 ){
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


	
	public static UIButton add1 = new UIButton( 1090, 340, 25, 25 ) {
		@Override
		public void onClick() {
			HeroUpgradeService.upgradeHp();
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

	public static UIButton add2 = new UIButton( 1090, 370, 25, 25 ) {
		@Override
		public void onClick() {
			HeroUpgradeService.upgradeHpRegen();
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

	public static UIButton add3 = new UIButton( 1090, 400, 25, 25 ) {
		@Override
		public void onClick() {
			HeroUpgradeService.upgradeShield();
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

	public static UIButton add4 = new UIButton( 1090, 430, 25, 25 ) {
		@Override
		public void onClick() {
			HeroUpgradeService.upgradeAttackDamage();
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

	public static UIButton add5 = new UIButton( 1090, 460, 25, 25 ) {
		@Override
		public void onClick() {
			HeroUpgradeService.upgradeSkillDamage();
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

	public static UIButton menuLose = new UIButton( 280, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.gameState = Constants.TITLE_SCREEN;
		}
	};
	public static UIButton menuWin = new UIButton( 280, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.rielLevel++;
			Screen.gameState = Constants.TITLE_SCREEN;
		}
	};
	public static UIButton again = new UIButton( 720, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.startGame();
		}
	};
	public static UIButton next = new UIButton( 720, 580, 220, 55 ) {
		@Override 
		public void onClick() {
			Screen.rielLevel++;
			Screen.startGame();
		}
	};
	

	public static void xuLyOutGame() {
		HeroUpgradeService.resetOutGame();
	}
}
