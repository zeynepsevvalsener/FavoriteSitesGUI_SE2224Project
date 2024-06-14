import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class DisplayFoodCountriesPanel extends JPanel {
    public DisplayFoodCountriesPanel() {
        setLayout(new BorderLayout());

        JButton displayButton = new JButton("Display Food Countries");
        JTextArea resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);

        displayButton.addActionListener(new DisplayFoodCountriesAction(resultArea));

        add(displayButton, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    private class DisplayFoodCountriesAction implements ActionListener {
        private JTextArea resultArea;

        public DisplayFoodCountriesAction(JTextArea resultArea) {
            this.resultArea = resultArea;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> countries = new ArrayList<>();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("SELECT countryname, rating FROM visits WHERE bestfeature LIKE ? ORDER BY rating DESC")) {
                stmt.setString(1, "%food%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    countries.add(rs.getString("countryname") + " (Rating: " + rs.getInt("rating") + ")");
                }
                if (countries.isEmpty()) {
                    resultArea.setText("No countries found with best feature as food.");
                } else {
                    resultArea.setText(String.join("\n", countries));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultArea.setText("Error displaying countries: " + ex.getMessage());
            }
        }
    }
}
