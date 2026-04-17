package ui;

import model.Expense;
import service.ExpenseService;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ExpenseTablePanel extends JPanel implements MainFrame.Refreshable {
    private ExpenseService expenseService;
    private JTable table;
    private DefaultTableModel tableModel;

    public ExpenseTablePanel(ExpenseService service) {
        this.expenseService = service;
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));
        setupUI();
    }

    private void setupUI() {
        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ThemeManager.getTextColor());
        add(title, BorderLayout.NORTH);

        String[] columns = {"Date", "Amount", "Category", "Note", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 4; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Custom Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 250));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Custom Cell Rendering
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(ThemeManager.getCardBackground());
        
        add(scrollPane, BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void refresh() {
        tableModel.setRowCount(0);
        List<Expense> expenses = expenseService.getAllExpenses();
        for (Expense e : expenses) {
            tableModel.addRow(new Object[]{
                e.getDate(),
                String.format("$%.2f", e.getAmount()),
                e.getCategory(),
                e.getNote(),
                "Delete"
            });
        }
        table.setBackground(ThemeManager.getCardBackground());
        table.setForeground(ThemeManager.getTextColor());
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { 
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(ThemeManager.DANGER);
            setForeground(Color.WHITE);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(ThemeManager.DANGER);
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        public Object getCellEditorValue() {
            int row = table.getEditingRow();
            if (row != -1) {
                String id = expenseService.getAllExpenses().get(row).getId();
                expenseService.deleteExpense(id);
                refresh();
            }
            return label;
        }
    }
}
