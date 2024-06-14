import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class DisplaySpringVisitedCountriesPanel extends JPanel {
    public DisplaySpringVisitedCountriesPanel() {
        setLayout(new GridLayout(2, 1));

        JButton displayButton = new JButton("Display Spring Visited Countries");
        displayButton.addActionListener(new DisplaySpringVisitedCountriesAction());

        add(displayButton);
    }

    private class DisplaySpringVisitedCountriesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> countries = new ArrayList<>();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT countryname FROM visits WHERE seasonvisited = 'spring'")) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    countries.add(rs.getString("countryname"));
                }
                if (countries.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No countries found visited only in spring.");
                } else {
                    JOptionPane.showMessageDialog(null, String.join("\n", countries));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying countries: " + ex.getMessage());
            }
        }
    }
}
