package org.segroup50.financialtracker.service.transaction;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.dao.TransactionDao;
import org.segroup50.financialtracker.data.model.Transaction;

import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao;
    private AccountDao accountDao;

    public TransactionService() {
        this.transactionDao = new TransactionDao();
        this.accountDao = new AccountDao();
    }

    public List<Transaction> getUserTransactions() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return List.of();
        }
        return transactionDao.getTransactionsByUserId(CurrentUserConfig.getCurrentUserId());
    }

    public String getAccountName(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            return "Unknown";
        }
        Account account = accountDao.getAccountById(accountId);
        return account != null ? account.getName() : "Unknown";
    }

    public boolean addTransaction(Transaction transaction) {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return false;
        }

        transaction.setUserId(CurrentUserConfig.getCurrentUserId());
        Account account = accountDao.getAccountById(transaction.getAccountId());
        if (account == null) {
            return false;
        }

        // Update account balance
        double amount = transaction.getAmount();
        if ("expense".equalsIgnoreCase(transaction.getType())) {
            account.setBalance(account.getBalance() - Math.abs(amount));
        } else if ("income".equalsIgnoreCase(transaction.getType())) {
            account.setBalance(account.getBalance() + Math.abs(amount));
        }

        // Update account first
        boolean accountUpdated = accountDao.updateAccount(account);
        if (!accountUpdated) {
            return false;
        }

        // Then add transaction
        boolean transactionAdded = transactionDao.addTransaction(transaction);
        if (!transactionAdded) {
            // Rollback account balance
            if ("expense".equalsIgnoreCase(transaction.getType())) {
                account.setBalance(account.getBalance() + Math.abs(amount));
            } else if ("income".equalsIgnoreCase(transaction.getType())) {
                account.setBalance(account.getBalance() - Math.abs(amount));
            }
            accountDao.updateAccount(account);
            return false;
        }

        return true;
    }

    public boolean deleteTransaction(int rowIndex) {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return false;
        }

        List<Transaction> transactions = transactionDao.getTransactionsByUserId(CurrentUserConfig.getCurrentUserId());
        if (rowIndex < 0 || rowIndex >= transactions.size()) {
            return false;
        }

        Transaction transaction = transactions.get(rowIndex);
        Account account = accountDao.getAccountById(transaction.getAccountId());
        if (account == null) {
            return false;
        }

        // Reverse the transaction effect
        double amount = transaction.getAmount();
        if ("expense".equalsIgnoreCase(transaction.getType())) {
            account.setBalance(account.getBalance() + Math.abs(amount));
        } else if ("income".equalsIgnoreCase(transaction.getType())) {
            account.setBalance(account.getBalance() - Math.abs(amount));
        }

        // Update account first
        boolean accountUpdated = accountDao.updateAccount(account);
        if (!accountUpdated) {
            return false;
        }

        // Then delete transaction
        boolean success = transactionDao.deleteTransaction(transaction.getId());
        if (!success) {
            // Rollback account balance
            if ("expense".equalsIgnoreCase(transaction.getType())) {
                account.setBalance(account.getBalance() - Math.abs(amount));
            } else if ("income".equalsIgnoreCase(transaction.getType())) {
                account.setBalance(account.getBalance() + Math.abs(amount));
            }
            accountDao.updateAccount(account);
            return false;
        }

        return true;
    }
}
