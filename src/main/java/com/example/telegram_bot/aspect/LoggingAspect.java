package com.example.telegram_bot.aspect;

import com.example.telegram_bot.handlers.CommandHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;


@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Логируем любые методы, помеченные @LogCommand.
     * Ожидаем первым аргументом объект Message (телеграма).
     */
    @Around("@annotation(com.example.telegram_bot.annotation.LogCommand) && args(message,..)")
    public Object logCommandCall(ProceedingJoinPoint joinPoint, Message message) throws Throwable {
        // Имя команды: берём из хендлера (если метод принадлежит CommandHandler)
        String command = extractCommand(joinPoint, message);
        // Идентификатор пользователя: сперва user.id, иначе chatId
        Long userId = extractUserId(message);

        try {
            Object result = joinPoint.proceed();

            String asString = (result == null) ? "null" : result.toString();
//            String truncated = truncate(asString, 300);

            log.info("Command executed: command='{}', userId={}, result=\"{}\"",
                    command, userId, asString);

            return result;
        } catch (Throwable ex) {
            log.error("Command failed: command='{}', userId={}, error='{}'",
                    command, userId, ex.toString(), ex);
            throw ex;
        }
    }

    private String extractCommand(ProceedingJoinPoint joinPoint, Message message) {
        // 1) если цель — CommandHandler, берём из handler.command()
        Object target = joinPoint.getTarget();
        if (target instanceof CommandHandler handler) {
            try {
                return handler.command();
            } catch (Exception ignore) { /* fallback ниже */ }
        }
        // 2) fallback: из текста сообщения (первое слово)
        String text = message != null ? message.getText() : null;
        if (text != null) {
            String trimmed = text.trim();
            int space = trimmed.indexOf(' ');
            return space > 0 ? trimmed.substring(0, space) : trimmed;
        }
        return "unknown";
    }

    private Long extractUserId(Message message) {
        if (message == null) return null;
        // user.id (если есть)
        Long fromId = Optional.ofNullable(message.getFrom())
                .map(User::getId)
                .orElse(null);
        if (fromId != null) return fromId;
        try {
            return message.getChatId();
        } catch (Exception e) {
            return null;
        }
    }
}
