package com.nelkinda.training;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseReportTest {

    @Test
    void printReport_shouldHandleAllExpenseTypesAndOverExpenseMarkers() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        ExpenseReport report = new ExpenseReport();

        Expense dinner = new Expense();
        dinner.type = ExpenseType.DINNER;
        dinner.amount = 6000; // Over limit (5000), should be marked with X

        Expense breakfast = new Expense();
        breakfast.type = ExpenseType.BREAKFAST;
        breakfast.amount = 500; // Under limit (1000), no marker

        Expense breakfastOver = new Expense();
        breakfastOver.type = ExpenseType.BREAKFAST;
        breakfastOver.amount = 1500; // Over limit (1000), should be marked with X

        Expense carRental = new Expense();
        carRental.type = ExpenseType.CAR_RENTAL;
        carRental.amount = 10000; // No limit for car rental

        List<Expense> expenses = Arrays.asList(dinner, breakfast, breakfastOver, carRental);

        // Act
        report.printReport(expenses);

        // Assert
        String output = outputStream.toString();

        // Skip the first line (contains dynamic date)
        String outputWithoutFirstLine = output.substring(output.indexOf("\n") + 1);


        String expectedResult = """
            Dinner	6000	X
            Breakfast	500	\s
            Breakfast	1500	X
            Car Rental	10000	\s
            Meal expenses: 8000
            Total expenses: 18000
            """.replace("\n", "\r\n");

        assertTrue(output.startsWith("Expenses ")); // Verify header exists
        assertEquals(expectedResult, outputWithoutFirstLine);
    }
}
