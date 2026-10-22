package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NutBamVip {
    public BufferedImage[] ima = new BufferedImage[3];
    int x, y, dx, dy;
    public int state = 0;

    // state = 0: bình thường
    // state = 1: trỏ chuột vào
    // state = 2: đang nhấn

    boolean startPressInNut = false;

    public NutBamVip(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
    

    // Kiểm tra tọa độ chuột có nằm trong nút ko
    public boolean contains(int mouseX, int mouseY) {
        if( mouseX >= x && mouseX <= x + dx && mouseY >= y && mouseY < y + dy){
            return true;
        }
        return false;
    }

    // Cập nhật trạng thái khi chuột di chuyển
    public void updateMouse(int mouseX, int mouseY) {
        boolean inside = contains(mouseX, mouseY);

        if (startPressInNut) {
            if (inside == true) {
                state = 2;
            } else {
                state = 0;
            }
        } else {
            if (inside == true) {
                state = 1;
            } else {
                state = 0;
            }
        }
    }

    // logic nhấn chuột
    public void mousePressed(int mouseX, int mouseY) {
        if (contains(mouseX, mouseY)) {
            startPressInNut = true;
            state = 2;
        }
    }

    // logic thả chuột
    public void mouseReleased(int mouseX, int mouseY) {
        // Chỉ kích hoạt nếu nhấn bên trong và thả bên trong
        boolean shouldClick;
        if (this.startPressInNut && this.contains(mouseX, mouseY)) {
            shouldClick = true;
        }
        else {
            shouldClick = false;
        }

        startPressInNut = false;

        // Cập nhật lại trạng thái sau khi thả
        updateMouse(mouseX, mouseY);

        if (shouldClick) {
            onClick();
        }
    }

    // Hành động khi bấm nút
    public void onClick() {
        
    }

    public void renderNut(Graphics g) {
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
