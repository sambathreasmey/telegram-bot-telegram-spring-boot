package com.sambathresmey.telegram_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.sambathresmey.telegram_service.service.TelegramService;

@Configuration
@EnableScheduling
public class TelegramBotConfig {

    @Bean
    public TelegramLongPollingBot myTelegramBot() {
        return new TelegramService();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramLongPollingBot myTelegramBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(myTelegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return botsApi;
    }
}