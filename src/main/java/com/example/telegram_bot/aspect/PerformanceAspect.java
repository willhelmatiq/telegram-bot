package com.example.telegram_bot.aspect;

import com.example.telegram_bot.annotation.MonitorPerformance;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class PerformanceAspect {
    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);

    @Value("${spring.application.performance.threshold-ms:500}")
    private long thresholdMs;

    @Around("@annotation(com.example.telegram_bot.annotation.MonitorPerformance) || " +
            "@within(com.example.telegram_bot.annotation.MonitorPerformance)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startNs = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            if (tookMs > thresholdMs) {
                log.warn("[PERF] Slow exec: {}.{} took {} ms {}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        tookMs, buildContext(joinPoint.getArgs()));
            } else if (log.isDebugEnabled()) {
                log.debug("[PERF] {}.{} took {} ms{}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        tookMs, buildContext(joinPoint.getArgs()));
            }
            return result;
        } catch (Throwable ex) {
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            log.warn("[PERF] Exception after {} ms in {}.{}: {}{}",
                    tookMs,
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex,
                    buildContext(joinPoint.getArgs()));
            throw ex;
        }
    }

    private String buildContext(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Message msg) {
                Long uid = msg.getFrom() != null ? msg.getFrom().getId() : null;
                String text = msg.getText();
                return String.format(" [userId=%s, text=%s]", uid, text);
            }
        }
        return "";
    }
}
