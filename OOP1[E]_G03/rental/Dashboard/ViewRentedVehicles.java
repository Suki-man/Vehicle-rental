package Dashboard;

import User.UserCustomer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.awt.Color;


public class ViewRentedVehicles implements ActionListener {

    private static final String FILE_PATH = "vehicles.txt";
    private String filePath;
    private JFrame frame;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private UserCustomer user; // Store the user context

    public ViewRentedVehicles(UserCustomer user) {
        this.user = user; // Initialize the user
        filePath = user.getName() + "_rented_vehicles.txt";
        frame = new JFrame("Rented Vehicle of " + user.getName());
        frame.setSize(1280, 720);
        frame.setLayout(null);
        frame.setVisible(true);

        String[] columnNames = {"Type", "Model", "Image", "Rent"}; 
        tableModel = new DefaultTableModel(columnNames, 0);
        vehicleTable = new JTable(tableModel);
        vehicleTable.setRowHeight(100);
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBounds(400, 50, 800, 600);
        frame.add(scrollPane);

        JButton returnButton = new JButton("Return Selected Vehicle");
        returnButton.setBounds(130, 300, 200, 30);
        frame.add(returnButton);
        returnButton.addActionListener(this);
        returnButton.setOpaque(false);
        returnButton.setForeground(Color.BLACK);

        JButton back = new JButton("Click here to go back");
        back.setBounds(130, 350, 200, 30);
        frame.add(back);
        back.addActionListener(this);
        back.setForeground(Color.BLACK);

        loadVehicles(); // Load existing vehicles into the table

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("img/icon.png");  // icon
        frame.setIconImage(icon.getImage());

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
            if (button.getText().equals("Return Selected Vehicle")) {
                returnSelectedVehicle();
            } else if (button.getText().equals("Click here to go back")) {
                new CustomDash(user); // Navigate back
                frame.dispose();
            }
        }
    }

    public void loadVehicles() {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        tableModel.setRowCount(0); // Clear existing rows
        while ((line = reader.readLine()) != null) {
            String[] vehicleData = line.split(",");
            if (vehicleData.length >= 4) {
                // Add the image path as a string for the table model
                tableModel.addRow(new Object[]{
                    vehicleData[0], // Type
                    vehicleData[1], // Model
                    new ImageIcon(vehicleData[2]), // Convert the image path to an ImageIcon
                    vehicleData[3]  // Rent Status
                });
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading vehicles: " + e.getMessage());
    }
}


    public void returnSelectedVehicle() {
    int selectedRow = vehicleTable.getSelectedRow();
    if (selectedRow != -1) {
        String type = (String) tableModel.getValueAt(selectedRow, 0);
        String model = (String) tableModel.getValueAt(selectedRow, 1);
        ImageIcon icon = (ImageIcon) tableModel.getValueAt(selectedRow, 2); // Get ImageIcon directly
        String rent = (String) tableModel.getValueAt(selectedRow, 3);
        
        String imagePath = icon.getDescription(); // Get the image path from the ImageIcon

        // Remove from JTable
        tableModel.removeRow(selectedRow);

        // Update user's vehicle file after deletion
        saveVehiclesToFile();

        // Update the common file to set vehicle status to Available
        updateVehicleStatus(type, model, imagePath);

        JOptionPane.showMessageDialog(null, "Vehicle returned successfully for " + user.getName() + "!");
    } else {
        JOptionPane.showMessageDialog(null, "Please select a vehicle to return.");
    }
}

    public void saveVehiclesToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String type = (String) tableModel.getValueAt(i, 0);
            String model = (String) tableModel.getValueAt(i, 1);
            ImageIcon icon = (ImageIcon) tableModel.getValueAt(i, 2);
            String imagePath = icon.getDescription(); // Get the image path
            String rent = (String) tableModel.getValueAt(i, 3);
            writer.write(type + "," + model + "," + imagePath + "," + rent);
            writer.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error saving vehicles: " + e.getMessage());
    }
}

    public void updateVehicleStatus(String type, String model, String imagePath) {
        try {
            File tempFile = new File("temp_vehicles.txt");
    
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] vehicleData = line.split(",");
                    if (vehicleData.length >= 4) {
                        if (vehicleData[0].equals(type) && vehicleData[1].equals(model) && vehicleData[2].equals(imagePath)) {
                            vehicleData[3] = "Available"; // Update the status to "Available"
                        }
                    }
                    writer.write(String.join(",", vehicleData));
                    writer.newLine();
                }
            }
    
            // Delete the original file and rename the temp file to original
            if (!new File(FILE_PATH).delete() || !tempFile.renameTo(new File(FILE_PATH))) {
                JOptionPane.showMessageDialog(null, "Error updating vehicle status: Could not update file.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating vehicle status: " + e.getMessage());
        }
    }
}    

