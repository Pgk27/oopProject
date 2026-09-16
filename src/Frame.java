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
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridLayout(1,1));
		
		Screen screen = new Screen(this);
		add(screen);
		
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
				Frame frame = new Frame(); // pencereyi çalıştıran kod
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