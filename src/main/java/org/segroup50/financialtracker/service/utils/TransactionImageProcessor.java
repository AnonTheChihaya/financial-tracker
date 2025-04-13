package org.segroup50.financialtracker.service.utils;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

public class TransactionImageProcessor {
    private static final String SYSTEM_PROMPT = """
            Analyze this payment screenshot and extract the following details:
            - Transaction date (format: YYYY-MM-DD)
            - Amount (as a number)
            - Transaction type (either "Income" or "Expense")
            - Category (choose from standard categories)
            - Notes (any additional information from the image)
            
            Return the information in JSON format with these exact keys:
            {
                "date": "YYYY-MM-DD",
                "amount": 0.0,
                "type": "Income/Expense",
                "category": "Category Name",
                "notes": "Additional information"
            }
            
            If any field cannot be determined, use these defaults:
            - date: today's date
            - type: "Expense"
            - category: "Other Expense" or "Other Income" based on type
            - notes: ""
            """;

    private final OpenAIClient client;

    public TransactionImageProcessor() {
        this.client = OpenAIOkHttpClient.builder()
                .baseUrl("https://api.chatanywhere.tech")
                .apiKey("sk-07UwtvT7Hck5p4BgRM5gsiPJgkYw4Wibe3mWGPgkhQRJIx7h")
                .build();
    }
}
