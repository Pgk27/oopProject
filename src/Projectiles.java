import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Projectiles {
    double x, y;
    double damage;
    double angle;
    Mob target;
    int towerID = -1; // ID của tower
    double speed = 1.5; // Tốc độ bay của mũi tên
    boolean hasRemoved = false;
    
    public Projectiles(double startX, double startY, Mob target, double damage, int towerID) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.damage = damage;
        this.towerID = towerID;
    }

    public void physic() {
        if (hasRemoved) return;

        if (target.isDead() || !target.inGame || target.isDead()) {
            hasRemoved = true; // Quái chết trước khi tên bay đến thì hủy mũi tên
            return;
        }

        // Tính toán hướng bay bám đuổi mục tiêu
        double targetCenterX = target.x + (target.width / 2.0);
        double targetCenterY = target.y + (target.height / 2.0);
        
        double dx = targetCenterX - x;
        double dy = targetCenterY - y;
        double dist= Math.sqrt(dx * dx + dy * dy);

        // tính góc xoay hướng vào quái
        this.angle = Math.atan2(dy, dx);

        // Nếu bay trúng mục tiêu (khoảng cách rất nhỏ)
        if (dist < speed) {
            hasRemoved = true;
            target.loseHealth(damage);
        } else {
            // mũi tên bay về phía quái
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }
    }

    public Image getSprite(){
        return Screen.projectiles[towerID-2]; // -2 tại vì trong Value thì các tower bắt đầu từ 2 
    }

    public void draw(Graphics g) {
        Image img = getSprite();
        if (img == null) return;

        Graphics2D g2d = (Graphics2D) g.create(); // tạo bản sao context vẽ để không ảnh hưởng tới các vật thể khác

        int imgW = (int)img.getWidth(null);
        int imgH = (int)img.getHeight(null);

        // dùng AffineTransform để dịch chuỷen tới tọa độ (x,y) và xoay quanh tâm mũi tên
        AffineTransform tx = new AffineTransform();
        tx.translate(x,y);
        tx.rotate(angle);

        //update kích thước do mở rộng map, tăng số lượng, giảm kích thước block
        double scale=0.8;
        tx.scale(scale, scale);


        tx.translate(-imgW / 2.0, -imgH/ 2.0); // căn tâm ảnh trùng đúng vị trí (x,y)

        g2d.drawImage(img, tx, null);
        g2d.dispose(); // giống việc free(g2d), bỏ animation thủ công, bớt việc cho Garbage Collector trong java
    }
}
