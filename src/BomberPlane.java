import java.awt.*;

/**
 * Kỹ năng Máy Bay Thả Bom (BomberPlane extends Spell)
 * - Xuất phát từ điểm A ngẫu nhiên cách điểm chỉ định B 1500px ngoài mép map.
 * - Bay theo vector AB cố định thẳng qua map, không chuyển hướng.
 * - Thả bom tại điểm B, tạo hoạt ảnh nổ 8 frame co giãn theo blastRadius (giữ tỷ lệ không méo mó).
 * - Tiêu diệt quái trong bán kính blastRadius, kích hoạt animation quái chết (mobDead()).
 * - Bay tiếp ra mép ngoài đối diện màn hình và tự hủy.
 */
public class BomberPlane extends Spell {
    private double currentX, currentY;
    private double startX, startY;
    private double targetX, targetY;
    private double vx, vy;
    private double speed = 3.0; // Tốc độ bay của máy bay
    private double totalDistance;
    private double distanceTraveled = 0;

    private boolean facingRight;
    private double tiltAngle;

    private boolean hasDroppedBomb = false;
    private boolean explosionFinished = false;
    private boolean planeFinished = false;
    private long explosionStartTime = -1;
    private static final long EXPLOSION_DURATION_MS = 640; // 8 frame x 80ms = 640ms

    // Bán kính nổ linh hoạt (mặc định 2x2 ô ~ 60px, có thể chỉnh sửa/nâng cấp)
    private  int blastRadius = 60;
    private int planeSize = 70; // Kích thước vẽ máy bay

    public BomberPlane(int targetX, int targetY) {
        this(targetX, targetY, 60); // thay ở đây nữa nếu muốn thay bán kính
    }

    public BomberPlane(int targetX, int targetY, int blastRadius) {
        super(targetX, targetY, 0);
        this.targetX = targetX;
        this.targetY = targetY;
        this.blastRadius = blastRadius;

        // 1. Chọn điểm xuất phát A theo góc ngẫu nhiên ngoài mép map (cách B 1500px, đảm bảo ngoài màn hình)
        double randomAngle = Math.random() * Math.PI * 2;
        this.startX = targetX - Math.cos(randomAngle) * 1500;
        this.startY = targetY - Math.sin(randomAngle) * 1500;
        this.currentX = startX;
        this.currentY = startY;

        // 2. Tính toán vector chỉ phương AB chuẩn xác
        double dx = targetX - startX;
        double dy = targetY - startY;
        this.totalDistance = Math.sqrt(dx * dx + dy * dy);

        if (totalDistance > 0) {
            this.vx = (dx / totalDistance) * speed;
            this.vy = (dy / totalDistance) * speed;
        } else {
            this.vx = speed;
            this.vy = 0;
        }

        // 3. Cơ chế lật trái/phải kết hợp góc nghiêng (mũi luôn hướng tới mục tiêu, buồng lái luôn ở trên)
        if (vx >= 0) {
            this.facingRight = true;
            this.tiltAngle = Math.atan2(vy, vx);
        } else {
            this.facingRight = false;
            this.tiltAngle = Math.atan2(vy, -vx);
        }
    }

    public int getBlastRadius() {
        return blastRadius;
    }

    public void setBlastRadius(int blastRadius) {
        this.blastRadius = blastRadius;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void physic() {
        if (finished) return;

        // A. Máy bay di chuyển thẳng tắp theo vector AB
        if(!planeFinished){
            currentX += vx;
            currentY += vy;
            distanceTraveled += speed;
        }

        // B. Thả bom khi máy bay bay qua điểm B
        if (!hasDroppedBomb && distanceTraveled >= totalDistance) {
            dropBomb();
        }

        // C. Kiểm tra vụ nổ kết thúc (sau 640ms)
        if (hasDroppedBomb && !explosionFinished) {
            if (System.currentTimeMillis() - explosionStartTime >= EXPLOSION_DURATION_MS) {
                explosionFinished = true;
            }
        }

        // D. Kiểm tra máy bay đã bay ra khỏi khu vực Room (bản đồ chơi) sau khi thả bom
        if (hasDroppedBomb && !planeFinished) {
            int roomBottom = Screen.room.block[Screen.room.worldHeight - 1][0].y + Screen.room.blockSize;
            int roomLeft = Screen.room.block[0][0].x;
            int roomRight = roomLeft + (Screen.room.worldWidth * Screen.room.blockSize);

            boolean outOfRoom = (currentX < roomLeft - planeSize || 
                                 currentX > roomRight + planeSize || 
                                 currentY < -planeSize || 
                                 currentY > roomBottom + 10);
            if (outOfRoom) {
                planeFinished = true;
            }
        }

        // E. Kết thúc khi cả máy bay đã bay xong và vụ nổ đã tắt hẳn
        if (planeFinished && explosionFinished) {
            finished = true;
        }
    }

    private void dropBomb() {
        hasDroppedBomb = true;
        explosionStartTime = System.currentTimeMillis();

        // Gây chết quái trong bán kính blastRadius và kích hoạt animation quái chết
        killMobsInRadius(Screen.mobs);
        killMobsInRadius(Screen.mobss);
        killMobsInRadius(Screen.mobsss);
    }

    private void killMobsInRadius(Mob[] mobs) {
        if (mobs == null) return;
        for (Mob m : mobs) {
            if (m != null && m.inGame && !m.isDead()) {
                double mobCenterX = m.x + (m.width / 2.0);
                double mobCenterY = m.y + (m.height / 2.0);
                double distSq = Math.pow(mobCenterX - targetX, 2) + Math.pow(mobCenterY - targetY, 2);
                if (distSq <= blastRadius * blastRadius) {
                    m.mobDead(); // Kích hoạt animation chết và cộng điểm hạ gục
                }
            }
        }
    }

    @Override
    public void destroy() {
        // Không chiếm block đất nên không cần dọn block
    }

    @Override
    public void draw(Graphics g) {
        // 1. Vẽ vụ nổ tại điểm B (nếu đang nổ) - Co giãn tương đối theo bán kính nổ không làm méo mó
        if (hasDroppedBomb && !explosionFinished && Screen.explosion != null && Screen.explosion.length > 0) {
            long elapsed = System.currentTimeMillis() - explosionStartTime;
            int frame = (int) ((elapsed * Screen.explosion.length) / EXPLOSION_DURATION_MS);
            if (frame >= Screen.explosion.length) frame = Screen.explosion.length - 1;

            Image expImg = Screen.explosion[frame];
            if (expImg != null) {
                // Co giãn vụ nổ bao phủ đường kính blastRadius và giữ đúng tỷ lệ gốc (không méo mó)
                int expW = (int) (blastRadius * 2.2);
                int expH = (int) (expW * (528.0 / 473.0));
                g.drawImage(expImg, (int) targetX - expW / 2, (int) targetY - expH / 2, expW, expH, null);
            }
        }

        // 2. Vẽ máy bay xoay theo vector & lật chuẩn chỉ (chỉ vẽ khi máy bay chưa bay xong)
        if (!planeFinished && Screen.airplane != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(currentX, currentY);

            if (facingRight) {
                // Bay sang phải: lật ngang (-planeSize) để mũi hướng sang phải, xoay góc nghiêng
                g2d.rotate(tiltAngle);
                g2d.drawImage(Screen.airplane, -planeSize / 2, -planeSize / 2, planeSize, planeSize, null);
            } else {
                // Bay sang trái: mũi tự nhiên đã ở bên trái, xoay góc nghiêng
                g2d.rotate(tiltAngle);
                g2d.drawImage(Screen.airplane, planeSize / 2, -planeSize / 2, -planeSize, planeSize, null);
            }

            g2d.dispose();
        }
    }
}

