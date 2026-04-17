package service;

import model.Expense;
import util.FileHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseService {
    private List<Expense> expenses;

    public ExpenseService() {
        this.expenses = FileHandler.loadExpenses();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        save();
    }

    public void deleteExpense(String id) {
        expenses.removeIf(e -> e.getId().equals(id));
        save();
    }

    public List<Expense> getAllExpenses() {
        return expenses;
    }

    public List<Expense> getExpensesByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public double getTotalSpending() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    private void save() {
        FileHandler.saveExpenses(expenses);
    }
}
