
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class Frame extends JFrame{
	public static String title = "Tower Defense Demo";
	public static Dimension size = new Dimension(1024, 768);
	
	public Frame() {
		setTitle(title);
		setSize(size);
		setResizable(true); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(1,1));
		/*
		nghia là cửa sổ không bị phân lô bán nền như dưới:
		1 hàng
		┌─────────────────────┐
		│                     │
		│       Screen        │  ← 1 ô duy nhất
		│                     │
		└─────────────────────┘
		1 cột

		nếu chia thành 2,2 thì sẽ như này:
		┌─────────┬─────────┐
		│ Ô 1     │ Ô 2     │
		├─────────┼─────────┤
		│ Ô 3     │ Ô 4     │
		└─────────┴─────────┘
		*/
		Screen screen = new Screen(this);
		/*
		từ new là để cấp phát bộ nhớ động kiểu malloc
		Screen() được gọi là constructor và cấp vào con trỏ this để có gì lấy dữ liệu từ Frame() cho dễ
		*/
		add(screen); //dòng này sẽ hiện cái screen làm xong hiện lên màn hình
		setVisible(true);
	}
	 //hello
	
	
	public static void main(String args[]) {
        try {
            // Tự động load và phát nhạc nền luôn không cần hỏi
            File file = new File("interstellar.wav");
            if (file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY); 
            }
        } catch (Exception e) {
            System.out.println("Không tìm thấy file nhạc nền, bỏ qua phần âm thanh!");
        }

        // Khởi tạo và bật thẳng cửa sổ game lên ngay lập tức
        new Frame();
    }
    
    private boolean isFullScreen = false;
	public void toggleFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        
        dispose();
        if (!isFullScreen) {
            setUndecorated(true);
            device.setFullScreenWindow(this);
            isFullScreen = true;
        } else {
            device.setFullScreenWindow(null);
            setUndecorated(false);
            setSize(size);
            setLocationRelativeTo(null);
            isFullScreen = false;
        }
        setVisible(true);
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

