package util;

import model.Expense;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_PATH = "data/expenses.csv";

    public static void saveExpenses(List<Expense> expenses) {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Expense expense : expenses) {
                writer.println(expense.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Expense expense = Expense.fromCSV(line);
                if (expense != null) {
                    expenses.add(expense);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expenses;
    }
}
