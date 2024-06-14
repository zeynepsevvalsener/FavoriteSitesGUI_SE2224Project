import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class ShareVisitPanel extends JPanel {
    private JTextField visitIdField, friendUsernameField;
    private String username;

    public ShareVisitPanel(String username) {
        this.username = username;

        setLayout(new GridLayout(3, 2));

        visitIdField = new JTextField(15);
        friendUsernameField = new JTextField(15);
        JButton shareButton = new JButton("Share Visit");
        shareButton.addActionListener(new ShareVisitAction());

        add(new JLabel("Visit ID:"));
        add(visitIdField);
        add(new JLabel("Friend's Username:"));
        add(friendUsernameField);
        add(shareButton);
    }

    private class ShareVisitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int visitId = Integer.parseInt(visitIdField.getText());
            String friendUsername = friendUsernameField.getText();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("INSERT INTO sharedvisits (username, friend_username, visitid) VALUES (?, ?, ?)")) {
                stmt.setString(1, username);
                stmt.setString(2, friendUsername);
                stmt.setInt(3, visitId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Visit shared successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error sharing visit: " + ex.getMessage());
            }
        }
    }
}
