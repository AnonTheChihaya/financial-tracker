package org.segroup50.financialtracker.data.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("acc123", "user456", "Savings Account", "Bank", "Saving", 1000.0);
    }

    @Test
    void testGettersAndSetters() {
        // Test getters with initial values
        assertEquals("acc123", account.getId());
        assertEquals("user456", account.getUserId());
        assertEquals("Savings Account", account.getName());
        assertEquals("Bank", account.getType());
        assertEquals("Saving", account.getAttribute());
        assertEquals(1000.0, account.getBalance(), 0.001);

        // Test setters
        account.setId("acc456");
        account.setUserId("user789");
        account.setName("Investment Account");
        account.setType("Brokerage");
        account.setAttribute("Investment");
        account.setBalance(5000.0);

        assertEquals("acc456", account.getId());
        assertEquals("user789", account.getUserId());
        assertEquals("Investment Account", account.getName());
        assertEquals("Brokerage", account.getType());
        assertEquals("Investment", account.getAttribute());
        assertEquals(5000.0, account.getBalance(), 0.001);
    }

    @Test
    void testNoArgsConstructor() {
        Account emptyAccount = new Account();
        assertNull(emptyAccount.getId());
        assertNull(emptyAccount.getUserId());
        assertNull(emptyAccount.getName());
        assertNull(emptyAccount.getType());
        assertNull(emptyAccount.getAttribute());
        assertEquals(0.0, emptyAccount.getBalance(), 0.001);
    }

    @Test
    void testAllArgsConstructor() {
        Account newAccount = new Account("acc789", "user123", "Checking", "Bank", "Saving", 2500.0);

        assertEquals("acc789", newAccount.getId());
        assertEquals("user123", newAccount.getUserId());
        assertEquals("Checking", newAccount.getName());
        assertEquals("Bank", newAccount.getType());
        assertEquals("Saving", newAccount.getAttribute());
        assertEquals(2500.0, newAccount.getBalance(), 0.001);
    }

    @Test
    void testToString() {
        String expectedString = "Account{id='acc123', userId='user456', name='Savings Account', type='Bank', attribute='Saving', balance=1000.0}";
        assertEquals(expectedString, account.toString());
    }
}
