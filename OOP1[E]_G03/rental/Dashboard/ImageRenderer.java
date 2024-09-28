package Dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ImageRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        if (value instanceof ImageIcon) {
            ImageIcon icon = (ImageIcon) value;
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // this is to resize the image to 100x100
            label.setIcon(new ImageIcon(img));
        }
        return label;
    }
}

