package org.example.programming5project.converters;

import org.example.programming5project.domain.Gender;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGenderConverter implements Converter<String, Gender> {
    @Override
    public Gender convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Gender.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender value: " + source);
        }
    }
}
