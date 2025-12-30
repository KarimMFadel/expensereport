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

    public InvoiceData(Date date) {
        this.date = date;
    }
}

public class ExpenseReport {
    public void printReport(List<Expense> expenses) {
        InvoiceData invoiceData = new InvoiceData(new Date());

        System.out.println("Expenses " + invoiceData.date);

        for (Expense expense : expenses) {
            System.out.println(getExpenseName(expense) + "\t" + expense.amount + "\t" + checkMealOverExpenses(expense));
        }

        System.out.println("Meal expenses: " + getTotalMealExpenses(expenses));
        System.out.println("Total expenses: " + getTotalExpenses(expenses));
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
