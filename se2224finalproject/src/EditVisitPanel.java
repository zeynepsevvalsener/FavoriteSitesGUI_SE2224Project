import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class EditVisitPanel extends JPanel {
    private JTextField visitIdField, countryField, cityField, yearField, seasonField, featureField, commentsField, ratingField;

    public EditVisitPanel() {
        setLayout(new GridLayout(9, 2));

        visitIdField = new JTextField(15);
        countryField = new JTextField(15);
        cityField = new JTextField(15);
        yearField = new JTextField(15);
        seasonField = new JTextField(15);
        featureField = new JTextField(15);
        commentsField = new JTextField(15);
        ratingField = new JTextField(15);

        JButton displayButton = new JButton("Display Visit");
        displayButton.addActionListener(new DisplayVisitAction());

        JButton updateButton = new JButton("Update Visit");
        updateButton.addActionListener(new UpdateVisitAction());

        add(new JLabel("Visit ID:"));
        add(visitIdField);
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
        add(new JLabel("Rating:"));
        add(ratingField);
        add(displayButton);
        add(updateButton);
    }

    private class DisplayVisitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int visitId = Integer.parseInt(visitIdField.getText());

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM visits WHERE visitid = ?")) {
                stmt.setInt(1, visitId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    countryField.setText(rs.getString("countryname"));
                    cityField.setText(rs.getString("cityname"));
                    yearField.setText(rs.getString("yearvisited"));
                    seasonField.setText(rs.getString("seasonvisited"));
                    featureField.setText(rs.getString("bestfeature"));
                    commentsField.setText(rs.getString("comments"));
                    ratingField.setText(rs.getString("rating"));
                } else {
                    JOptionPane.showMessageDialog(null, "Visit ID not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying visit: " + ex.getMessage());
            }
        }
    }

    private class UpdateVisitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int visitId = Integer.parseInt(visitIdField.getText());
            String country = countryField.getText();
            String city = cityField.getText();
            int year = Integer.parseInt(yearField.getText());
            String season = seasonField.getText();
            String feature = featureField.getText();
            String comments = commentsField.getText();
            int rating = Integer.parseInt(ratingField.getText());

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("UPDATE visits SET countryname = ?, cityname = ?, yearvisited = ?, seasonvisited = ?, bestfeature = ?, comments = ?, rating = ? WHERE visitid = ?")) {
                stmt.setString(1, country);
                stmt.setString(2, city);
                stmt.setInt(3, year);
                stmt.setString(4, season);
                stmt.setString(5, feature);
                stmt.setString(6, comments);
                stmt.setInt(7, rating);
                stmt.setInt(8, visitId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Visit updated successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating visit: " + ex.getMessage());
            }
        }
    }
}
