import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class DisplayLocationImagePanel extends JPanel {
    private JTextField visitIdField;

    public DisplayLocationImagePanel() {
        setLayout(new GridLayout(2, 2));

        visitIdField = new JTextField(15);
        JButton displayButton = new JButton("Display Location Image");
        displayButton.addActionListener(new DisplayLocationImageAction());

        add(new JLabel("Visit ID:"));
        add(visitIdField);
        add(displayButton);
    }

    private class DisplayLocationImageAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int visitId;
            try {
                visitId = Integer.parseInt(visitIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid Visit ID. Please enter a valid number.");
                return;
            }

            String imagePath = "/Users/sevvalsener/IdeaProjects/se2224finalproject/src/images/location" + visitId + ".jpeg"; // Make sure these images are in the correct directory

            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = ImageIO.read(imageFile);
                    ImageIcon icon = new ImageIcon(image);
                    JLabel imageLabel = new JLabel(icon);
                    JOptionPane.showMessageDialog(null, imageLabel, "Location Image", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Image file not found for visit ID: " + visitId + "\nExpected path: " + imageFile.getAbsolutePath());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying image: " + ex.getMessage());
            }
        }
    }
}
