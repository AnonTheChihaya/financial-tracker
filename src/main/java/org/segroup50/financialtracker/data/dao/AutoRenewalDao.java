package org.segroup50.financialtracker.data.dao;

import org.segroup50.financialtracker.data.BaseCsvDao;
import org.segroup50.financialtracker.data.model.AutoRenewal;

import java.util.List;
import java.util.UUID;

public class AutoRenewalDao extends BaseCsvDao<AutoRenewal> {
    private static final String CSV_FILE = "auto_renewals.csv";
    private static final String CSV_HEADER = "id,userId,name,amount,period,accountId,note";

    public AutoRenewalDao() {
        super(CSV_FILE, CSV_HEADER);
    }

    @Override
    protected String getId(AutoRenewal autoRenewal) {
        return autoRenewal.getId();
    }

    @Override
    protected String toCsvLine(AutoRenewal autoRenewal) {
        if (autoRenewal.getId() == null || autoRenewal.getId().isEmpty()) {
            autoRenewal.setId(UUID.randomUUID().toString());
        }

        if (autoRenewal.getUserId() == null || autoRenewal.getUserId().isEmpty()) {
            throw new IllegalArgumentException("AutoRenewal must have a userId");
        }

        if (autoRenewal.getAccountId() == null || autoRenewal.getAccountId().isEmpty()) {
            throw new IllegalArgumentException("AutoRenewal must have an accountId");
        }

        return String.join(",",
                autoRenewal.getId(),
                autoRenewal.getUserId(),
                autoRenewal.getName(),
                String.valueOf(autoRenewal.getAmount()),
                autoRenewal.getPeriod(),
                autoRenewal.getAccountId(),
                autoRenewal.getNote() != null ? autoRenewal.getNote() : "");
    }

    @Override
    protected AutoRenewal fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) {
            return null;
        }
        try {
            double amount = Double.parseDouble(parts[3]);
            String note = parts.length > 6 ? parts[6] : "";
            return new AutoRenewal(parts[0], parts[1], parts[2], amount, parts[4], parts[5], note);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing amount: " + e.getMessage());
            return null;
        }
    }

    // Custom methods
    public boolean addAutoRenewal(AutoRenewal autoRenewal) {
        return add(autoRenewal);
    }

    public List<AutoRenewal> getAllAutoRenewals() {
        return getAll();
    }

    public AutoRenewal getAutoRenewalById(String id) {
        return getAll().stream()
                .filter(autoRenewal -> autoRenewal.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<AutoRenewal> getAutoRenewalsByUserId(String userId) {
        return getAll().stream()
                .filter(autoRenewal -> autoRenewal.getUserId().equals(userId))
                .toList();
    }

    public List<AutoRenewal> getAutoRenewalsByAccountId(String accountId) {
        return getAll().stream()
                .filter(autoRenewal -> autoRenewal.getAccountId().equals(accountId))
                .toList();
    }

    public List<AutoRenewal> getAutoRenewalsByPeriod(String period) {
        return getAll().stream()
                .filter(autoRenewal -> autoRenewal.getPeriod().equalsIgnoreCase(period))
                .toList();
    }

    public boolean updateAutoRenewal(AutoRenewal updatedAutoRenewal) {
        return update(updatedAutoRenewal, updatedAutoRenewal.getId());
    }

    public boolean deleteAutoRenewal(String id) {
        return delete(id);
    }
}
