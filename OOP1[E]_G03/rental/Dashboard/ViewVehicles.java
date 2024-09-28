package Dashboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.io.*;
import java.awt.Color;

import User.UserCustomer;

public class ViewVehicles {

    private static final String FILE_PATH = "vehicles.txt";

    private String filePath;

    private JFrame frame;
    private JTextField typeField, modelField, imageField;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private UserCustomer user;
    
    public ViewVehicles(UserCustomer user){
        this.user = user;
        filePath = user.getName() + "_rented_vehicles.txt"; 
        
        frame = new JFrame("Available Vehicle for " + user.getName());
        frame.setSize(1280, 720);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
       
        

        JButton rentButton=new JButton("Click here to Rent");
        rentButton.setBounds(130, 300, 200, 30);
        frame.add(rentButton);
        rentButton.setForeground(Color.BLACK);

        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rentSelectedVehicle(); // Method to rent the selected vehicle
            }
        });

        JButton back=new JButton("Click here go back");
        back.setBounds(130, 350, 200, 30);
        frame.add(back);
        back.setForeground(Color.BLACK);

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new CustomDash(user);
            }
        });

          // Create JTable
        String[] columnNames = {"Type", "Model", "Image", "Rent Status"}; 
        tableModel = new DefaultTableModel(columnNames, 0);
        vehicleTable = new JTable(tableModel);
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer()); //image renderer
        vehicleTable.setRowHeight(100);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBounds(400, 50, 800, 600);
        frame.add(scrollPane);

        // Load existing vehicles into the table
        loadVehicles();

        frame.setVisible(true);

        ImageIcon icon = new ImageIcon("img/icon.png");  // icon
        frame.setIconImage(icon.getImage());

        //
        ImageIcon background4 = new ImageIcon("img/bg5.png");
        JLabel backgroundLabel4 = new JLabel(background4);
        backgroundLabel4.setBounds(0,0, 1280, 720); 
        frame.add(backgroundLabel4);
        //

    }

    public void loadVehicles() {
        tableModel.setRowCount(0); // Clear existing rows
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split(",");
                tableModel.addRow(vehicleData);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading vehicles: " + e.getMessage());
        }
        
        // Set the ImageRenderer for the image column (index 2)
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
        
        // Refresh the table to show images
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String imagePath = (String) tableModel.getValueAt(i, 2);
            // Convert the image path to an ImageIcon for rendering
            ImageIcon icon = new ImageIcon(imagePath); // Ensure the path is valid
            tableModel.setValueAt(icon, i, 2); // Set the icon in the table model
        }
    }
    
   
    public void rentSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
    
        if (selectedRow != -1) {
            String type = (String) tableModel.getValueAt(selectedRow, 0);
            String model = (String) tableModel.getValueAt(selectedRow, 1);
            ImageIcon imagePath = (ImageIcon) tableModel.getValueAt(selectedRow, 2);
            String rentStatus = (String) tableModel.getValueAt(selectedRow, 3);
    
            if (rentStatus.equals("Available") && !isVehicleAlreadyRented(type, model, imagePath.getDescription())) {
                saveRentedVehicleToFile(type, model, imagePath.getDescription(), "Rented");
                updateVehicleStatusInFile(type, model, imagePath.getDescription());
                JOptionPane.showMessageDialog(null, "Vehicle rented successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "You have already rented this vehicle or it is unavailable.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a vehicle to rent.");
        }
    }
    
    public boolean isVehicleAlreadyRented(String type, String model, String imagePath) {
        // Check the user's rented vehicles
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split(",");
                if (vehicleData[0].equals(type) && vehicleData[1].equals(model) && vehicleData[2].equals(imagePath)) {
                    return true; // User has already rented this vehicle
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error checking user's rented vehicles: " + e.getMessage());
        }
    
        // Check if the vehicle is available in the main vehicle file
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split(",");
                if (vehicleData[0].equals(type) && vehicleData[1].equals(model) && vehicleData[2].equals(imagePath) && !vehicleData[3].equals("Available")) {
                    return true; // Vehicle already rented
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error checking rented vehicles: " + e.getMessage());
        }
        return false;
    }

    public void saveRentedVehicleToFile(String type, String model, String imagePath,String rent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(type + "," + model + "," + imagePath+ "," + "rented");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Creating File for user : "+ e.getMessage());
        }
    }

    public void updateVehicleStatusInFile(String type, String model, String imagePath) {
    // Create a temporary file to store updated data
    File tempFile = new File("temp_vehicles.txt");

    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
         BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
        
        String line;
        while ((line = reader.readLine()) != null) {
            String[] vehicleData = line.split(",");
            // Check if the current line matches the rented vehicle
            if (vehicleData[0].equals(type) && vehicleData[1].equals(model) && vehicleData[2].equals(imagePath)) {
                // Update the rent status to "Unavailable"
                writer.write(type + "," + model + "," + imagePath + "," + "Unavailable");
            } else {
                // Write the original line for other vehicles
                writer.write(line);
            }
            writer.newLine();
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error updating vehicle status: " + e.getMessage());
    }

    // Delete the original file
    File originalFile = new File("vehicles.txt");
    if (originalFile.delete()) {

    // Replace the original file with the updated one
    if (tempFile.renameTo(new File("vehicles.txt"))) {
        JOptionPane.showMessageDialog(null, "File updated successfully!");
    } else {
        JOptionPane.showMessageDialog(null, "Error updating the file.");
    }
}
}

    public void clearFields() {
        typeField.setText("");
        modelField.setText("");
        imageField.setText("");
    }
}
