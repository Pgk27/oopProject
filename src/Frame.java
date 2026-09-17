import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

public class Frame extends JFrame{
	public static String title = "Tower Defense Demo";
	public static Dimension size = new Dimension(700, 600);
	
	public Frame() {
		setTitle(title);
		setSize(size);
		setResizable(false); // không cho sửa kích thước cửa sổ
		setLocationRelativeTo(null); //đặt cửa sổ ở đâu đó
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // khi bấm nút x sẽ thoát chương trình
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

	
	public static void main(String args[]) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
     	Scanner scanner = new Scanner(System.in);
     
		File file = new File("interstellar.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);  // interstellar theme çalması için gereken kodlar 
		Clip clip = AudioSystem.getClip();									//internetten müzik çalmak için bulduğumuz kod dizisi
		clip.open(audioStream);												//duruma göre kaldırılabilir
		
		String response = "";
		
		while(!response.equals("Q")) {
			System.out.println("Press P to start playing\nP = play, S = Stop, R = Reset, Q = Quit");
			System.out.print("Enter your choice: ");
			response = scanner.next();
			response = response.toUpperCase();
			switch(response) {
				case "p":
				case ("P"): clip.start();
				Frame frame = new Frame(); 
				break;
				case "s":
				case ("S"): clip.stop();
				break;
				case "r":
				case ("R"): clip.setMicrosecondPosition(0);
				break;
				case "q":
				case ("Q"): clip.close();
				break;
				default: System.out.println("Not a valid response");
			}
		}
		scanner.close();
		System.out.println("Byeeee!");	
	}
}