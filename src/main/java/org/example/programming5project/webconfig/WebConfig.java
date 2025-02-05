package org.example.programming5project.webconfig;

import org.example.programming5project.converters.StringToDepartmentConverter;
import org.example.programming5project.converters.StringToGenderConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * class that customizes how Spring converts and formats data for specific types in the application (e.g., Department, Gender).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDepartmentConverter());
        registry.addConverter(new StringToGenderConverter());
    }
}
