package com.sambathresmey.telegram_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sambathresmey.telegram_service.api.ApiConnector;

@SuppressWarnings("deprecation")
public class TelegramService extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            sendTypingAction(chatId);

            ApiConnector apiConnector = new ApiConnector("https://api.blackbox.ai/api");

            JsonObject requestJson = new JsonObject();
            JsonArray messagesArray = new JsonArray();

            JsonObject messObject = new JsonObject();
            messObject.addProperty("content", messageText);
            messObject.addProperty("role", "user");

            messagesArray.add(messObject);
            requestJson.add("messages", messagesArray);
            requestJson.addProperty("model", "deepseek-ai/DeepSeek-V3");
            requestJson.addProperty("max_tokens", "1024");

            Optional<String> postResponse = apiConnector.post("/chat", requestJson.toString());

            // Create a response message
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(postResponse.orElse("No response received").toString());
            message.setParseMode("Markdown");
            

            try {
                execute(message); // Sending the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendTypingAction(long chatId) {
        SendChatAction action = new SendChatAction();
        action.setChatId(String.valueOf(chatId));
        action.setAction(ActionType.TYPING);
        try {
            execute(action);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}