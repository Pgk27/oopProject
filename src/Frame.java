import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame{
	public static String title = "FINAL KINGDOM";
	public static Dimension size = new Dimension(1220, 780);
	public static boolean isFullscreen = false;
	private Screen screen;
	
	public Frame() {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Chạy code = mhinh tỉ lệ gốc
		// Đừng xóa cục này
		// Tao phải xài cục này để check tọa độ gốc cho dễ


		// setSize(size);
		// setLocationRelativeTo(null);

		// Fullscreen setup: borderless, maximized to screen
		setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		setLayout(new GridLayout(1, 1));
		screen = new Screen(this);
		add(screen);

		// Global hotkey: F11 to toggle fullscreen / windowed mode
		screen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullscreen");
		screen.getActionMap().put("toggleFullscreen", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleFullscreen();
			}
		});

		setVisible(true);
	}

	public void toggleFullscreen() {
		dispose();
		isFullscreen = !isFullscreen;
		if (isFullscreen) {
			setUndecorated(true);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setBounds(0, 0, screenSize.width, screenSize.height);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			setUndecorated(false);
			setExtendedState(JFrame.NORMAL);
			setSize(size);
			setLocationRelativeTo(null);
		}
		setVisible(true);
	}
	
	public static void main(String args[]) {
        new Frame();
    }
}
