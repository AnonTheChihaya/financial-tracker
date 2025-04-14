package org.segroup50.financialtracker.service.utils;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionContentPart;
import com.openai.models.chat.completions.ChatCompletionContentPartImage;
import com.openai.models.chat.completions.ChatCompletionContentPartText;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

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

    public String extractTransactionDetails(byte[] imageBytes) throws IOException {
        String imageBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);

        ChatCompletionContentPart imagePart = ChatCompletionContentPart.ofImageUrl(
                ChatCompletionContentPartImage.builder()
                        .imageUrl(ChatCompletionContentPartImage.ImageUrl.builder()
                                .url(imageBase64)
                                .build())
                        .build());

        ChatCompletionContentPart instructionPart = ChatCompletionContentPart.ofText(
                ChatCompletionContentPartText.builder()
                        .text("Extract transaction details from this image.")
                        .build());

        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O)
                .maxCompletionTokens(500)
                .addSystemMessage(SYSTEM_PROMPT)
                .addUserMessageOfArrayOfContentParts(List.of(instructionPart, imagePart))
                .build();

        return client.chat().completions().create(createParams)
                .choices().stream()
                .findFirst()
                .flatMap(choice -> choice.message().content())
                .orElse("{}");
    }
}
