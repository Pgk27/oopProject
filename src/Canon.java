import java.awt.*;

public class Canon extends Tower {

    public Canon(int x, int y, int width, int height) {
        // Truyền thông số: Tầm bắn 70, Sát thương 2, Thời gian khựng 50, ID tháp
        super(x, y, width, height, 70, 2, 50, Value.airTowerLaser3); //check id
    }

    @Override
    public void draw(Graphics g) {
    
        if (Screen.cannon == null || Screen.cannon.length == 0) {
            return; 
        }

        int animationFrame = 0;
        
        // Chỉ chạy hoạt ảnh khi đang khóa mục tiêu, nếu không thì đứng im ở frame 0
        if (shotingMob1 || shotingMob2 || shotingMob3) {
            animationFrame = Screen.AnimFrame % Screen.cannon.length;
        }
        
        Image currentFrame = Screen.cannon[animationFrame];
        
        // Tìm tọa độ mục tiêu để lật ảnh
        int targetX = this.x; 
        if (shotMob != -1) {
            if (shotingMob1 && shotMob < Screen.mobs.length && Screen.mobs[shotMob] != null) 
                targetX = Screen.mobs[shotMob].x;
            else if (shotingMob2 && shotMob < Screen.mobss.length && Screen.mobss[shotMob] != null) 
                targetX = Screen.mobss[shotMob].x;
            else if (shotingMob3 && shotMob < Screen.mobsss.length && Screen.mobsss[shotMob] != null) 
                targetX = Screen.mobsss[shotMob].x;
        }
        
        // Vẽ và lật ảnh dựa vào vị trí quái vật
        if (targetX < this.x) {
            // lật bên trái
            g.drawImage(currentFrame, x + width, y, -width, height, null);
        } else {
            // lật bên phải
            g.drawImage(currentFrame, x, y, width, height, null);
        }
    }

    @Override
    public void fight(Graphics g) {
        // Chỉ vẽ tia laser khi đã khóa được mục tiêu
        if (shotMob == -1) return;
        g.setColor(new Color(51, 153, 92));

        int startX = x + (width / 2);
        int startY = y + (height / 2);
        
        if (shotingMob1 && shotMob < Screen.mobs.length && Screen.mobs[shotMob] != null) {
            int targetX = Screen.mobs[shotMob].x + (Screen.mobs[shotMob].width / 2);
            int targetY = Screen.mobs[shotMob].y + (Screen.mobs[shotMob].height / 2);
            g.drawLine(startX, startY, targetX, targetY);
        }
        else if (shotingMob2 && shotMob < Screen.mobss.length && Screen.mobss[shotMob] != null) {
            int targetX = Screen.mobss[shotMob].x + (Screen.mobss[shotMob].width / 2);
            int targetY = Screen.mobss[shotMob].y + (Screen.mobss[shotMob].height / 2);
            g.drawLine(startX, startY, targetX, targetY);
        }
        else if (shotingMob3 && shotMob < Screen.mobsss.length && Screen.mobsss[shotMob] != null) {
            int targetX = Screen.mobsss[shotMob].x + (Screen.mobsss[shotMob].width / 2);
            int targetY = Screen.mobsss[shotMob].y + (Screen.mobsss[shotMob].height / 2);
            g.drawLine(startX, startY, targetX, targetY);
        }
    }
}