package com.nelkinda.training;

import java.util.Date;
import java.util.List;

enum ExpenseType {
    DINNER, BREAKFAST, CAR_RENTAL
}

class Expense {
    ExpenseType type;
    int amount;
}

class InvoiceData {
    Date date;
    int totalExpenses;
    int totalMealExpenses;

    public InvoiceData(Date date, int totalExpenses, int totalMealExpenses) {
        this.date = date;
        this.totalExpenses = totalExpenses;
        this.totalMealExpenses = totalMealExpenses;
    }
}

class InvoiceItem {
    String expenseName;
    String mealOverExpensesMarker;

    public InvoiceItem(String expenseName, String mealOverExpensesMarker) {
        this.expenseName = expenseName;
        this.mealOverExpensesMarker = mealOverExpensesMarker;
    }
}

public class ExpenseReport {
    public void printReport(List<Expense> expenses) {
        InvoiceData invoiceData = new InvoiceData(new Date(), getTotalExpenses(expenses), getTotalMealExpenses(expenses));

        System.out.println("Expenses " + invoiceData.date);

        for (Expense expense : expenses) {
            InvoiceItem expenseItem = new InvoiceItem(getExpenseName(expense), checkMealOverExpenses(expense));
            System.out.println(expenseItem.expenseName + "\t" + expense.amount + "\t" + expenseItem.mealOverExpensesMarker);
        }

        System.out.println("Meal expenses: " + invoiceData.totalMealExpenses);
        System.out.println("Total expenses: " + invoiceData.totalExpenses);
    }

    private static int getTotalExpenses(List<Expense> expenses) {
        int total = 0;
        for (Expense expense : expenses) {
            total += expense.amount;
        }
        return total;
    }

    private static int getTotalMealExpenses(List<Expense> expenses) {
        int mealExpenses = 0;
        for (Expense expense : expenses) {
            if (expense.type == ExpenseType.DINNER || expense.type == ExpenseType.BREAKFAST) {
                mealExpenses += expense.amount;
            }
        }
        return mealExpenses;
    }

    private static String checkMealOverExpenses(Expense expense) {
        return expense.type == ExpenseType.DINNER && expense.amount > 5000 || expense.type == ExpenseType.BREAKFAST && expense.amount > 1000 ? "X" : " ";
    }

    private static String getExpenseName(Expense expense) {
        return switch (expense.type) {
            case DINNER -> "Dinner";
            case BREAKFAST -> "Breakfast";
            case CAR_RENTAL -> "Car Rental";
        };
    }
}
