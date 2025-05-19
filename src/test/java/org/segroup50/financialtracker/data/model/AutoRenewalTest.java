package org.segroup50.financialtracker.data.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AutoRenewalTest {

    private AutoRenewal autoRenewal;

    @BeforeEach
    void setUp() {
        autoRenewal = new AutoRenewal("ar123", "user456", "Netflix Subscription", 15.99, "MONTH", "acc789", "Entertainment");
    }

    @Test
    void testGettersAndSetters() {
        // Test getters with initial values
        assertEquals("ar123", autoRenewal.getId());
        assertEquals("user456", autoRenewal.getUserId());
        assertEquals("Netflix Subscription", autoRenewal.getName());
        assertEquals(15.99, autoRenewal.getAmount(), 0.001);
        assertEquals("MONTH", autoRenewal.getPeriod());
        assertEquals("acc789", autoRenewal.getAccountId());
        assertEquals("Entertainment", autoRenewal.getNote());

        // Test setters
        autoRenewal.setId("ar456");
        autoRenewal.setUserId("user789");
        autoRenewal.setName("Spotify Subscription");
        autoRenewal.setAmount(9.99);
        autoRenewal.setPeriod("YEAR");
        autoRenewal.setAccountId("acc123");
        autoRenewal.setNote("Music service");

        assertEquals("ar456", autoRenewal.getId());
        assertEquals("user789", autoRenewal.getUserId());
        assertEquals("Spotify Subscription", autoRenewal.getName());
        assertEquals(9.99, autoRenewal.getAmount(), 0.001);
        assertEquals("YEAR", autoRenewal.getPeriod());
        assertEquals("acc123", autoRenewal.getAccountId());
        assertEquals("Music service", autoRenewal.getNote());
    }

    @Test
    void testNoArgsConstructor() {
        AutoRenewal emptyAutoRenewal = new AutoRenewal();
        assertNull(emptyAutoRenewal.getId());
        assertNull(emptyAutoRenewal.getUserId());
        assertNull(emptyAutoRenewal.getName());
        assertEquals(0.0, emptyAutoRenewal.getAmount(), 0.001);
        assertNull(emptyAutoRenewal.getPeriod());
        assertNull(emptyAutoRenewal.getAccountId());
        assertNull(emptyAutoRenewal.getNote());
    }

    @Test
    void testAllArgsConstructor() {
        AutoRenewal newAutoRenewal = new AutoRenewal("ar789", "user123", "Gym Membership", 30.0, "MONTH", "acc456", "Fitness");

        assertEquals("ar789", newAutoRenewal.getId());
        assertEquals("user123", newAutoRenewal.getUserId());
        assertEquals("Gym Membership", newAutoRenewal.getName());
        assertEquals(30.0, newAutoRenewal.getAmount(), 0.001);
        assertEquals("MONTH", newAutoRenewal.getPeriod());
        assertEquals("acc456", newAutoRenewal.getAccountId());
        assertEquals("Fitness", newAutoRenewal.getNote());
    }

    @Test
    void testToString() {
        String expectedString = "AutoRenewal{id='ar123', userId='user456', name='Netflix Subscription', amount=15.99, period='MONTH', accountId='acc789', note='Entertainment'}";
        assertEquals(expectedString, autoRenewal.toString());
    }
}
