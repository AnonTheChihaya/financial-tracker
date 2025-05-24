package org.segroup50.financialtracker.service.ai;

import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.dao.TransactionDao;
import org.segroup50.financialtracker.data.model.Transaction;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FinancialAnalyzer {
    private static final String SYSTEM_PROMPT_REPORT =
            "You are a financial advisor analyzing a user's transaction history. " +
                    "Provide a comprehensive financial report that includes:\n" +
                    "1. Spending patterns by category (top 3 categories)\n" +
                    "2. Income vs expense comparison (total amounts and percentages)\n" +
                    "3. Savings rate analysis (savings as percentage of income)\n" +
                    "4. Notable trends or changes (month-over-month comparison)\n" +
                    "5. Personalized recommendations (2-3 actionable items)\n\n" +
                    "Format the response as clear plain text without section headers and bullet points.\n" +
                    "Use simple language and avoid financial jargon.";

    private static final String SYSTEM_PROMPT_ANOMALY =
            "You are a financial fraud detection system. Analyze the following transactions " +
                    "and identify any anomalies or unusual spending patterns that may indicate:\n" +
                    "1. Potential fraud (unusual locations, amounts)\n" +
                    "2. Uncharacteristic spending (compared to user's typical patterns)\n" +
                    "3. Recurring suspicious transactions\n" +
                    "4. Unusual amounts or timings\n\n" +
                    "For each anomaly found, provide:\n" +
                    "- Transaction date and amount\n" +
                    "- Category and description if available\n" +
                    "- Reason for flagging (specific and concise)\n" +
                    "- Suggested action (monitor, verify, etc.)\n\n" +
                    "Format the response as clear plain text with numbered items.\n" +
                    "Mark high severity items with (!) symbol.";

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;
    private final OpenAIClient aiClient;

    public FinancialAnalyzer() {
        this.transactionDao = new TransactionDao();
        this.accountDao = new AccountDao();
        this.aiClient = OpenAIOkHttpClient.builder()
                .baseUrl("https://api.chatanywhere.tech")
                .apiKey("sk-07UwtvT7Hck5p4BgRM5gsiPJgkYw4Wibe3mWGPgkhQRJIx7h")
                .build();
    }

    public String generateFinancialReport(String userId) {
        List<Transaction> transactions = getLastSixMonthsTransactions(userId);
        String transactionSummary = formatTransactionsForAI(transactions);

        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O)
                .temperature(0.3)
                .maxCompletionTokens(1500)
                .addSystemMessage(SYSTEM_PROMPT_REPORT)
                .addUserMessage(transactionSummary)
                .build();

        try {
            return aiClient.chat().completions().create(createParams)
                    .choices().stream()
                    .findFirst()
                    .flatMap(choice -> choice.message().content())
                    .orElse("Unable to generate financial report at this time.");
        } catch (Exception e) {
            return "Error generating report: " + e.getMessage();
        }
    }

    public String detectAnomalies(String userId) {
        List<Transaction> transactions = getLastThreeMonthsTransactions(userId);
        String transactionSummary = formatTransactionsForAI(transactions);

        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O)
                .temperature(0.2)
                .maxCompletionTokens(1000)
                .addSystemMessage(SYSTEM_PROMPT_ANOMALY)
                .addUserMessage(transactionSummary)
                .build();

        try {
            return aiClient.chat().completions().create(createParams)
                    .choices().stream()
                    .findFirst()
                    .flatMap(choice -> choice.message().content())
                    .orElse("Unable to analyze transactions for anomalies at this time.");
        } catch (Exception e) {
            return "Error detecting anomalies: " + e.getMessage();
        }
    }

    private List<Transaction> getLastSixMonthsTransactions(String userId) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = sixMonthsAgo.format(formatter);
        String endDate = LocalDate.now().format(formatter);

        return transactionDao.getTransactionsByDateRange(startDate, endDate).stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    private List<Transaction> getLastThreeMonthsTransactions(String userId) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = threeMonthsAgo.format(formatter);
        String endDate = LocalDate.now().format(formatter);

        return transactionDao.getTransactionsByDateRange(startDate, endDate).stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    private String formatTransactionsForAI(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction History:\n");
        sb.append("Date\t\tAmount\tType\tCategory\tAccount\t\tNotes\n");
        sb.append("------------------------------------------------------------\n");

        for (Transaction t : transactions) {
            String accountName = accountDao.getAccountById(t.getAccountId()).getName();
            sb.append(String.format("%s\t$%.2f\t%s\t%s\t%s\t%s\n",
                    t.getDate(),
                    t.getAmount(),
                    t.getType(),
                    t.getCategory(),
                    accountName,
                    t.getNote() != null ? t.getNote() : ""));
        }

        return sb.toString();
    }
}
