package org.segroup50.financialtracker.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.data.model.Account;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoTest {
    private AccountDao accountDao;
    private static final String TEST_CSV_FILE = "test_accounts.csv";
    private static final String CSV_HEADER = "id,userId,name,type,attribute,balance";

    @BeforeEach
    void setUp() {
        // Use a test CSV file instead of the production one
        accountDao = new AccountDao() {
            @Override
            protected void initCsvFile() {
                this.csvFile = TEST_CSV_FILE;
                this.csvHeader = CSV_HEADER;
                super.initCsvFile();
            }
        };
    }

    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File file = new File(TEST_CSV_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddAndGetAccount() {
        Account account = new Account(null, "user1", "Savings Account", "Bank", "Saving", 1000.0);
        assertTrue(accountDao.addAccount(account));

        List<Account> accounts = accountDao.getAllAccounts();
        assertEquals(1, accounts.size());

        Account retrieved = accounts.get(0);
        assertNotNull(retrieved.getId());
        assertEquals("user1", retrieved.getUserId());
        assertEquals("Savings Account", retrieved.getName());
        assertEquals(1000.0, retrieved.getBalance(), 0.001);
    }

    @Test
    void testGetAccountById() {
        Account account = new Account(null, "user1", "Savings Account", "Bank", "Saving", 1000.0);
        accountDao.addAccount(account);

        List<Account> accounts = accountDao.getAllAccounts();
        String id = accounts.get(0).getId();

        Account retrieved = accountDao.getAccountById(id);
        assertNotNull(retrieved);
        assertEquals(id, retrieved.getId());
    }

    @Test
    void testGetAccountsByUserId() {
        Account account1 = new Account(null, "user1", "Account 1", "Bank", "Saving", 1000.0);
        Account account2 = new Account(null, "user2", "Account 2", "Credit", "Investment", 2000.0);
        accountDao.addAccount(account1);
        accountDao.addAccount(account2);

        List<Account> userAccounts = accountDao.getAccountsByUserId("user1");
        assertEquals(1, userAccounts.size());
        assertEquals("Account 1", userAccounts.get(0).getName());
    }

    @Test
    void testGetAccountsByName() {
        Account account1 = new Account(null, "user1", "Savings", "Bank", "Saving", 1000.0);
        Account account2 = new Account(null, "user1", "Checking", "Bank", "Saving", 1500.0);
        accountDao.addAccount(account1);
        accountDao.addAccount(account2);

        List<Account> accounts = accountDao.getAccountsByName("Savings");
        assertEquals(1, accounts.size());
        assertEquals(1000.0, accounts.get(0).getBalance(), 0.001);
    }

    @Test
    void testGetAccountsByType() {
        Account account1 = new Account(null, "user1", "Savings", "Bank", "Saving", 1000.0);
        Account account2 = new Account(null, "user1", "Credit Card", "Credit", "Investment", -500.0);
        accountDao.addAccount(account1);
        accountDao.addAccount(account2);

        List<Account> accounts = accountDao.getAccountsByType("Credit");
        assertEquals(1, accounts.size());
        assertEquals("Credit Card", accounts.get(0).getName());
    }

    @Test
    void testGetAccountsByAttribute() {
        Account account1 = new Account(null, "user1", "Savings", "Bank", "Saving", 1000.0);
        Account account2 = new Account(null, "user1", "Stocks", "Investment", "Investment", 5000.0);
        accountDao.addAccount(account1);
        accountDao.addAccount(account2);

        List<Account> accounts = accountDao.getAccountsByAttribute("Investment");
        assertEquals(1, accounts.size());
        assertEquals("Stocks", accounts.get(0).getName());
    }

    @Test
    void testUpdateAccount() {
        Account account = new Account(null, "user1", "Savings", "Bank", "Saving", 1000.0);
        accountDao.addAccount(account);

        List<Account> accounts = accountDao.getAllAccounts();
        Account toUpdate = accounts.get(0);
        toUpdate.setBalance(1500.0);

        assertTrue(accountDao.updateAccount(toUpdate));

        Account updated = accountDao.getAccountById(toUpdate.getId());
        assertEquals(1500.0, updated.getBalance(), 0.001);
    }

    @Test
    void testDeleteAccount() {
        Account account = new Account(null, "user1", "Savings", "Bank", "Saving", 1000.0);
        accountDao.addAccount(account);

        List<Account> accounts = accountDao.getAllAccounts();
        String id = accounts.get(0).getId();

        assertTrue(accountDao.deleteAccount(id));
        assertEquals(0, accountDao.getAllAccounts().size());
        assertNull(accountDao.getAccountById(id));
    }

    @Test
    void testAddMultipleAccounts() {
        Account account1 = new Account(null, "user1", "Savings", "Bank", "Saving", 1000.0);
        Account account2 = new Account(null, "user1", "Checking", "Bank", "Saving", 1500.0);

        assertTrue(accountDao.addAccount(account1));
        assertTrue(accountDao.addAccount(account2));

        List<Account> accounts = accountDao.getAllAccounts();
        assertEquals(2, accounts.size());
    }

    @Test
    void testGetNonExistentAccount() {
        assertNull(accountDao.getAccountById("nonexistent"));
    }

    @Test
    void testUpdateNonExistentAccount() {
        Account account = new Account("nonexistent", "user1", "Savings", "Bank", "Saving", 1000.0);
        assertFalse(accountDao.updateAccount(account));
    }

    @Test
    void testDeleteNonExistentAccount() {
        assertFalse(accountDao.deleteAccount("nonexistent"));
    }

    @Test
    void testFromCsvLineWithInvalidData() {
        assertNull(accountDao.fromCsvLine("invalid,data"));
    }

    @Test
    void testAddAccountWithoutUserId() {
        Account account = new Account(null, null, "Savings", "Bank", "Saving", 1000.0);
        assertThrows(IllegalArgumentException.class, () -> accountDao.addAccount(account));
    }
}
