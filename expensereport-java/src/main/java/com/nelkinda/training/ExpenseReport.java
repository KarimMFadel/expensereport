package com.nelkinda.training;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

enum ExpenseType {
    DINNER("Dinner"),
    BREAKFAST("Breakfast"),
    CAR_RENTAL("Car Rental");

    private final String name;

    ExpenseType(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Expense {
    ExpenseType type;
    int amount;

    boolean isOverLimit() {
        return (type == ExpenseType.DINNER && amount > ExpenseReport.DINNER_EXPENSE_LIMIT)
                || (type == ExpenseType.BREAKFAST && amount > ExpenseReport.BREAKFAST_EXPENSE_LIMIT);
    }

    boolean isMeal() {
        return type == ExpenseType.DINNER || type == ExpenseType.BREAKFAST;
    }
}

class InvoiceData {
    Date date;
    int totalExpenses;
    int totalMealExpenses;
    List<InvoiceItem> invoiceItems;

    public InvoiceData(Date date, int totalExpenses, int totalMealExpenses, List<InvoiceItem> invoiceItems) {
        this.date = date;
        this.totalExpenses = totalExpenses;
        this.totalMealExpenses = totalMealExpenses;
        this.invoiceItems = invoiceItems;
    }
}

class InvoiceItem {
    String expenseName;
    int amount;
    String mealOverExpenseMarker;

    public InvoiceItem(String expenseName, int amount, String mealOverExpenseMarker) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.mealOverExpenseMarker = mealOverExpenseMarker;
    }
}

public class ExpenseReport {

    public static final int DINNER_EXPENSE_LIMIT = 5000;
    public static final int BREAKFAST_EXPENSE_LIMIT = 1000;

    public void printReport(List<Expense> expenses) {
        InvoiceData invoiceData = getInvoiceData(expenses);

        presentInvoiceText(invoiceData);
    }

    private InvoiceData getInvoiceData(List<Expense> expenses) {
        int total = 0;
        int mealExpenses = 0;
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (Expense expense : expenses) {
            total += expense.amount;
            if (expense.isMeal()) {
                mealExpenses += expense.amount;
            }
            invoiceItems.add(new InvoiceItem(
                    expense.type.getName(),
                    expense.amount,
                    expense.isOverLimit() ? "X" : " "));
        }
        return new InvoiceData(new Date(),
                total,
                mealExpenses,
                invoiceItems);
    }

    private void presentInvoiceText(InvoiceData invoiceData) {
        System.out.println("Expenses " + invoiceData.date);

        for (InvoiceItem expenseItem : invoiceData.invoiceItems) {
            System.out.println(expenseItem.expenseName + "\t" + expenseItem.amount + "\t" + expenseItem.mealOverExpenseMarker);
        }

        System.out.println("Meal expenses: " + invoiceData.totalMealExpenses);
        System.out.println("Total expenses: " + invoiceData.totalExpenses);
    }

}
