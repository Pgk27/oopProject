package tower;
import java.awt.*;
import java.net.URL;

import core.Screen;
import entity.Projectiles;
import entity.mob.Mob;

public abstract class Tower {
    protected int x, y, width, height;
    protected int rangeSize;
    protected int animFrame = 0; 
    protected int animTick = 0;
    protected int animSpeed = 40;
    protected int towerAnimFrame = 0;
    protected int shootFrame; // frame bắn của từng tháp
    protected double damage;

    public int id; // ID của tháp để Block nhận diện

    // Quản lý mục tiêu
    protected int shotMob = -1;
    protected boolean shotingMob1 = false;
    protected boolean shotingMob2 = false;
    protected boolean shotingMob3 = false;
    protected boolean hasDealtDamage = false;
    protected Rectangle towerSquare;

    protected URL shootSound;

    public Tower(int x, int y, int width, int height, int rangeSize, int damage, int shootFrame, int id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rangeSize = rangeSize;
        this.damage = damage;
        this.id = id;

        this.animFrame = Screen.rand.nextInt(8);
		this.animTick = Screen.rand.nextInt(20);
		this.animSpeed = 36 + Screen.rand.nextInt(10);
        
        // Khởi tạo tầm bắn ngay khi xây tháp
        this.towerSquare = new Rectangle(x - (rangeSize / 2), y - (rangeSize / 2), width + rangeSize, height + rangeSize);
    }

    // Tính khoảng cách bình phương (Tối ưu hiệu năng, không dùng căn bậc 2)
    protected double distanceCheck(Rectangle mob) {
        double towerCenterX = this.x + (this.width / 2.0);
        double towerCenterY = this.y + (this.height / 2.0);
        double mobCenterX = mob.x + (mob.width / 2.0);
        double mobCenterY = mob.y + (mob.height / 2.0);

        double dx = towerCenterX - mobCenterX;
        double dy = towerCenterY - mobCenterY;
        return (dx * dx) + (dy * dy);
    }

    public void physic() {
        animTick++;
		if (animTick >= animSpeed){
			animFrame++;
			animTick = 0;
		}

        // A. KIỂM TRA MỤC TIÊU CŨ (Mất dấu hoặc quái chết thì buông mục tiêu ngay)
        if (shotMob != -1) {
            if (shotingMob1 && (Screen.mobs[shotMob] == null || Screen.mobs[shotMob].isDead() || !Screen.mobs[shotMob].inGame || !towerSquare.intersects(Screen.mobs[shotMob]))) {
                shotingMob1 = false;
            }
            else if (shotingMob2 && (Screen.mobss[shotMob] == null || Screen.mobss[shotMob].isDead() || !Screen.mobss[shotMob].inGame || !towerSquare.intersects(Screen.mobss[shotMob]))) {
                shotingMob2 = false;
            }
            else if (shotingMob3 && (Screen.mobsss[shotMob] == null || Screen.mobsss[shotMob].isDead() || !Screen.mobsss[shotMob].inGame || !towerSquare.intersects(Screen.mobsss[shotMob]))) {
                shotingMob3 = false;
            }

            // Nếu không còn khóa mục tiêu nào thì reset index
            if (!shotingMob1 && !shotingMob2 && !shotingMob3) {
                shotMob = -1;
            }
        }

        // B. TÌM KIẾM MỤC TIÊU MỚI GẦN NHẤT
        if (shotMob == -1) {
            double closestDistance = Double.MAX_VALUE;
            int potentialTarget = -1;
            int targetGroup = 0; // 1: mobs, 2: mobss, 3: mobsss

            // Quét nhóm quái 1
            for (int i = 0; i < Screen.mobs.length; i++) {
                if (Screen.mobs[i] != null && Screen.mobs[i].inGame && !Screen.mobs[i].isDead() && towerSquare.intersects(Screen.mobs[i])) {
                    double dist = distanceCheck(Screen.mobs[i]);
                    if (dist < closestDistance) {
                        closestDistance = dist;
                        potentialTarget = i;
                        targetGroup = 1;
                    }
                }
            }
            // Quét nhóm quái 2
            for (int i = 0; i < Screen.mobss.length; i++) {
                if (Screen.mobss[i] != null && Screen.mobss[i].inGame && !Screen.mobss[i].isDead() && towerSquare.intersects(Screen.mobss[i])) {
                    double dist = distanceCheck(Screen.mobss[i]);
                    if (dist < closestDistance) {
                        closestDistance = dist;
                        potentialTarget = i;
                        targetGroup = 2;
                    }
                }
            }
            // Quét nhóm quái 3
            for (int i = 0; i < Screen.mobsss.length; i++) {
                if (Screen.mobsss[i] != null && Screen.mobsss[i].inGame && !Screen.mobsss[i].isDead() && towerSquare.intersects(Screen.mobsss[i])) {
                    double dist = distanceCheck(Screen.mobsss[i]);
                    if (dist < closestDistance) {
                        closestDistance = dist;
                        potentialTarget = i;
                        targetGroup = 3;
                    }
                }
            }

            // Ghi nhận mục tiêu gần nhất tìm được
            if (potentialTarget != -1) {
                shotMob = potentialTarget;
                if (targetGroup == 1) shotingMob1 = true;
                else if (targetGroup == 2) shotingMob2 = true;
                else if (targetGroup == 3) shotingMob3 = true;
            }
        }

        // LOGIC TRỪ MÁU THEO THỜI GIAN KHỰNG
        if (shotingMob1 || shotingMob2 || shotingMob3) {
            Mob target = null;
            if (shotingMob1 && Screen.mobs[shotMob] != null) target = Screen.mobs[shotMob];
            else if (shotingMob2 && Screen.mobss[shotMob] != null) target = Screen.mobss[shotMob];
            else if (shotingMob3 && Screen.mobsss[shotMob] != null) target = Screen.mobsss[shotMob];
            if (towerAnimFrame == shootFrame) {
                double finalDamage = damage * (1-target.dmgReduction);

                double startX = x + width/2;
				double startY = y + height/2;
                if (target != null && !hasDealtDamage){
                    playShootSound();
                    Screen.pjt.add(new Projectiles(startX, startY, target, finalDamage, id));
					hasDealtDamage = true;
					if (target.isDead()) {
						shotMob = -1;
						shotingMob1 = false;
						shotingMob2 = false;
						shotingMob3 = false;
						Screen.hasWon();
					}
                }
            } else {
                hasDealtDamage = false;
            }
        }
    }

    protected void playShootSound(){
        if (shootSound != null){
            Screen.sound.playSfx(shootSound);
        }
    }

    public abstract void draw(Graphics g);
    public abstract void fight(Graphics g);
}