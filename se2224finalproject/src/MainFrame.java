import javax.swing.*;
import java.awt.*;

class MainFrame extends JFrame {
    private String username;

    public MainFrame(String username) {
        this.username = username;
        setTitle("Favorite Sites");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Visit", new AddVisitPanel(username));
        tabbedPane.addTab("Delete Visit", new DeleteVisitPanel());
        tabbedPane.addTab("Display/Edit/Update Visit", new EditVisitPanel());
        tabbedPane.addTab("Display Food Countries", new DisplayFoodCountriesPanel());
        tabbedPane.addTab("Display Location Image", new DisplayLocationImagePanel());
        tabbedPane.addTab("Display Visits by Year", new DisplayVisitsByYearPanel());
        tabbedPane.addTab("Most Visited Countries", new DisplayMostVisitedCountriesPanel());
        tabbedPane.addTab("Spring Visited Countries", new DisplaySpringVisitedCountriesPanel());
        tabbedPane.addTab("Share Visit", new ShareVisitPanel(username));
        tabbedPane.addTab("Shared Visits", new DisplaySharedVisitsPanel(username));

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame("username").setVisible(true));
    }
}
