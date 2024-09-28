package Login;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.Color;

public interface Iframe {
	void base_frame(JFrame frame, String frame_name, int frame_width, int frame_height, Color color);
	void button_design(JButton button, String button_name, int button_x, int button_y, int button_width, int button_height, Color bg_color, Color fg_color);
}