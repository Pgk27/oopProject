import java.awt.*;

public abstract class Tower {
    protected int x, y, width, height;
    protected int rangeSize;
    protected int damage;
    protected int loseTime;
    protected int loseFrame = 0;
    public int id; // ID của tháp để Block nhận diện

    // Quản lý mục tiêu
    protected int shotMob = -1;
    protected boolean shotingMob1 = false;
    protected boolean shotingMob2 = false;
    protected boolean shotingMob3 = false;
    protected Rectangle towerSquare;

    public Tower(int x, int y, int width, int height, int rangeSize, int damage, int loseTime, int id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rangeSize = rangeSize;
        this.damage = damage;
        this.loseTime = loseTime;
        this.id = id;
        
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
            if (loseFrame >= loseTime) {
                if (shotingMob1 && Screen.mobs[shotMob] != null) {
                    Screen.mobs[shotMob].loseHealth(this.damage);
                    if (Screen.mobs[shotMob].isDead()) { shotingMob1 = false; shotMob = -1; Screen.hasWon(); }
                } else if (shotingMob2 && Screen.mobss[shotMob] != null) {
                    Screen.mobss[shotMob].loseHealth(this.damage);
                    if (Screen.mobss[shotMob].isDead()) { shotingMob2 = false; shotMob = -1; Screen.hasWon(); }
                } else if (shotingMob3 && Screen.mobsss[shotMob] != null) {
                    Screen.mobsss[shotMob].loseHealth(this.damage);
                    if (Screen.mobsss[shotMob].isDead()) { shotingMob3 = false; shotMob = -1; Screen.hasWon(); }
                }
                loseFrame = 0; // Đưa thời gian khựng về 0
            } else {
                loseFrame++;
            }
        } else {
            loseFrame = 0; // Không có mục tiêu thì không tích nạp đạn
        }
    }

    
    public abstract void draw(Graphics g);
    public abstract void fight(Graphics g);
}