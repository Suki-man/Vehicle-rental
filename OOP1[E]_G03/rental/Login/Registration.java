package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Registration extends frameDesign implements ActionListener { 

    private JFrame frame;
    private JButton b1,b2,b3;

   public Registration(){

    frame = base_frame(frame, "Registration", 1280, 720, Color.WHITE);

    frame.setVisible(true);
    frame.setLocationRelativeTo(null);  

    b1 = button_design(b1, "Register as an Owner", 410, 350, 200, 40, Color.WHITE, Color.BLACK);

    b1.addActionListener(this);
    frame.add(b1);


    b2 = button_design(b2, "Register as a Customer", 670, 350, 200, 40, Color.WHITE, Color.BLACK);

    b2.addActionListener(this);
    frame.add(b2);


    b3 = button_design(b3, "Go Back", 590, 450, 100, 40, null, Color.BLACK);

    b3.addActionListener(this);
    frame.add(b3);
    b3.setContentAreaFilled(false);
    b3.setBorderPainted(false);

    ImageIcon background = new ImageIcon("img/3.gif");

	JLabel backgroundLabel = new JLabel(background);
    backgroundLabel.setBounds(0,-150, 1280, 720); 
    frame.add(backgroundLabel);
    ImageIcon icon = new ImageIcon("img/icon.png");  // icon
    frame.setIconImage(icon.getImage());


    
}
public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Register as an Owner")) {
                frame.dispose();
                new owner();
            } else if (button.getText().equals("Register as a Customer")) {
                frame.dispose();
                new customer();
            }
             else if (button.getText().equals("Go Back")) {
               frame.dispose();
                new llogin();
            }
        }
    



    }




   }
