import java.awt.*; // để tạm đây nhé chứ nếu cho cái này vào logic nó loạn lắm
public class Arrow {
    public double x, y;
    public int speed = 5; // Tốc độ bay của mũi tên
    public int damage;
    public Mob target;
    public boolean isDead = false; // Đánh dấu mũi tên đã trúng đích

    public Arrow(double startX, double startY, Mob target, int damage) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.damage = damage;
    }

    public void tick() {
        if (target.isDead() || !target.inGame) {
            isDead = true; // Quái chết trước khi tên bay đến thì hủy mũi tên
            return;
        }

        // Tính toán hướng bay bám đuổi mục tiêu
        double targetX = target.x + (target.width / 2.0);
        double targetY = target.y + (target.height / 2.0);
        
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Nếu bay trúng mục tiêu (khoảng cách rất nhỏ)
        if (distance < speed) {
            target.loseHealth(damage);
            isDead = true;
        } else {
            // Di chuyển mũi tên từng bước một
            x += (deltaX / distance) * speed;
            y += (deltaY / distance) * speed;
        }
    }

    public void draw(Graphics g) {
        // Tạm thời vẽ mũi tên là một chấm vuông/tròn màu trắng. 
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, 5, 5); 
    }
}
