import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class DisplaySharedVisitsPanel extends JPanel {
    private String username;

    public DisplaySharedVisitsPanel(String username) {
        this.username = username;

        setLayout(new GridLayout(2, 1));

        JButton displayButton = new JButton("Display Shared Visits");
        displayButton.addActionListener(new DisplaySharedVisitsAction());

        add(displayButton);
    }

    private class DisplaySharedVisitsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> sharedVisits = new ArrayList<>();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(
                         "SELECT v.countryname, v.cityname, v.seasonvisited, v.bestfeature " +
                                 "FROM sharedvisits s JOIN visits v ON s.visitid = v.visitid " +
                                 "WHERE s.friend_username = ?")) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    sharedVisits.add(rs.getString("countryname") + ", " + rs.getString("cityname") + ", " +
                            rs.getString("seasonvisited") + ", " + rs.getString("bestfeature"));
                }
                if (sharedVisits.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No shared visits found.");
                } else {
                    JOptionPane.showMessageDialog(null, String.join("\n", sharedVisits));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying shared visits: " + ex.getMessage());
            }
        }
    }
}
