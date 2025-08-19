package com.weatherapp.weatherdashboard.controller;

import com.weatherapp.weatherdashboard.model.WeatherResponse;
import com.weatherapp.weatherdashboard.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        try {
            if (city == null || city.trim().isEmpty()) {
                model.addAttribute("error", "Please enter a city name");
                return "index";
            }

            WeatherResponse weather = weatherService.getWeatherByCity(city.trim());

            if (weather != null) {
                // Add weather data to model
                model.addAttribute("city", weather.getName());
                model.addAttribute("country", weather.getSys().getCountry());
                model.addAttribute("temperature", Math.round(weather.getMain().getTemp()));
                model.addAttribute("description", weather.getWeather().get(0).getDescription());
                model.addAttribute("humidity", weather.getMain().getHumidity());
                model.addAttribute("windSpeed", weather.getWind().getSpeed());
                model.addAttribute("feelsLike", Math.round(weather.getMain().getFeels_like()));

                // Weather icon
                String iconCode = weather.getWeather().get(0).getIcon();
                model.addAttribute("weatherIcon", "http://openweathermap.org/img/wn/" + iconCode + "@2x.png");

                log.info("Weather data successfully processed for city: {}", city);
                return "weather";
            } else {
                model.addAttribute("error", "Weather data not found for " + city);
                return "index";
            }

        } catch (Exception e) {
            log.error("Error processing weather request for city: {}", city, e);
            model.addAttribute("error", "Unable to fetch weather data. Please try again.");
            return "index";
        }
    }
}
