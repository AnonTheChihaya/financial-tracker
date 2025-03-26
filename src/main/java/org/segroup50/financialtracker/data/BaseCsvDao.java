package org.segroup50.financialtracker.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseCsvDao<T> {
    protected String csvFile;
    protected String csvHeader;

    protected BaseCsvDao(String csvFile, String csvHeader) {
        this.csvFile = csvFile;
        this.csvHeader = csvHeader;
        initCsvFile();
    }

    protected void initCsvFile() {
        File file = new File(csvFile);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
                writer.println(csvHeader);
            } catch (IOException e) {
                System.err.println("Error initializing CSV file: " + e.getMessage());
            }
        }
    }

    public boolean add(T item) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true))) {
            writer.println(toCsvLine(item));
            return true;
        } catch (IOException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        }
    }

    public List<T> getAll() {
        List<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            // 跳过标题行
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                T item = fromCsvLine(line);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading items: " + e.getMessage());
        }
        return items;
    }

    public boolean update(T updatedItem, String id) {
        List<T> items = getAll();
        boolean found = false;

        for (int i = 0; i < items.size(); i++) {
            if (getId(items.get(i)).equals(id)) {
                items.set(i, updatedItem);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        return writeAll(items);
    }

    public boolean delete(String id) {
        List<T> items = getAll();
        boolean removed = items.removeIf(item -> getId(item).equals(id));

        if (!removed) {
            return false;
        }

        return writeAll(items);
    }

    protected boolean writeAll(List<T> items) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println(csvHeader);
            for (T item : items) {
                writer.println(toCsvLine(item));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing items: " + e.getMessage());
            return false;
        }
    }

    protected abstract String getId(T item);
    protected abstract String toCsvLine(T item);
    protected abstract T fromCsvLine(String csvLine);
}
