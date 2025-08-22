package com.example.telegram_bot.service;

import com.example.telegram_bot.model.ValCurs;
import jakarta.xml.bind.JAXBContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getUsdRate() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + today;

        String xml = restTemplate.getForObject(url, String.class);
        try {
            JAXBContext context = JAXBContext.newInstance(ValCurs.class);
            var unmarshaller = context.createUnmarshaller();
            ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(new StringReader(xml));

            return valCurs.getValutes().stream()
                    .filter(v -> "USD".equals(v.getCharCode()))
                    .findFirst()
                    .map(v -> String.format("Курс доллара: %s руб. за %d %s", v.getValue(), v.getNominal(), v.getCharCode()))
                    .orElse("Курс USD не найден");
        } catch (Exception e) {
            return "Ошибка при получении курса: " + e.getMessage();
        }
    }
}
