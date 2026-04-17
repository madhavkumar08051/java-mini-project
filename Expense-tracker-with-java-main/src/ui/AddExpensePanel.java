package ui;

import model.Expense;
import service.ExpenseService;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;

public class AddExpensePanel extends JPanel implements MainFrame.Refreshable {
    private ExpenseService expenseService;
    private MainFrame mainFrame;
    
    private JTextField amountField;
    private JComboBox<String> categoryBox;
    private JTextArea noteArea;
    private JPanel formCard;

    public AddExpensePanel(ExpenseService service, MainFrame frame) {
        this.expenseService = service;
        this.mainFrame = frame;
        setOpaque(false);
        setLayout(new BorderLayout());
        setupUI();
    }

    private void setupUI() {
        JLabel title = new JLabel("Add New Expense");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ThemeManager.getTextColor());
        add(title, BorderLayout.NORTH);

        formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.getCardBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Form Fields Styling
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = new Color(150, 150, 170);

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(createLabel("Amount ($)", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        amountField = createTextField();
        formCard.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(createLabel("Category", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        categoryBox = new JComboBox<>(new String[]{"Food", "Transport", "Rent", "Entertainment", "Utilities", "Other"});
        categoryBox.setPreferredSize(new Dimension(250, 40));
        formCard.add(categoryBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(createLabel("Note", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        noteArea = new JTextArea(4, 20);
        noteArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formCard.add(new JScrollPane(noteArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveBtn = new JButton("Save Transaction");
        saveBtn.setPreferredSize(new Dimension(200, 50));
        saveBtn.setBackground(ThemeManager.PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder());
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveExpense());
        formCard.add(saveBtn, gbc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(formCard);
        add(wrapper, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private JTextField createTextField() {
        JTextField f = new JTextField(20);
        f.setPreferredSize(new Dimension(250, 40));
        f.setBackground(new Color(240, 240, 250));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return f;
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String category = (String) categoryBox.getSelectedItem();
            String note = noteArea.getText();
            
            Expense expense = new Expense(amount, category, LocalDate.now(), note);
            expenseService.addExpense(expense);
            
            JOptionPane.showMessageDialog(this, "Expense recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            mainFrame.showPanel("Dashboard");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        amountField.setText("");
        noteArea.setText("");
        categoryBox.setSelectedIndex(0);
    }

    @Override
    public void refresh() {
        formCard.repaint();
    }
}
