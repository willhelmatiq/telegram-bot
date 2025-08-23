package com.example.telegram_bot.aspect;


import com.example.telegram_bot.service.NotificationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Aspect
@Component
public class NotificationAspect {

    private final NotificationService notifications;

    public NotificationAspect(NotificationService notifications) {
        this.notifications = notifications;
    }

    @Around("execution(* com.example.telegram_bot.handlers..*.handle(..))")
    public Object notifyOnException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            String title = buildTitle(joinPoint);
            String context = buildContext(joinPoint.getArgs());
            notifications.notifyException(title, context, ex);
            throw ex;
        }
    }

    private String buildTitle(ProceedingJoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        return ms.getDeclaringTypeName() + "." + ms.getName();
    }

    private String buildContext(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Message msg) {
                Long userId = msg.getFrom() != null ? msg.getFrom().getId() : null;
                Long chatId = msg.getChatId();
                String text = msg.getText();
                return "userId=" + userId + ", chatId=" + chatId + ", text=" + text;
            }
        }
        return "";
    }
}
