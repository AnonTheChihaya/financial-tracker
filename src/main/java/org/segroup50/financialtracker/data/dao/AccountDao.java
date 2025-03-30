package org.segroup50.financialtracker.data.dao;

import org.segroup50.financialtracker.data.BaseCsvDao;
import org.segroup50.financialtracker.data.model.Account;

import java.util.List;
import java.util.UUID;

public class AccountDao extends BaseCsvDao<Account> {
    private static final String CSV_FILE = "accounts.csv";
    private static final String CSV_HEADER = "id,userId,name,type,attribute,balance";

    public AccountDao() {
        super(CSV_FILE, CSV_HEADER);
    }

    @Override
    protected String getId(Account account) {
        return account.getId();
    }

    @Override
    protected String toCsvLine(Account account) {
        if (account.getId() == null || account.getId().isEmpty()) {
            account.setId(UUID.randomUUID().toString());
        }

        if (account.getUserId() == null || account.getUserId().isEmpty()) {
            throw new IllegalArgumentException("Account must have a userId");
        }

        return String.join(",",
                account.getId(),
                account.getUserId(),
                account.getName(),
                account.getType(),
                account.getAttribute(),
                String.valueOf(account.getBalance()));
    }

    @Override
    protected Account fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 6) {
            return null;
        }
        try {
            double balance = Double.parseDouble(parts[5]);
            return new Account(parts[0], parts[1], parts[2], parts[3], parts[4], balance);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing balance: " + e.getMessage());
            return null;
        }
    }

    // 保持原有方法签名
    public boolean addAccount(Account account) {
        return add(account);
    }

    public List<Account> getAllAccounts() {
        return getAll();
    }

    public Account getAccountById(String id) {
        return getAll().stream()
                .filter(account -> account.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAccountsByUserId(String userId) {
        return getAll().stream()
                .filter(account -> account.getUserId().equals(userId))
                .toList();
    }

    public List<Account> getAccountsByName(String name) {
        return getAll().stream()
                .filter(account -> account.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<Account> getAccountsByType(String type) {
        return getAll().stream()
                .filter(account -> account.getType().equalsIgnoreCase(type))
                .toList();
    }

    public List<Account> getAccountsByAttribute(String attribute) {
        return getAll().stream()
                .filter(account -> account.getAttribute().equalsIgnoreCase(attribute))
                .toList();
    }

    public boolean updateAccount(Account updatedAccount) {
        return update(updatedAccount, updatedAccount.getId());
    }

    public boolean deleteAccount(String id) {
        return delete(id);
    }
}
