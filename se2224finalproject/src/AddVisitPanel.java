import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class AddVisitPanel extends JPanel {
    private JTextField countryField, cityField, yearField, seasonField, featureField, commentsField, ratingField;
    private String username;

    public AddVisitPanel(String username) {
        this.username = username;

        setLayout(new GridLayout(8, 2));

        countryField = new JTextField(15);
        cityField = new JTextField(15);
        yearField = new JTextField(15);
        seasonField = new JTextField(15);
        featureField = new JTextField(15);
        commentsField = new JTextField(15);
        ratingField = new JTextField(15);

        JButton addButton = new JButton("Add Visit");
        addButton.addActionListener(new AddVisitAction());

        add(new JLabel("Country Name:"));
        add(countryField);
        add(new JLabel("City Name:"));
        add(cityField);
        add(new JLabel("Year Visited:"));
        add(yearField);
        add(new JLabel("Season Visited:"));
        add(seasonField);
        add(new JLabel("Best Feature:"));
        add(featureField);
        add(new JLabel("Comments:"));
        add(commentsField);
        add(new JLabel("Rating (1-5):"));
        add(ratingField);
        add(addButton);
    }

    private class AddVisitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String country = countryField.getText();
            String city = cityField.getText();
            int year = Integer.parseInt(yearField.getText());
            String season = seasonField.getText();
            String feature = featureField.getText();
            String comments = commentsField.getText();
            int rating;

            try {
                rating = Integer.parseInt(ratingField.getText());
                if (rating < 1 || rating > 5) {
                    throw new NumberFormatException("Rating must be between 1 and 5.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid rating. Please enter a number between 1 and 5.");
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(
                         "INSERT INTO visits (username, countryname, cityname, yearvisited, seasonvisited, bestfeature, comments, rating) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, username);
                stmt.setString(2, country);
                stmt.setString(3, city);
                stmt.setInt(4, year);
                stmt.setString(5, season);
                stmt.setString(6, feature);
                stmt.setString(7, comments);
                stmt.setInt(8, rating);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Visit added successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error adding visit: " + ex.getMessage());
            }
        }
    }
}
