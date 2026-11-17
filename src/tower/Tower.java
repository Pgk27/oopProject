package tower;
import java.awt.*;
import java.net.URL;

import core.Screen;
import entity.Projectiles;
import entity.enemy.Mob;

public abstract class Tower {
    protected int x, y, width, height;
    protected int rangeSize;
    protected int animFrame = 0; 
    protected int animTick = 0;
    protected int animSpeed = 40;
    protected int towerAnimFrame = 0;
    protected int shootFrame; // frame bắn của từng tháp
    protected double damage;
    protected double attackSpeed = 1.0;
    protected long lastAttackTime = 0;
    protected boolean isAttacking = false;

    public int id; // ID của tháp để Block nhận diện

    // Quản lý mục tiêu
    protected Mob target = null;
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
        if (target != null && (target.isDead() || !towerSquare.intersects(target.getBounds()))){
            target = null;
        }

        // B. TÌM KIẾM MỤC TIÊU MỚI GẦN NHẤT
        if (target == null) {
            double closestDistance = Double.MAX_VALUE;
            for (Mob mob : Screen.activeMobs){
                if (!mob.isDead() && towerSquare.intersects(mob.getBounds())){
                    double dist = distanceCheck(mob.getBounds());
                    if (dist < closestDistance){
                        closestDistance = dist;
                        target = mob;
                    }
                }
            }
        }

        // C. LOGIC BẮN VÀ HOẠT ẢNH TẤN CÔNG
        if (target != null) {
            long now = System.currentTimeMillis();
            long cooldownMs = (long) (1000.0 / attackSpeed);

            if (lastAttackTime == 0) {
                lastAttackTime = now;
                hasDealtDamage = false;
                isAttacking = true;
            }

            long elapsed = now - lastAttackTime;
            if (elapsed >= cooldownMs) {
                lastAttackTime = now;
                hasDealtDamage = false;
                elapsed = 0;
                isAttacking = true;
            }

            int totalFrames = getTotalAnimationFrames();
            long frameDurationMs = animSpeed * 2;
            long attackAnimDurationMs = totalFrames * frameDurationMs;

            if (elapsed < attackAnimDurationMs) {
                isAttacking = true;
                towerAnimFrame = (int) (elapsed / frameDurationMs);
                if (towerAnimFrame >= totalFrames) {
                    towerAnimFrame = totalFrames - 1;
                }
            } else {
                isAttacking = false;
                towerAnimFrame = 0; // Trở về idle frame 0
            }

            if (towerAnimFrame >= shootFrame) {
                double startX = x + width/2;
				double startY = y + height/2;
                if (target != null && !hasDealtDamage){
                    double damageMultiplier = Screen.store == null ? 1.0 : Screen.store.getTowerDamageMultiplier();
                    double finalDamage = damage * damageMultiplier * (1-target.dmgReduction);
                    playShootSound();
                    Screen.pjt.add(new Projectiles(startX, startY, target, finalDamage, id));
					hasDealtDamage = true;
					if (target.isDead()) {
						target = null;
						Screen.hasWon();
					}
                }
            } else {
                hasDealtDamage = false;
            }
        } else {
            lastAttackTime = 0;
            hasDealtDamage = false;
            isAttacking = false;
            towerAnimFrame = 0;
        }
    }

    protected void playShootSound(){
        if (shootSound != null){
            Screen.sound.playSfx(shootSound);
        }
    }

    protected int getTotalAnimationFrames() {
        return 8;
    }

    // In Tower.java
    protected void drawOriented(Graphics g, Image frame) {
        if (frame == null) return;
        
        // If there is a target, face it; otherwise face right by default
        int targetX = (target != null) ? target.getX() : (this.x + width);
        if (targetX < this.x + width / 2) {
            // Target is to the left -> flip horizontally using negative width
            g.drawImage(frame, x + width, y, -width, height, null);
        } else {
            // Target is to the right -> draw normally
            g.drawImage(frame, x, y, width, height, null);
        }
    }

    public abstract void draw(Graphics g);
}
