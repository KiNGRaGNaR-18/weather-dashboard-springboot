package com.weatherapp.weatherdashboard.service;

import com.weatherapp.weatherdashboard.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class WeatherService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.url}")
    private String apiUrl;

    public WeatherResponse getWeatherByCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

            log.info("Making API call to: {}", url.replace(apiKey, "***"));

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            log.info("Successfully retrieved weather data for city: {}", city);

            return response;
        } catch (RestClientException e) {
            log.error("Error fetching weather data for city: {}", city, e);
            throw new RuntimeException("Unable to fetch weather data for " + city, e);
        }
    }
}
