package Dashboard;

import javax.swing.*;

import Login.llogin;
import User.UserOwner;

import java.awt.*;
import java.awt.event.*;

public class OwnerDash implements ActionListener {
    private JFrame frame;
    private UserOwner user;

    public OwnerDash(UserOwner user) {
        this.user = user;

        frame = new JFrame("Owner Dashboard"+ user.getName());
    frame.setSize(1280, 720);
    frame.setLayout(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);

    JLabel titleLabel = new JLabel("Welcome "+ user.getName());
    titleLabel.setBounds(520, 200, 300, 30);
    frame.add(titleLabel);
    titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
    titleLabel.setForeground(Color.BLACK);
    titleLabel.setOpaque(false);

    JButton viewVehiclesButton = new JButton("Add Vehicles for Rent");
    viewVehiclesButton.setBounds(500, 260, 200, 30);
    frame.add(viewVehiclesButton);
    viewVehiclesButton.addActionListener(this);
    viewVehiclesButton.setForeground(Color.BLACK);


    JButton deleteVehiclesButton = new JButton("Take Vehicles Back");
    deleteVehiclesButton.setBounds(500, 300, 200, 30);
    frame.add(deleteVehiclesButton);
    deleteVehiclesButton.addActionListener(this);
    deleteVehiclesButton.setForeground(Color.BLACK);

   
    JButton logoutButton = new JButton("Logout");
    logoutButton.setBounds(500, 340, 200, 30);
    frame.add(logoutButton);
    logoutButton.addActionListener(this);
    logoutButton.setForeground(Color.BLACK);

    //
    ImageIcon background = new ImageIcon("img/bgdash.png");

    JLabel backgroundLabel = new JLabel(background);
    backgroundLabel.setBounds(0,0, 1280, 720); 
    frame.add(backgroundLabel);
//

    ImageIcon icon = new ImageIcon("img/icon.png");  // icon
    frame.setIconImage(icon.getImage());

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Add Vehicles for Rent")) {
                new Add(user); 
                frame.dispose();
            } 
             else if (button.getText().equals("Take Vehicles Back")) {
                new Delete(user); 
                frame.dispose();
            }
             else if (button.getText().equals("Logout")) {
                JOptionPane.showMessageDialog(null, "Logging out!");
                frame.dispose();
                new llogin(); 
            }
        }
    }
}
