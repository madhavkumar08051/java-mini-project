package ui;

import service.ExpenseService;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DashboardPanel extends JPanel implements MainFrame.Refreshable {
    private ExpenseService expenseService;
    private JLabel totalLabel;
    private JPanel cardsPanel;

    public DashboardPanel(ExpenseService service) {
        this.expenseService = service;
        setOpaque(false);
        setLayout(new BorderLayout(0, 30));
        setupUI();
    }

    private void setupUI() {
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ThemeManager.getTextColor());
        add(title, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        cardsPanel.setOpaque(false);
        
        refresh();
        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color accent) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCardBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2.setColor(accent);
                g2.fillRect(0, 0, 8, getHeight());
                g2.dispose();
            }
        } .getClass().cast(new JPanel() {{
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            setOpaque(false);

            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            titleLbl.setForeground(new Color(150, 150, 170));
            
            JLabel valueLbl = new JLabel(value);
            valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
            valueLbl.setForeground(ThemeManager.getTextColor());
            if (title.contains("Total Spent")) totalLabel = valueLbl;

            add(titleLbl);
            add(Box.createVerticalStrut(10));
            add(valueLbl);
        }});
    }
    
    // Helper to create the card since anonymous inner class double brace was messy
    private JPanel makeCard(String title, String value, Color accent) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCardBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 10, getHeight(), 20, 20); // Accent bar
                g2.fillRect(5, 0, 5, getHeight()); // Flatten the right side of accent
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(new Color(150, 150, 170));
        
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLbl.setForeground(ThemeManager.getTextColor());
        if (title.contains("Total Spent")) totalLabel = valueLbl;

        card.add(titleLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLbl);
        return card;
    }

    @Override
    public void refresh() {
        cardsPanel.removeAll();
        cardsPanel.add(makeCard("Total Spent", String.format("$%.2f", expenseService.getTotalSpending()), ThemeManager.PRIMARY));
        cardsPanel.add(makeCard("Active Budget", "$2,500.00", ThemeManager.SUCCESS));
        cardsPanel.add(makeCard("Transactions", String.valueOf(expenseService.getAllExpenses().size()), ThemeManager.DANGER));
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}
