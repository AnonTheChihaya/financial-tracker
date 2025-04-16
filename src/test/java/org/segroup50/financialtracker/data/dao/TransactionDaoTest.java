package org.segroup50.financialtracker.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.data.model.Transaction;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDaoTest {
    private TransactionDao transactionDao;
    private static final String TEST_CSV_FILE = "test_transactions.csv";
    private static final String CSV_HEADER = "id,date,amount,type,category,accountId,userId,note";

    @BeforeEach
    void setUp() {
        // Use a test CSV file instead of the production one
        transactionDao = new TransactionDao() {
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
    void testAddAndGetTransaction() {
        Transaction transaction = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Monthly salary");
        assertTrue(transactionDao.addTransaction(transaction));

        List<Transaction> transactions = transactionDao.getAllTransactions();
        assertEquals(1, transactions.size());

        Transaction retrieved = transactions.get(0);
        assertNotNull(retrieved.getId());
        assertEquals("2023-01-01", retrieved.getDate());
        assertEquals(100.0, retrieved.getAmount());
        assertEquals("Income", retrieved.getType());
        assertEquals("Salary", retrieved.getCategory());
        assertEquals("acc123", retrieved.getAccountId());
        assertEquals("user123", retrieved.getUserId());
        assertEquals("Monthly salary", retrieved.getNote());
    }

    @Test
    void testGetTransactionById() {
        Transaction transaction = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Monthly salary");
        transactionDao.addTransaction(transaction);

        List<Transaction> transactions = transactionDao.getAllTransactions();
        String id = transactions.get(0).getId();

        Transaction retrieved = transactionDao.getTransactionById(id);
        assertNotNull(retrieved);
        assertEquals(id, retrieved.getId());
    }

    @Test
    void testGetTransactionsByUserId() {
        Transaction t1 = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Breakfast");
        Transaction t2 = new Transaction(null, "2023-01-02", 50.0, "Expense",
                "Food", "acc123", "user123", "Lunch");
        Transaction t3 = new Transaction(null, "2023-01-03", 75.0, "Expense",
                "Entertainment", "acc456", "user456", "Movie");

        transactionDao.addTransaction(t1);
        transactionDao.addTransaction(t2);
        transactionDao.addTransaction(t3);

        List<Transaction> userTransactions = transactionDao.getTransactionsByUserId("user123");
        assertEquals(2, userTransactions.size());
        assertTrue(userTransactions.stream().allMatch(t -> t.getUserId().equals("user123")));
    }

    @Test
    void testGetTransactionsByAccountId() {
        Transaction t1 = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Breakfast");
        Transaction t2 = new Transaction(null, "2023-01-02", 50.0, "Expense",
                "Food", "acc123", "user123", "Lunch");
        Transaction t3 = new Transaction(null, "2023-01-03", 75.0, "Expense",
                "Entertainment", "acc456", "user123", "Movie");

        transactionDao.addTransaction(t1);
        transactionDao.addTransaction(t2);
        transactionDao.addTransaction(t3);

        List<Transaction> accountTransactions = transactionDao.getTransactionsByAccountId("acc123");
        assertEquals(2, accountTransactions.size());
        assertTrue(accountTransactions.stream().allMatch(t -> t.getAccountId().equals("acc123")));
    }

    @Test
    void testGetTransactionsByType() {
        Transaction t1 = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Breakfast");
        Transaction t2 = new Transaction(null, "2023-01-02", 50.0, "Expense",
                "Food", "acc123", "user123", "Lunch");
        Transaction t3 = new Transaction(null, "2023-01-03", 75.0, "Expense",
                "Entertainment", "acc456", "user123", "Movie");

        transactionDao.addTransaction(t1);
        transactionDao.addTransaction(t2);
        transactionDao.addTransaction(t3);

        List<Transaction> incomeTransactions = transactionDao.getTransactionsByType("Income");
        assertEquals(1, incomeTransactions.size());
        assertEquals("Income", incomeTransactions.get(0).getType());
    }

    @Test
    void testGetTransactionsByCategory() {
        Transaction t1 = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Breakfast");
        Transaction t2 = new Transaction(null, "2023-01-02", 50.0, "Expense",
                "Food", "acc123", "user123", "Lunch");
        Transaction t3 = new Transaction(null, "2023-01-03", 75.0, "Expense",
                "Food", "acc456", "user123", "Dinner");

        transactionDao.addTransaction(t1);
        transactionDao.addTransaction(t2);
        transactionDao.addTransaction(t3);

        List<Transaction> foodTransactions = transactionDao.getTransactionsByCategory("Food");
        assertEquals(2, foodTransactions.size());
        assertTrue(foodTransactions.stream().allMatch(t -> t.getCategory().equals("Food")));
    }

    @Test
    void testGetTransactionsByDateRange() {
        Transaction t1 = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Breakfast");
        Transaction t2 = new Transaction(null, "2023-01-15", 50.0, "Expense",
                "Food", "acc123", "user123", "Lunch");
        Transaction t3 = new Transaction(null, "2023-02-01", 75.0, "Expense",
                "Food", "acc456", "user123", "Dinner");

        transactionDao.addTransaction(t1);
        transactionDao.addTransaction(t2);
        transactionDao.addTransaction(t3);

        List<Transaction> janTransactions = transactionDao.getTransactionsByDateRange("2023-01-01", "2023-01-31");
        assertEquals(2, janTransactions.size());
        assertTrue(janTransactions.stream().allMatch(t ->
                t.getDate().compareTo("2023-01-01") >= 0 &&
                        t.getDate().compareTo("2023-01-31") <= 0));
    }

    @Test
    void testUpdateTransaction() {
        Transaction transaction = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Monthly salary");
        transactionDao.addTransaction(transaction);

        List<Transaction> transactions = transactionDao.getAllTransactions();
        Transaction toUpdate = transactions.get(0);
        toUpdate.setAmount(120.0);
        toUpdate.setNote("Updated salary");

        assertTrue(transactionDao.updateTransaction(toUpdate));

        Transaction updated = transactionDao.getTransactionById(toUpdate.getId());
        assertEquals(120.0, updated.getAmount());
        assertEquals("Updated salary", updated.getNote());
    }

    @Test
    void testDeleteTransaction() {
        Transaction transaction = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", "user123", "Monthly salary");
        transactionDao.addTransaction(transaction);

        List<Transaction> transactions = transactionDao.getAllTransactions();
        String id = transactions.get(0).getId();

        assertTrue(transactionDao.deleteTransaction(id));
        assertEquals(0, transactionDao.getAllTransactions().size());
        assertNull(transactionDao.getTransactionById(id));
    }

    @Test
    void testAddTransactionWithoutUserIdOrAccountId() {
        Transaction noUserId = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", "acc123", null, "Breakfast");
        assertThrows(IllegalArgumentException.class, () -> transactionDao.addTransaction(noUserId));

        Transaction noAccountId = new Transaction(null, "2023-01-01", 100.0, "Income",
                "Salary", null, "user123", "Breakfast");
        assertThrows(IllegalArgumentException.class, () -> transactionDao.addTransaction(noAccountId));
    }

    @Test
    void testFromCsvLineWithInvalidData() {
        assertNull(transactionDao.fromCsvLine("invalid,data"));
    }

    @Test
    void testFromCsvLineWithInvalidAmount() {
        assertNull(transactionDao.fromCsvLine("id123,2023-01-01,invalid,Income,Salary,acc123,user123,Note"));
    }
}
