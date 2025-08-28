package com.example.telegram_bot.handlers;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
//@Primary
public class AdvancedQuoteCommandHandler implements CommandHandler {

    private final Random random = new Random();

    @PostConstruct
    public void init() {
        log.info(this.getClass().getCanonicalName() + "is initialized");
    }

    @Override
    public String command() {
        return "/quote";
    }

    @Override
    public String handle(Message message) {
        ResourceReader rr = new ResourceReader();
        try {
            List<String> lines = rr.readAllLines("advancedQuotes.txt");
            return lines.get(random.nextInt(lines.size()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String description() {
        return "случайная цитата Магистра Йоды";
    } // magic string

    static class ResourceReader {
        public List<String> readAllLines(String fileName) throws Exception {
            try (var inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                 var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                return reader.lines().collect(Collectors.toList());
            }
        }
    }
}
