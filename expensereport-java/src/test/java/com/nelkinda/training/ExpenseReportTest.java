package com.nelkinda.training;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseReportTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;
    private ExpenseReport report;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        report = new ExpenseReport();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should handle all expense types and mark over-expense items with X")
    void printReport_shouldHandleAllExpenseTypesAndOverExpenseMarkers() {
        // Arrange
        Expense dinner = createExpense(ExpenseType.DINNER, 6000); // Over limit (5000)
        Expense breakfast = createExpense(ExpenseType.BREAKFAST, 500); // Under limit (1000)
        Expense breakfastOver = createExpense(ExpenseType.BREAKFAST, 1500); // Over limit (1000)
        Expense carRental = createExpense(ExpenseType.CAR_RENTAL, 10000); // No limit

        List<Expense> expenses = Arrays.asList(dinner, breakfast, breakfastOver, carRental);

        // Act
        report.printReport(expenses);

        // Assert
        String output = outputStream.toString();
        String outputWithoutFirstLine = output.substring(output.indexOf("\n") + 1);

        String expectedResult = """
            Dinner	6000	X
            Breakfast	500	\s
            Breakfast	1500	X
            Car Rental	10000	\s
            Meal expenses: 8000
            Total expenses: 18000
            """.replace("\n", System.lineSeparator());

        assertTrue(output.startsWith("Expenses "));
        assertEquals(expectedResult, outputWithoutFirstLine);
    }

    @Test
    @DisplayName("Should handle empty expense list")
    void printReport_shouldHandleEmptyExpenseList() {
        // Act
        report.printReport(Collections.emptyList());

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Meal expenses: 0"));
        assertTrue(output.contains("Total expenses: 0"));
    }

    @Test
    @DisplayName("Should not mark dinner at exactly the limit")
    void printReport_shouldNotMarkDinnerAtExactLimit() {
        // Arrange - dinner exactly at limit (5000)
        Expense dinner = createExpense(ExpenseType.DINNER, ExpenseReport.DINNER_EXPENSE_LIMIT);

        // Act
        report.printReport(List.of(dinner));

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Dinner\t5000\t "), "Dinner at exact limit should not be marked");
    }

    @Test
    @DisplayName("Should not mark breakfast at exactly the limit")
    void printReport_shouldNotMarkBreakfastAtExactLimit() {
        // Arrange - breakfast exactly at limit (1000)
        Expense breakfast = createExpense(ExpenseType.BREAKFAST, ExpenseReport.BREAKFAST_EXPENSE_LIMIT);

        // Act
        report.printReport(List.of(breakfast));

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Breakfast\t1000\t "), "Breakfast at exact limit should not be marked");
    }

    @Test
    @DisplayName("Should mark dinner one cent over the limit")
    void printReport_shouldMarkDinnerOneCentOverLimit() {
        // Arrange - dinner just over limit
        Expense dinner = createExpense(ExpenseType.DINNER, ExpenseReport.DINNER_EXPENSE_LIMIT + 1);

        // Act
        report.printReport(List.of(dinner));

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Dinner\t5001\tX"), "Dinner over limit should be marked with X");
    }

    @Test
    @DisplayName("Should mark breakfast one cent over the limit")
    void printReport_shouldMarkBreakfastOneCentOverLimit() {
        // Arrange - breakfast just over limit
        Expense breakfast = createExpense(ExpenseType.BREAKFAST, ExpenseReport.BREAKFAST_EXPENSE_LIMIT + 1);

        // Act
        report.printReport(List.of(breakfast));

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Breakfast\t1001\tX"), "Breakfast over limit should be marked with X");
    }

    @Test
    @DisplayName("Should calculate meal expenses only for dinner and breakfast")
    void printReport_shouldCalculateMealExpensesCorrectly() {
        // Arrange
        Expense dinner = createExpense(ExpenseType.DINNER, 3000);
        Expense breakfast = createExpense(ExpenseType.BREAKFAST, 500);
        Expense carRental = createExpense(ExpenseType.CAR_RENTAL, 10000);

        // Act
        report.printReport(Arrays.asList(dinner, breakfast, carRental));

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Meal expenses: 3500"), "Meal expenses should only include dinner and breakfast");
        assertTrue(output.contains("Total expenses: 13500"), "Total should include all expenses");
    }

    @Test
    @DisplayName("Should never mark car rental as over-expense regardless of amount")
    void printReport_shouldNeverMarkCarRentalAsOverExpense() {
        // Arrange - very high car rental amount
        Expense carRental = createExpense(ExpenseType.CAR_RENTAL, 1000000);

        // Act
        report.printReport(List.of(carRental));

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Car Rental\t1000000\t "), "Car rental should never be marked as over-expense");
    }

    private Expense createExpense(ExpenseType type, int amount) {
        Expense expense = new Expense();
        expense.type = type;
        expense.amount = amount;
        return expense;
    }
}
