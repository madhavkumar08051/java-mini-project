package model;

import java.time.LocalDate;
import java.util.UUID;

public class Expense {
    private String id;
    private double amount;
    private String category;
    private LocalDate date;
    private String note;

    public Expense(double amount, String category, LocalDate date, String note) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public Expense(String id, double amount, String category, LocalDate date, String note) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getNote() { return note; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return id + "," + amount + "," + category + "," + date + "," + note.replace(",", ";");
    }

    public static Expense fromCSV(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 5) return null;
        return new Expense(
            parts[0],
            Double.parseDouble(parts[1]),
            parts[2],
            LocalDate.parse(parts[3]),
            parts[4].replace(";", ",")
        );
    }
}
