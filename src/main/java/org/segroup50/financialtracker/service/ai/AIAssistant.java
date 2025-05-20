package org.segroup50.financialtracker.service.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class AIAssistant {
    private static final String SYSTEM_PROMPT =
            "You are a helpful assistant for a personal finance management application. " +
                    "Use the following application features to answer user questions:\n\n" +

                    "REGISTRATION & LOGIN:\n" +
                    "- New users must provide username, email, mobile number, and password\n" +
                    "- TOTP authenticator (e.g., Microsoft Authenticator) is mandatory\n" +
                    "- Login requires username, password, and TOTP code\n\n" +

                    "ACCOUNT MANAGEMENT:\n" +
                    "- Create/view savings and investment accounts\n" +
                    "- Modify contact info with validation\n" +
                    "- Change password requires TOTP verification\n\n" +

                    "TRANSACTION RECORDING:\n" +
                    "- Manual entry with date, amount, type, category, notes\n" +
                    "- Predefined categories with auto-tagging\n" +
                    "- Filter records by month/criteria\n\n" +

                    "SMART TRANSACTION IMPORT:\n" +
                    "- Upload bill screenshots for single-entry recognition\n" +
                    "- Bulk import from bank/WeChat Payment files\n\n" +

                    "FINANCIAL PLANNING:\n" +
                    "- Set savings goals with progress tracking\n" +
                    "- Create periodic budgets with alerts\n" +
                    "- Manage recurring payments\n\n" +

                    "DATA VISUALIZATION:\n" +
                    "- Dashboard with assets and trends\n" +
                    "- AI-generated financial reports\n\n" +

                    "SECURITY ALERTS:\n" +
                    "- Notifications for unusual large expenses\n\n" +

                    "Guidelines:\n" +
                    "- Be concise and specific\n" +
                    "- Only answer questions about the app's features\n" +
                    "- If unsure, ask for clarification\n" +
                    "- Format responses clearly with line breaks when needed";

    private final OpenAIClient aiClient;

    public AIAssistant() {
        this.aiClient = OpenAIOkHttpClient.builder()
                .baseUrl("https://api.chatanywhere.tech")
                .apiKey("sk-07UwtvT7Hck5p4BgRM5gsiPJgkYw4Wibe3mWGPgkhQRJIx7h")
                .build();
    }

    public String getHelpResponse(String userQuestion) {
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O)
                .temperature(0.3)
                .maxCompletionTokens(1000)
                .addSystemMessage(SYSTEM_PROMPT)
                .addUserMessage(userQuestion)
                .build();

        try {
            return aiClient.chat().completions().create(createParams)
                    .choices().stream()
                    .findFirst()
                    .flatMap(choice -> choice.message().content())
                    .orElse("I couldn't process your request. Please try again.");
        } catch (Exception e) {
            return "Error getting help: " + e.getMessage();
        }
    }
}
