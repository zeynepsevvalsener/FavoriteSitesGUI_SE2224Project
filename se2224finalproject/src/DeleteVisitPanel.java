import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

class DeleteVisitPanel extends JPanel {
    private JTextField visitIdField;
    private static final int MAX_RETRIES = 3;

    public DeleteVisitPanel() {
        setLayout(new GridLayout(2, 2));

        visitIdField = new JTextField(15);
        JButton deleteButton = new JButton("Delete Visit");
        deleteButton.addActionListener(new DeleteVisitAction());

        add(new JLabel("Visit ID:"));
        add(visitIdField);
        add(deleteButton);
    }

    private class DeleteVisitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int visitId = Integer.parseInt(visitIdField.getText());
            int attempt = 0;

            while (attempt < MAX_RETRIES) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    // Start transaction
                    connection.setAutoCommit(false);

                    try {
                        // Delete related entries in sharedvisits table
                        try (PreparedStatement deleteSharedVisitsStmt = connection.prepareStatement("DELETE FROM sharedvisits WHERE visitid = ?")) {
                            deleteSharedVisitsStmt.setInt(1, visitId);
                            deleteSharedVisitsStmt.executeUpdate();
                        }

                        // Delete the visit record
                        try (PreparedStatement deleteVisitStmt = connection.prepareStatement("DELETE FROM visits WHERE visitid = ?")) {
                            deleteVisitStmt.setInt(1, visitId);
                            int affectedRows = deleteVisitStmt.executeUpdate();
                            if (affectedRows > 0) {
                                // Reset visitid values to be sequential
                                try (Statement resetStmt = connection.createStatement()) {
                                    resetStmt.executeUpdate("SET @count = 0;");
                                    resetStmt.executeUpdate("UPDATE visits SET visitid = @count:= @count + 1;");
                                    resetStmt.executeUpdate("ALTER TABLE visits AUTO_INCREMENT = 1;");
                                }
                                connection.commit();
                                JOptionPane.showMessageDialog(null, "Visit deleted and IDs reset successfully!");
                            } else {
                                connection.rollback();
                                JOptionPane.showMessageDialog(null, "Visit ID not found.");
                            }
                        }
                        break; // Exit loop if successful
                    } catch (SQLException ex) {
                        connection.rollback();
                        if (ex instanceof com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException) {
                            attempt++;
                            if (attempt >= MAX_RETRIES) {
                                JOptionPane.showMessageDialog(null, "Error deleting visit: " + ex.getMessage());
                            }
                        } else {
                            throw ex;
                        }
                    } finally {
                        // End transaction
                        connection.setAutoCommit(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error deleting visit: " + ex.getMessage());
                }
            }
        }
    }
}
