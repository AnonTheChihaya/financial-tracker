package org.segroup50.financialtracker.data.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BudgetPlanTest {

    private BudgetPlan budgetPlan;
    private LocalDate testStartDate;
    private LocalDate testEndDate;

    @BeforeEach
    void setUp() {
        testStartDate = LocalDate.of(2023, 1, 1);
        testEndDate = LocalDate.of(2023, 1, 31);
        budgetPlan = new BudgetPlan("bp123", "user456", 1000.0, 300.0,
                "Monthly", testStartDate, testEndDate, "Groceries");
    }

    @Test
    void testGettersAndSetters() {
        // Test getters with initial values
        assertEquals("bp123", budgetPlan.getId());
        assertEquals("user456", budgetPlan.getUserId());
        assertEquals(1000.0, budgetPlan.getBudgetAmount(), 0.001);
        assertEquals(300.0, budgetPlan.getSpentAmount(), 0.001);
        assertEquals("Monthly", budgetPlan.getPeriod());
        assertEquals(testStartDate, budgetPlan.getStartDate());
        assertEquals(testEndDate, budgetPlan.getEndDate());
        assertEquals("Groceries", budgetPlan.getCategory());

        // Test setters
        LocalDate newStartDate = LocalDate.of(2023, 2, 1);
        LocalDate newEndDate = LocalDate.of(2023, 2, 28);

        budgetPlan.setId("bp456");
        budgetPlan.setUserId("user789");
        budgetPlan.setBudgetAmount(2000.0);
        budgetPlan.setSpentAmount(500.0);
        budgetPlan.setPeriod("Weekly");
        budgetPlan.setStartDate(newStartDate);
        budgetPlan.setEndDate(newEndDate);
        budgetPlan.setCategory("Entertainment");

        assertEquals("bp456", budgetPlan.getId());
        assertEquals("user789", budgetPlan.getUserId());
        assertEquals(2000.0, budgetPlan.getBudgetAmount(), 0.001);
        assertEquals(500.0, budgetPlan.getSpentAmount(), 0.001);
        assertEquals("Weekly", budgetPlan.getPeriod());
        assertEquals(newStartDate, budgetPlan.getStartDate());
        assertEquals(newEndDate, budgetPlan.getEndDate());
        assertEquals("Entertainment", budgetPlan.getCategory());
    }

    @Test
    void testNoArgsConstructor() {
        BudgetPlan emptyBudgetPlan = new BudgetPlan();
        assertNull(emptyBudgetPlan.getId());
        assertNull(emptyBudgetPlan.getUserId());
        assertEquals(0.0, emptyBudgetPlan.getBudgetAmount(), 0.001);
        assertEquals(0.0, emptyBudgetPlan.getSpentAmount(), 0.001);
        assertNull(emptyBudgetPlan.getPeriod());
        assertNull(emptyBudgetPlan.getStartDate());
        assertNull(emptyBudgetPlan.getEndDate());
        assertNull(emptyBudgetPlan.getCategory());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate start = LocalDate.of(2023, 3, 1);
        LocalDate end = LocalDate.of(2023, 3, 31);

        BudgetPlan newBudgetPlan = new BudgetPlan("bp789", "user123", 1500.0, 750.0,
                "Monthly", start, end, "Transportation");

        assertEquals("bp789", newBudgetPlan.getId());
        assertEquals("user123", newBudgetPlan.getUserId());
        assertEquals(1500.0, newBudgetPlan.getBudgetAmount(), 0.001);
        assertEquals(750.0, newBudgetPlan.getSpentAmount(), 0.001);
        assertEquals("Monthly", newBudgetPlan.getPeriod());
        assertEquals(start, newBudgetPlan.getStartDate());
        assertEquals(end, newBudgetPlan.getEndDate());
        assertEquals("Transportation", newBudgetPlan.getCategory());
    }

    @Test
    void testFormattedDates() {
        assertEquals("2023-01-01", budgetPlan.getFormattedStartDate());
        assertEquals("2023-01-31", budgetPlan.getFormattedEndDate());
    }

    @Test
    void testProgressPercentage() {
        assertEquals(30, budgetPlan.getProgressPercentage());

        // Test with zero budget
        budgetPlan.setBudgetAmount(0);
        assertEquals(0, budgetPlan.getProgressPercentage());
    }

    @Test
    void testRemainingAmount() {
        assertEquals(700.0, budgetPlan.getRemainingAmount(), 0.001);

        // Test with spent exceeding budget
        budgetPlan.setSpentAmount(1200.0);
        assertEquals(-200.0, budgetPlan.getRemainingAmount(), 0.001);
    }

    @Test
    void testToString() {
        String expectedString = "BudgetPlan{id='bp123', userId='user456', budgetAmount=1000.0, " +
                "spentAmount=300.0, period='Monthly', startDate=2023-01-01, " +
                "endDate=2023-01-31, category='Groceries'}";
        assertEquals(expectedString, budgetPlan.toString());
    }
}
