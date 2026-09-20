
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class Frame extends JFrame{
	public static String title = "Cmpe 114 Tower Defense";
	public static Dimension size = new Dimension(700, 600);
	
	public Frame() {
		setTitle(title);
		setSize(size);
		setResizable(false); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(1,1));
		
		Screen screen = new Screen(this);
		add(screen);
		
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
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

