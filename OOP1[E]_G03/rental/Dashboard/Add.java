package Dashboard;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import User.UserOwner;

public class Add {

    private static final String FILE_PATH = "vehicles.txt"; // Common file path
    private String filePath; // User-specific file path

    private JFrame frame;
    private JTextField typeField, modelField;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private String selectedImagePath;
    String rent = "Available";
    
    public Add(UserOwner user) {
        // Initialize user-specific file path
        filePath = user.getName() + "_vehicles.txt";
        
        frame = new JFrame("Add Vehicle for " + user.getName());
        frame.setSize(1280, 720);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
       
        JLabel type = new JLabel("Type:");
        type.setBounds(50, 150, 100, 30);
        frame.add(type);
        type.setFont(new Font("Arial", Font.BOLD, 16));
        type.setOpaque(false);

        typeField = new JTextField();
        typeField.setBounds(150, 150, 200, 30);
        frame.add(typeField);
        typeField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        typeField.setOpaque(false);

        JLabel model = new JLabel("Model:");
        model.setBounds(50, 200, 100, 30);
        frame.add(model);
        model.setFont(new Font("Arial", Font.BOLD, 16));
        model.setOpaque(false);

        modelField = new JTextField();
        modelField.setBounds(150, 200, 200, 30);
        frame.add(modelField);
        modelField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        modelField.setOpaque(false);

        JLabel imagePath = new JLabel("Image:");
        imagePath.setBounds(50, 250, 100, 30);
        frame.add(imagePath);
        imagePath.setFont(new Font("Arial", Font.BOLD, 16));
        imagePath.setOpaque(false);

        // Button to trigger image upload
        JButton uploadImageButton = new JButton("Upload Image");
        uploadImageButton.setBounds(160, 250, 200, 30);
        frame.add(uploadImageButton);
        uploadImageButton.setOpaque(false);
        uploadImageButton.setForeground(Color.BLACK);
        uploadImageButton.setBackground(null);
        uploadImageButton.setContentAreaFilled(false);
        uploadImageButton.setBorder(null);

        uploadImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadImage();  // Call the method to upload an image
            }
        });

        JButton addd = new JButton("Click here to add");
        addd.setBounds(100, 300, 200, 30);
        frame.add(addd);
        addd.setForeground(Color.BLACK);

        addd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addVehicle();
            }
        });

        JButton back = new JButton("Click here go back");
        back.setBounds(100, 350, 200, 30);
        frame.add(back);
        back.setForeground(Color.BLACK);

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new OwnerDash(user);
            }
        });

        // Create JTable with ImageIcon column
        String[] columnNames = {"Type", "Model", "Image", "Rent Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        vehicleTable = new JTable(tableModel) {
            public Class<?> getColumnClass(int column) {
                if (column == 2) {
                    return ImageIcon.class; // Image column
                } else {
                    return String.class; // Other columns
                }
            }
        };

        vehicleTable.setRowHeight(100);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBounds(400, 50, 800, 600);
        frame.add(scrollPane);

        // Set a custom cell renderer for displaying images in the table
        vehicleTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        // Load existing vehicles into the table
        loadVehicles();

        frame.setVisible(true);

        ImageIcon icon = new ImageIcon("img/icon.png");  // icon
        frame.setIconImage(icon.getImage());
        
        ImageIcon background = new ImageIcon("img/8.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(-850,-135, 1280, 720); 
        frame.add(backgroundLabel);

        ImageIcon background1 = new ImageIcon("img/8.png");
        JLabel backgroundLabel1 = new JLabel(background1);
        backgroundLabel1.setBounds(-850,-85, 1280, 720); 
        frame.add(backgroundLabel1);

        ImageIcon background2 = new ImageIcon("img/8.png");
        JLabel backgroundLabel2 = new JLabel(background2);
        backgroundLabel2.setBounds(-850,-35, 1280, 720); 
        frame.add(backgroundLabel2);

        ImageIcon background3 = new ImageIcon("img/9.png");
        JLabel backgroundLabel3 = new JLabel(background3);
        backgroundLabel3.setBounds(-850,-35, 1280, 720); 
        frame.add(backgroundLabel3);

        // Add background images
        ImageIcon background4 = new ImageIcon("img/bg5.png");
        JLabel backgroundLabel4 = new JLabel(background4);
        backgroundLabel4.setBounds(0, 0, 1280, 720); 
        frame.add(backgroundLabel4);
    }

    public void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Store the selected image path
        }
    }

    public void loadVehicles() {
        updateRentStatusFromCommonFile();
        tableModel.setRowCount(0); // Clear existing rows
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] vehicleData = line.split(",");
                if (vehicleData.length == 4) {
                    String type = vehicleData[0];
                    String model = vehicleData[1];
                    String imagePath = vehicleData[2];
                    String rentStatus = vehicleData[3];

                    // Convert image path to ImageIcon
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));

                    // Add row with ImageIcon
                    tableModel.addRow(new Object[]{type, model, imageIcon, rentStatus});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading vehicles: " + e.getMessage());
        }
    }

    public void updateRentStatusFromCommonFile() {
        try (BufferedReader commonFileReader = new BufferedReader(new FileReader(FILE_PATH));
             BufferedReader userFileReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter tempWriter = new BufferedWriter(new FileWriter("user_vehicles_temp.txt"))) {

            String line;
            while ((line = userFileReader.readLine()) != null) {
                String[] userVehicleData = line.split(",");
                if (userVehicleData.length == 4) {
                    String type = userVehicleData[0];
                    String model = userVehicleData[1];
                    String imagePath = userVehicleData[2];
                    String rentStatus = userVehicleData[3];

                    try (BufferedReader commonFileCheckReader = new BufferedReader(new FileReader(FILE_PATH))) {
                        String commonLine;
                        while ((commonLine = commonFileCheckReader.readLine()) != null) {
                            String[] commonVehicleData = commonLine.split(",");
                            if (commonVehicleData.length == 4) {
                                String commonType = commonVehicleData[0];
                                String commonModel = commonVehicleData[1];
                                String commonImagePath = commonVehicleData[2];
                                String commonRentStatus = commonVehicleData[3];

                                if (type.equals(commonType) && model.equals(commonModel) && imagePath.equals(commonImagePath)) {
                                    rentStatus = commonRentStatus; // Update rent status from the common file
                                    break;
                                }
                            }
                        }
                    }

                    tempWriter.write(type + "," + model + "," + imagePath + "," + rentStatus);
                    tempWriter.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating rent status in user file: " + e.getMessage());
        }

        // Replace original file with the updated temp file
        File originalFile = new File(filePath);
        File tempFile = new File("user_vehicles_temp.txt");
        if (originalFile.delete()) {
            tempFile.renameTo(originalFile);
        } else {
            JOptionPane.showMessageDialog(null, "Error deleting the original file.");
        }
    }

    public void addVehicle() {
        String type = typeField.getText();
        String model = modelField.getText();

        if (!type.isEmpty() && !model.isEmpty() && selectedImagePath != null) {
            saveVehicleToFile(type, model, selectedImagePath, rent);
            copyToCommonFile(type, model, selectedImagePath, rent);
            JOptionPane.showMessageDialog(null, "Vehicle added successfully!");
            clearFields();
            loadVehicles();
        } else {
            JOptionPane.showMessageDialog(null, "Please fill all fields.");
        }
    }

    public void saveVehicleToFile(String type, String model, String imagePath, String rent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(type + "," + model + "," + imagePath + "," + rent);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving vehicle: " + e.getMessage());
        }
    }

    public void copyToCommonFile(String type, String model, String imagePath, String rent) {
        try (BufferedWriter commonWriter = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            commonWriter.write(type + "," + model + "," + imagePath + "," + rent);
            commonWriter.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error copying vehicle to common file: " + e.getMessage());
        }
    }

    public void clearFields() {
        typeField.setText("");
        modelField.setText("");
        selectedImagePath = null;
    }
}
