package org.segroup50.financialtracker.data.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class SavingsGoalTest {

    private SavingsGoal savingsGoal;

    @BeforeEach
    void setUp() {
        savingsGoal = new SavingsGoal("sg123", "user456", 1000.0, 500.0,
                LocalDate.of(2023, 12, 31), "Vacation fund");
    }

    @Test
    void testGettersAndSetters() {
        // Test getters with initial values
        assertEquals("sg123", savingsGoal.getId());
        assertEquals("user456", savingsGoal.getUserId());
        assertEquals(1000.0, savingsGoal.getTargetAmount(), 0.001);
        assertEquals(500.0, savingsGoal.getCurrentAmount(), 0.001);
        assertEquals(LocalDate.of(2023, 12, 31), savingsGoal.getTargetDate());
        assertEquals("Vacation fund", savingsGoal.getDescription());

        // Test setters
        savingsGoal.setId("sg456");
        savingsGoal.setUserId("user789");
        savingsGoal.setTargetAmount(2000.0);
        savingsGoal.setCurrentAmount(1000.0);
        savingsGoal.setTargetDate(LocalDate.of(2024, 6, 30));
        savingsGoal.setDescription("New car fund");

        assertEquals("sg456", savingsGoal.getId());
        assertEquals("user789", savingsGoal.getUserId());
        assertEquals(2000.0, savingsGoal.getTargetAmount(), 0.001);
        assertEquals(1000.0, savingsGoal.getCurrentAmount(), 0.001);
        assertEquals(LocalDate.of(2024, 6, 30), savingsGoal.getTargetDate());
        assertEquals("New car fund", savingsGoal.getDescription());
    }

    @Test
    void testNoArgsConstructor() {
        SavingsGoal emptySavingsGoal = new SavingsGoal();
        assertNull(emptySavingsGoal.getId());
        assertNull(emptySavingsGoal.getUserId());
        assertEquals(0.0, emptySavingsGoal.getTargetAmount(), 0.001);
        assertEquals(0.0, emptySavingsGoal.getCurrentAmount(), 0.001);
        assertNull(emptySavingsGoal.getTargetDate());
        assertNull(emptySavingsGoal.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        SavingsGoal newSavingsGoal = new SavingsGoal("sg789", "user123", 5000.0, 2500.0,
                LocalDate.of(2025, 1, 1), "Retirement fund");

        assertEquals("sg789", newSavingsGoal.getId());
        assertEquals("user123", newSavingsGoal.getUserId());
        assertEquals(5000.0, newSavingsGoal.getTargetAmount(), 0.001);
        assertEquals(2500.0, newSavingsGoal.getCurrentAmount(), 0.001);
        assertEquals(LocalDate.of(2025, 1, 1), newSavingsGoal.getTargetDate());
        assertEquals("Retirement fund", newSavingsGoal.getDescription());
    }

    @Test
    void testGetFormattedTargetDate() {
        assertEquals("2023-12-31", savingsGoal.getFormattedTargetDate());
    }

    @Test
    void testGetProgressPercentage() {
        // Test with current amount less than target
        assertEquals(50, savingsGoal.getProgressPercentage());

        // Test with current amount equal to target
        savingsGoal.setCurrentAmount(1000.0);
        assertEquals(100, savingsGoal.getProgressPercentage());

        // Test with current amount more than target
        savingsGoal.setCurrentAmount(1500.0);
        assertEquals(150, savingsGoal.getProgressPercentage());

        // Test with zero target amount
        savingsGoal.setTargetAmount(0);
        assertEquals(0, savingsGoal.getProgressPercentage());
    }

    @Test
    void testToString() {
        String expectedString = "SavingsGoal{id='sg123', userId='user456', targetAmount=1000.0, " +
                "currentAmount=500.0, targetDate=2023-12-31, description='Vacation fund'}";
        assertEquals(expectedString, savingsGoal.toString());
    }
}
