import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class DisplayVisitsByYearPanel extends JPanel {
    private JTextField yearField;

    public DisplayVisitsByYearPanel() {
        setLayout(new GridLayout(2, 2));

        yearField = new JTextField(15);
        JButton displayButton = new JButton("Display Visits by Year");
        displayButton.addActionListener(new DisplayVisitsByYearAction());

        add(new JLabel("Year:"));
        add(yearField);
        add(displayButton);
    }

    private class DisplayVisitsByYearAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int year = Integer.parseInt(yearField.getText());
            ArrayList<String> visits = new ArrayList<>();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM visits WHERE yearvisited = ?")) {
                stmt.setInt(1, year);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    visits.add(rs.getString("countryname") + ", " + rs.getString("cityname") + ", " +
                            rs.getInt("yearvisited") + ", " + rs.getString("seasonvisited") + ", " +
                            rs.getString("bestfeature") + ", " + rs.getString("comments") + ", Rating: " + rs.getInt("rating"));
                }
                if (visits.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No visits found for the year: " + year);
                } else {
                    JOptionPane.showMessageDialog(null, String.join("\n", visits));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying visits: " + ex.getMessage());
            }
        }
    }
}
