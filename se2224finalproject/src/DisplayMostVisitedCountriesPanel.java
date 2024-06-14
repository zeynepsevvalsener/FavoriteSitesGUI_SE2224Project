import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class DisplayMostVisitedCountriesPanel extends JPanel {
    public DisplayMostVisitedCountriesPanel() {
        setLayout(new BorderLayout());

        JButton displayButton = new JButton("Display Most Visited Countries");
        JTextArea resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);

        displayButton.addActionListener(new DisplayMostVisitedCountriesAction(resultArea));

        add(displayButton, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    private class DisplayMostVisitedCountriesAction implements ActionListener {
        private JTextArea resultArea;

        public DisplayMostVisitedCountriesAction(JTextArea resultArea) {
            this.resultArea = resultArea;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> countries = new ArrayList<>();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement maxCountStmt = connection.prepareStatement("SELECT MAX(visit_count) FROM (SELECT COUNT(*) as visit_count FROM visits GROUP BY countryname) as counts");
                 PreparedStatement countriesStmt = connection.prepareStatement("SELECT countryname FROM visits GROUP BY countryname HAVING COUNT(*) = ?")) {

                ResultSet maxCountRs = maxCountStmt.executeQuery();
                if (maxCountRs.next()) {
                    int maxCount = maxCountRs.getInt(1);

                    countriesStmt.setInt(1, maxCount);
                    ResultSet countriesRs = countriesStmt.executeQuery();
                    while (countriesRs.next()) {
                        countries.add(countriesRs.getString("countryname"));
                    }

                    if (countries.isEmpty()) {
                        resultArea.setText("No countries found.");
                    } else {
                        resultArea.setText("Most visited country(s):\n" + String.join("\n", countries));
                    }
                } else {
                    resultArea.setText("No visit data found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultArea.setText("Error displaying countries: " + ex.getMessage());
            }
        }
    }
}
