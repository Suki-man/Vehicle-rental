package Login;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontFormatException;

public class llogin extends frameDesign{
    private JFrame firstpage;
	private JButton customer,owner,reg;
	private JLabel l,l1;


	public llogin()
	{
        firstpage = base_frame(firstpage, "Rental Vehicle", 1280, 720, null);

        // Assign buttons correctly
        customer = button_design(customer, "Customer Login", 720, 550, 200, 40, Color.ORANGE, Color.BLACK);
        reg = button_design(reg, "Register", 535, 630, 200, 40, Color.WHITE, Color.BLACK);
        owner = button_design(owner, "Owner Login", 350, 550, 200, 40, Color.CYAN, Color.BLACK);

        // Add action listeners to the buttons
        customer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firstpage.dispose();
                new LogCustomer();
            }
        });
    
    reg.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            firstpage.dispose();
            new Registration();
        }
    });
    
    owner.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            firstpage.dispose();
            new LogOwner();
        }
    });



		l1 = new JLabel("Don't have an account yet ?");
		l = new JLabel("ভাড়া হবে!!");

        try {
            Font banglaFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/Nikosh.ttf"));
            banglaFont = banglaFont.deriveFont(Font.BOLD, 55);
            l.setFont(banglaFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        l.setForeground(Color.WHITE);

        l1.setFont(new Font("Arial", Font.BOLD,13));
        l1.setForeground(Color.WHITE);


		l.setBounds(550, 480, 600, 50);
		l1.setBounds(540, 590, 600, 50);


		l.setOpaque(false);
		l1.setOpaque(false);


        ImageIcon background = new ImageIcon("img/gif.gif");

		JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0,-150, 1280, 720); 
        firstpage.add(backgroundLabel);

    
        backgroundLabel.add(owner);
        backgroundLabel.add(customer);
        backgroundLabel.add(reg);
        backgroundLabel.add(l);
        backgroundLabel.add(l1);


		firstpage.setVisible(true);
        firstpage.setLocationRelativeTo(null);  
	

		firstpage.getContentPane().setBackground(Color.BLACK);

        ImageIcon icon = new ImageIcon("img/icon.png");  // icon
        firstpage.setIconImage(icon.getImage());


}

}
