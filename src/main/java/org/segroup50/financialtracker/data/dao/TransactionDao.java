package org.segroup50.financialtracker.data.dao;

import org.segroup50.financialtracker.data.BaseCsvDao;
import org.segroup50.financialtracker.data.model.Transaction;

import java.util.List;
import java.util.UUID;

public class TransactionDao extends BaseCsvDao<Transaction> {
    private static final String CSV_FILE = "transactions.csv";
    private static final String CSV_HEADER = "id,date,amount,type,category,accountId,userId,note";

    public TransactionDao() {
        super(CSV_FILE, CSV_HEADER);
    }

    @Override
    protected String getId(Transaction transaction) {
        return transaction.getId();
    }

    @Override
    protected String toCsvLine(Transaction transaction) {
        if (transaction.getId() == null || transaction.getId().isEmpty()) {
            transaction.setId(UUID.randomUUID().toString());
        }

        if (transaction.getUserId() == null || transaction.getUserId().isEmpty()) {
            throw new IllegalArgumentException("Transaction must have a userId");
        }

        if (transaction.getAccountId() == null || transaction.getAccountId().isEmpty()) {
            throw new IllegalArgumentException("Transaction must have an accountId");
        }

        return String.join(",",
                transaction.getId(),
                transaction.getDate(),
                String.valueOf(transaction.getAmount()),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getAccountId(),
                transaction.getUserId(),
                transaction.getNote() != null ? transaction.getNote() : "");
    }

    @Override
    protected Transaction fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 8) {
            return null;
        }
        try {
            double amount = Double.parseDouble(parts[2]);
            String note = parts.length > 7 ? parts[7] : "";
            return new Transaction(parts[0], parts[1], amount, parts[3], parts[4], parts[5], parts[6], note);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing amount: " + e.getMessage());
            return null;
        }
    }

    // 保持原有方法签名
    public boolean addTransaction(Transaction transaction) {
        return add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return getAll();
    }

    public Transaction getTransactionById(String id) {
        return getAll().stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Transaction> getTransactionsByUserId(String userId) {
        return getAll().stream()
                .filter(transaction -> transaction.getUserId().equals(userId))
                .toList();
    }

    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return getAll().stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .toList();
    }

    public List<Transaction> getTransactionsByType(String type) {
        return getAll().stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase(type))
                .toList();
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        return getAll().stream()
                .filter(transaction -> transaction.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        return getAll().stream()
                .filter(transaction -> transaction.getDate().compareTo(startDate) >= 0 &&
                        transaction.getDate().compareTo(endDate) <= 0)
                .toList();
    }

    public boolean updateTransaction(Transaction updatedTransaction) {
        return update(updatedTransaction, updatedTransaction.getId());
    }

    public boolean deleteTransaction(String id) {
        return delete(id);
    }
}
