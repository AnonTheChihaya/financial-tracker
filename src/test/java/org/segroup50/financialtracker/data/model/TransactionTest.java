package org.segroup50.financialtracker.data.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testEmptyConstructor() {
        Transaction transaction = new Transaction();
        assertNotNull(transaction);
    }

    @Test
    void testParameterizedConstructor() {
        Transaction transaction = new Transaction("123", "2023-01-01", 100.50,
                "Expense", "Food", "acc-456", "user-789", "Lunch");

        assertEquals("123", transaction.getId());
        assertEquals("2023-01-01", transaction.getDate());
        assertEquals(100.50, transaction.getAmount());
        assertEquals("Expense", transaction.getType());
        assertEquals("Food", transaction.getCategory());
        assertEquals("acc-456", transaction.getAccountId());
        assertEquals("user-789", transaction.getUserId());
        assertEquals("Lunch", transaction.getNote());
    }

    @Test
    void testGettersAndSetters() {
        Transaction transaction = new Transaction();

        transaction.setId("456");
        transaction.setDate("2023-01-02");
        transaction.setAmount(200.75);
        transaction.setType("Income");
        transaction.setCategory("Salary");
        transaction.setAccountId("acc-789");
        transaction.setUserId("user-123");
        transaction.setNote("Monthly salary");

        assertEquals("456", transaction.getId());
        assertEquals("2023-01-02", transaction.getDate());
        assertEquals(200.75, transaction.getAmount());
        assertEquals("Income", transaction.getType());
        assertEquals("Salary", transaction.getCategory());
        assertEquals("acc-789", transaction.getAccountId());
        assertEquals("user-123", transaction.getUserId());
        assertEquals("Monthly salary", transaction.getNote());
    }

    @Test
    void testToString() {
        Transaction transaction = new Transaction("789", "2023-01-03", 50.25,
                "Expense", "Transport", "acc-111", "user-222", "Bus fare");

        String expected = "Transaction{id='789', date='2023-01-03', amount=50.25, " +
                "type='Expense', category='Transport', accountId='acc-111', " +
                "userId='user-222', note='Bus fare'}";

        assertEquals(expected, transaction.toString());
    }

    @Test
    void testAmountPrecision() {
        Transaction transaction = new Transaction();
        transaction.setAmount(123.456789);

        assertEquals(123.456789, transaction.getAmount(), 0.000001);
    }
}
