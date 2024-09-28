package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import User.*;


public class customer implements ActionListener{

    private JFrame frame;
    private JTextField nameField, emailField;
    private JPasswordField passwordField;

    public customer() {
        frame = new JFrame("Customer Registration");
        frame.setSize(1280, 720);
        frame.setLayout(null);


        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 150, 100, 30);
        frame.add(nameLabel);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(new Font("Arial", Font.BOLD,16));

        nameField = new JTextField();
        nameField.setBounds(150, 150, 200, 30);
        frame.add(nameField);

        JLabel customerlLabel = new JLabel("CUSTOMER REGISTRATION");
        customerlLabel.setBounds(100, 70, 350, 30);
        frame.add(customerlLabel);
        customerlLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        customerlLabel.setForeground(Color.BLACK);
        customerlLabel.setOpaque(false);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 200, 100, 30);
        frame.add(emailLabel);
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setFont(new Font("Arial", Font.BOLD,16));

        emailField = new JTextField();
        emailField.setBounds(150, 200, 200, 30);
        frame.add(emailField);
        
        // Add a KeyListener to check the email format while typing
        emailField.addKeyListener(new KeyAdapter() {
            
            public void keyReleased(KeyEvent e) {
                String emailText = emailField.getText();
                // Regex to match email format like example@example.example
                String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        
                if (!emailText.matches(emailPattern)) {
                    emailField.setBackground(Color.PINK); // Show error by changing background
                } else {
                    emailField.setBackground(Color.GREEN); // Reset background on valid input
                }
            }
        });

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 250, 100, 30);
        frame.add(passwordLabel);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Arial", Font.BOLD,16));

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 250, 200, 30);
        frame.add(passwordField);




        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 300, 100, 30);
        registerButton.addActionListener(this);
        frame.add(registerButton);
        registerButton.setFont(new Font("Arial", Font.BOLD,16));
        registerButton.setForeground(Color.BLACK);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(260, 300, 100, 30);
        clearButton.addActionListener(this);
        frame.add(clearButton);
        clearButton.setFont(new Font("Arial", Font.BOLD,16));
        clearButton.setForeground(Color.BLACK);


        JLabel loginLabel = new JLabel("Already Registered? Login");
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setBounds(150, 350, 200, 30);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new llogin();
            }
        });
        frame.add(loginLabel);

//
        ImageIcon background = new ImageIcon("img/5.gif");

	    JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(250,-120, 1280, 720); 
        frame.add(backgroundLabel);
//

//
        ImageIcon background1 = new ImageIcon("img/bg.png");

	    JLabel backgroundLabel1 = new JLabel(background1);
        backgroundLabel1.setBounds(0,0, 1280, 720); 
        frame.add(backgroundLabel1);
//
        



ImageIcon icon = new ImageIcon("img/icon.png");  // icon
frame.setIconImage(icon.getImage());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Register")) {
                register();
            } else if (button.getText().equals("Clear")) {
                clearFields();
            }
        }
    }

    private void register() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());


        // Check for empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields!");
            return;
        }

        // Check for existing email
        if (ifEmailExists(email)) {
            JOptionPane.showMessageDialog(frame, "User with this email already exists!");
            return;
        }

        UserCustomer newUser = new UserCustomer(name, email, password);

        try {
            FileWriter writer = new FileWriter("userCustomerdata.txt", true);
            writer.write(newUser.getName() + "," + newUser.getEmail() + "," + newUser.getPassword() +"\n");
            writer.close();

        // Create the empty file for the new user
        String filePath = newUser.getName() + "_rented_vehicles.txt";
        File rentedVehiclesFile = new File(filePath);
        rentedVehiclesFile.createNewFile(); // Creates the file without writing anything

            JOptionPane.showMessageDialog(frame, "Registration Successful!");
            frame.dispose();
            new llogin();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean ifEmailExists(String email) {
        try {
            File file = new File("userCustomerdata.txt");
            if (!file.exists()) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(email)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        }
}


