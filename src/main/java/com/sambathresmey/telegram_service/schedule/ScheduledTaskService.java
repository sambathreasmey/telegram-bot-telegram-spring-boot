package com.sambathresmey.telegram_service.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import com.sambathresmey.telegram_service.service.TelegramService;

@Service
public class ScheduledTaskService {

    private final TelegramService telegramBotService;

    public ScheduledTaskService(TelegramBot telegramBot) {
        this.telegramBotService = (TelegramService) telegramBot;
    }

    // This method will run every 5 seconds
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void performTask() {
        long SAMBATH_REASMEY = 762808963;
        String messageText = "✅ Telegram Bot កំពុងដំណើរការធម្មតា";
        SendMessage message = new SendMessage();
        message.setChatId(SAMBATH_REASMEY);
        message.setText(messageText);

        try {
            telegramBotService.execute(message);
            System.out.println("Message sent: " + messageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // This method will run at a fixed time (e.g., every day at 10:00 AM)
    @Scheduled(cron = "0 0 10 * * ?")
    public void performDailyTask() {
        System.out.println("Daily task executed at: " + System.currentTimeMillis());
        // Add your daily task logic here
    }
}