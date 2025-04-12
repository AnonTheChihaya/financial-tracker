package org.segroup50.financialtracker.service.account;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.dao.TransactionDao;
import org.segroup50.financialtracker.data.model.Transaction;

import java.util.List;

public class AccountService {
    private AccountDao accountDao;
    private TransactionDao transactionDao;

    public AccountService() {
        this.accountDao = new AccountDao();
        this.transactionDao = new TransactionDao();
    }

    public List<Account> getUserAccounts() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return List.of();
        }
        return accountDao.getAccountsByUserId(CurrentUserConfig.getCurrentUserId());
    }

    public boolean addAccount(Account account) {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return false;
        }
        account.setUserId(CurrentUserConfig.getCurrentUserId());
        return accountDao.addAccount(account);
    }

    public boolean deleteAccount(Account account) {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return false;
        }

        // First delete all transactions associated with this account
        List<Transaction> accountTransactions = transactionDao.getTransactionsByAccountId(account.getId());
        for (Transaction transaction : accountTransactions) {
            if (!transactionDao.deleteTransaction(transaction.getId())) {
                return false;
            }
        }

        // Then delete the account itself
        return accountDao.deleteAccount(account.getId());
    }

    public Account getAccountById(String accountId) {
        return accountDao.getAccountById(accountId);
    }
}
