package com.example.telegram_bot.service;

import com.example.telegram_bot.configuration.NotificationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.Objects;


@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private static final int TELEGRAM_LIMIT = 4096;

    private final TelegramClient telegramClient;
    private final NotificationConfig props;

    public NotificationService(TelegramClient telegramClient, NotificationConfig props) {
        this.telegramClient = telegramClient;
        this.props = props;
    }

    public boolean isEnabled() {
        return props.isEnabled() && props.getChatId() != null;
    }

    public boolean shouldSkip(Throwable ex) {
        if (ex == null || props.getSkipExceptions() == null) return false;
        String name = ex.getClass().getName();
        return props.getSkipExceptions().stream().anyMatch(s -> Objects.equals(s, name));
    }

    @Async
    public void notifyException(String title, String context, Throwable ex, Long userId) {
        if (!isEnabled() || shouldSkip(ex)) {
            return;
        }
        Long chatId = userId != null ? userId : props.getChatId();

        StringBuilder sb = new StringBuilder();
        sb.append("🚨 <b>").append(escape(title)).append("</b>\n");
        if (context != null && !context.isBlank()) {
            sb.append(escape(context)).append("\n");
        }
        sb.append("<b>Error:</b> ").append(escape(ex.getClass().getName()))
                .append(": ").append(escape(String.valueOf(ex.getMessage()))).append("\n");

        if (props.isIncludeStacktrace() && ex.getStackTrace() != null) {
            sb.append("<b>Stack:</b>\n");
            Arrays.stream(ex.getStackTrace())
                    .limit(props.getStacktraceMaxLines())
                    .forEach(st -> sb.append(escape(st.toString())).append("\n"));
        }

        String text = sb.toString();
        if (text.length() > TELEGRAM_LIMIT) {
            text = text.substring(0, TELEGRAM_LIMIT - 1) + "…";
        }

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .build();

        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            log.warn("Failed to send notification to Telegram: {}", e.getMessage());
        }
    }

    private String escape(String s) {
        if (s == null) return "null";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
