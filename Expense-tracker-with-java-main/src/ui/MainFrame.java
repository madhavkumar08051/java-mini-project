package ui;

import service.ExpenseService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ExpenseService expenseService;
    private JPanel mainContent;
    private Sidebar sidebar;

    public MainFrame() {
        this.expenseService = new ExpenseService();
        setupUI();
    }

    private void setupUI() {
        setTitle("💰 Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        
        // Remove standard window borders for a custom look if possible, 
        // but let's keep it simple and just style the content.
        
        getContentPane().setBackground(ThemeManager.getBackground());
        setLayout(new BorderLayout());

        sidebar = new Sidebar(this);
        add(sidebar, BorderLayout.WEST);

        mainContent = new JPanel(new CardLayout());
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        mainContent.add(new DashboardPanel(expenseService), "Dashboard");
        mainContent.add(new AddExpensePanel(expenseService, this), "AddExpense");
        mainContent.add(new ExpenseTablePanel(expenseService), "History");

        add(mainContent, BorderLayout.CENTER);
    }

    public void showPanel(String name) {
        CardLayout cl = (CardLayout) mainContent.getLayout();
        cl.show(mainContent, name);
        refreshCurrentPanel();
    }

    public void refreshCurrentPanel() {
        for (Component comp : mainContent.getComponents()) {
            if (comp.isVisible()) {
                if (comp instanceof Refreshable) {
                    ((Refreshable) comp).refresh();
                }
            }
        }
    }

    private void updateTheme() {
        getContentPane().setBackground(ThemeManager.getBackground());
        sidebar.updateTheme();
        refreshCurrentPanel();
        SwingUtilities.updateComponentTreeUI(this);
    }

    public interface Refreshable {
        void refresh();
    }

    private class Sidebar extends JPanel {
        private MainFrame frame;
        public Sidebar(MainFrame frame) {
            this.frame = frame;
            setPreferredSize(new Dimension(240, 0));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            updateTheme();
        }

        public void updateTheme() {
            setBackground(new Color(25, 25, 35)); // Consistent dark sidebar
            removeAll();
            
            add(Box.createVerticalStrut(40));
            
            JLabel logo = new JLabel("EXPENSELY");
            logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            logo.setForeground(Color.WHITE);
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(logo);
            
            add(Box.createVerticalStrut(40));
            
            add(createSidebarButton("📊 Dashboard", "Dashboard"));
            add(createSidebarButton("➕ Add Expense", "AddExpense"));
            add(createSidebarButton("📜 History", "History"));
            
            add(Box.createVerticalGlue());
            
            JButton themeBtn = new JButton("🌓 Toggle Theme");
            themeBtn.setBackground(new Color(60, 60, 80));
            themeBtn.setForeground(Color.WHITE);
            themeBtn.setFocusPainted(false);
            themeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            themeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            themeBtn.addActionListener(e -> {
                ThemeManager.toggleTheme();
                frame.updateTheme();
            });
            add(themeBtn);
            add(Box.createVerticalStrut(30));
            revalidate();
            repaint();
        }

        private JButton createSidebarButton(String text, String cardName) {
            JButton btn = new JButton(text);
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(35, 35, 50));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> frame.showPanel(cardName));
            
            // Basic Hover Effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(ThemeManager.PRIMARY);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(35, 35, 50));
                }
            });
            
            return btn;
        }
    }
}
