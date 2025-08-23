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

    @Around("@annotation(com.example.telegram_bot.annotation.LogCommand) && args(message,..)")
    public Object logCommandCall(ProceedingJoinPoint joinPoint, Message message) throws Throwable {
        String command = extractCommand(joinPoint);
        Long userId = extractUserId(message);

        try {
            Object result = joinPoint.proceed();

            String asString = (result == null) ? "null" : result.toString();
            log.info("Command executed: command='{}', userId={}, result=\"{}\"",
                    command, userId, asString);
            return result;
        } catch (Throwable ex) {
            log.error("Command failed: command='{}', userId={}, error='{}'",
                    command, userId, ex, ex);
            throw ex;
        }
    }

    private String extractCommand(ProceedingJoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target instanceof CommandHandler handler) {
            return handler.command();
        }
        return "unknown command";
    }

    private Long extractUserId(Message message) {
        if (message == null) {
            return null;
        }
        return Optional.ofNullable(message.getFrom())
                .map(User::getId)
                .orElse(null);
    }
}
