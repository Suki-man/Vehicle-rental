package Login;

import javax.swing.*;
import java.awt.Color;

public class frameDesign {

    public JFrame base_frame(JFrame frame, String title, int width, int height, Color color) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(color);
        frame.setLayout(null);
        return frame;
    }

    public JButton button_design(JButton button, String text, int x, int y, int width, int height, Color bgColor, Color fgColor) {
        button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        return button;
    }
}
