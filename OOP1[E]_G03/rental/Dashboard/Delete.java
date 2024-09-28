package Dashboard;

import User.UserOwner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.awt.Color;

public class Delete implements ActionListener {

    private static final String FILE_PATH = "vehicles.txt";
    private String filePath;
    private JFrame frame;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private UserOwner user; // Store the user context

    public Delete(UserOwner user) {
        this.user = user; // Initialize the user
        filePath = user.getName() + "_vehicles.txt";
        frame = new JFrame("Take Back Vehicle of " + user.getName());
        frame.setSize(1280, 720);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        String[] columnNames = {"Type", "Model", "Image", "Rent Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        vehicleTable = new JTable(tableModel);
        vehicleTable.setRowHeight(100);

        // Set ImageRenderer for the image column (column index 2)
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBounds(400, 50, 800, 600);
        frame.add(scrollPane);

        JButton delButton = new JButton("Take Selected Vehicle Back");
        delButton.setBounds(100, 300, 200, 30);
        frame.add(delButton);
        delButton.addActionListener(this);
        delButton.setOpaque(false);
        delButton.setForeground(Color.BLACK);

        JButton back = new JButton("Click here to go back");
        back.setBounds(100, 350, 200, 30);
        frame.add(back);
        back.addActionListener(this);
        back.setForeground(Color.BLACK);

        loadVehicles(); // Load existing vehicles into the table

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("img/icon.png");  // icon
        frame.setIconImage(icon.getImage());

/* 
    ImageIcon background1 = new ImageIcon("img/del.gif");

    JLabel backgroundLabel1 = new JLabel(background1);
    backgroundLabel1.setBounds(-420,-110, 1280, 600); 
    frame.add(backgroundLabel1);
*/

//
ImageIcon background4 = new ImageIcon("img/bg5.png");

JLabel backgroundLabel4 = new JLabel(background4);
backgroundLabel4.setBounds(0,0, 1280, 720); 
frame.add(backgroundLabel4);
//

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Take Selected Vehicle Back")) {
                deleteSelectedVehicle();
            } else if (button.getText().equals("Click here to go back")) {
                new OwnerDash(user); // Navigate back
                frame.dispose();
            }
        }
    }

    public void loadVehicles() {
        // Call the method to update rent status from the common file before loading vehicles
        updateRentStatusFromCommonFile(); 
        
        tableModel.setRowCount(0); // Clear existing rows
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split(",");
                if (vehicleData.length == 4) {
                    // Load the image as an ImageIcon
                    ImageIcon imageIcon = new ImageIcon(vehicleData[2]);
                    tableModel.addRow(new Object[]{vehicleData[0], vehicleData[1], imageIcon, vehicleData[3]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading vehicles: " + e.getMessage());
        }
    }

    public void deleteSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow != -1) {
            String type = (String) tableModel.getValueAt(selectedRow, 0);
            String model = (String) tableModel.getValueAt(selectedRow, 1);
            String imagePath = ((ImageIcon) tableModel.getValueAt(selectedRow, 2)).getDescription();
            String rentStatus = (String) tableModel.getValueAt(selectedRow, 3); // Get rent status directly from the table
    
            if (rentStatus != null) {
                if (rentStatus.equalsIgnoreCase("Unavailable")) {
                    JOptionPane.showMessageDialog(null, "Cannot take a vehicle back that is Unavailable.");
                    return;
                }
    
                tableModel.removeRow(selectedRow); // Remove the row from the table
                saveVehiclesToFile(); // Save the updated list to the user-specific file
                removeFromCommonFile(type, model, imagePath); // Remove from the common file
    
                JOptionPane.showMessageDialog(null, "Vehicle deleted successfully for " + user.getName() + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Selected vehicle details are incomplete or not found.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a vehicle to take back.");
        }
    }

    public void updateRentStatusFromCommonFile() {
        try (BufferedReader userFileReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter tempWriter = new BufferedWriter(new FileWriter("user_vehicles_temp.txt"))) {

            String userLine;
            while ((userLine = userFileReader.readLine()) != null) {
                String[] userVehicleData = userLine.split(",");
                if (userVehicleData.length == 4) {
                    String type = userVehicleData[0];
                    String model = userVehicleData[1];
                    String imagePath = userVehicleData[2];
                    String rentStatus = userVehicleData[3];

                    // Update rent status from common file
                    try (BufferedReader commonFileReader = new BufferedReader(new FileReader(FILE_PATH))) {
                        String commonLine;
                        while ((commonLine = commonFileReader.readLine()) != null) {
                            String[] commonVehicleData = commonLine.split(",");
                            if (commonVehicleData.length == 4 &&
                                type.equals(commonVehicleData[0]) &&
                                model.equals(commonVehicleData[1]) &&
                                imagePath.equals(commonVehicleData[2])) {
                                rentStatus = commonVehicleData[3]; // Update rent status
                                break;
                            }
                        }
                    }

                    // Write updated vehicle data to temp file
                    tempWriter.write(type + "," + model + "," + imagePath + "," + rentStatus);
                    tempWriter.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating rent status in user file: " + e.getMessage());
        }

        // Replace original user file with the updated temp file
        File originalFile = new File(filePath);
        File tempFile = new File("user_vehicles_temp.txt");
        if (originalFile.delete()) {
            tempFile.renameTo(originalFile);
        } else {
            JOptionPane.showMessageDialog(null, "Error deleting the original file.");
        }
    }

    public void saveVehiclesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String type = (String) tableModel.getValueAt(i, 0);
                String model = (String) tableModel.getValueAt(i, 1);
                String imagePath = ((ImageIcon) tableModel.getValueAt(i, 2)).getDescription();
                String rent = (String) tableModel.getValueAt(i, 3);
                writer.write(type + "," + model + "," + imagePath + "," + rent);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving vehicles: " + e.getMessage());
        }
    }

    public void removeFromCommonFile(String type, String model, String imagePath) {
        File originalFile = new File(FILE_PATH);
        File tempFile = new File("vehicles_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split(",");
                String vehicleType = vehicleData[0];
                String vehicleModel = vehicleData[1];
                String vehicleImagePath = vehicleData[2];

                // Only write the line to the temp file if it doesn't match the vehicle being deleted
                if (!(vehicleType.equals(type) && vehicleModel.equals(model) && vehicleImagePath.equals(imagePath))) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating common file: " + e.getMessage());
        }

        // Delete the original file and rename the temp file to the original name
        if (originalFile.delete()) {
            tempFile.renameTo(originalFile);
        } else {
            JOptionPane.showMessageDialog(null, "Error deleting the original file.");
        }
    }
}
