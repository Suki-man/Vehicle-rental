package Dashboard;

import javax.swing.*;

import Login.llogin;
import User.UserCustomer;

import java.awt.*;
import java.awt.event.*;

public class CustomDash implements ActionListener {
    private JFrame frame;
    private UserCustomer user;

    public CustomDash(UserCustomer user) {
        this.user = user;

        frame = new JFrame("Customer Dashboard"+ user.getName());
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

    JButton viewVehiclesButton = new JButton("View Available Vehicles");
    viewVehiclesButton.setBounds(500, 260, 200, 30);
    frame.add(viewVehiclesButton);
    viewVehiclesButton.addActionListener(this);
    viewVehiclesButton.setForeground(Color.BLACK);

    JButton viewRentedVehiclesButton = new JButton("View My Rentals");
    viewRentedVehiclesButton.setBounds(500, 300, 200, 30);
    frame.add(viewRentedVehiclesButton);
    viewRentedVehiclesButton.addActionListener(this);
    viewRentedVehiclesButton.setForeground(Color.BLACK);

   
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
            if (button.getText().equals("View Available Vehicles")) {
                new ViewVehicles(user); 
                frame.dispose();
            } 
             else if (button.getText().equals("View My Rentals")) {
                new ViewRentedVehicles(user); 
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
