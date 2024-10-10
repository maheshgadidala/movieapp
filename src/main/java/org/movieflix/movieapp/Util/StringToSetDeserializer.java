package org.movieflix.movieapp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StringToSetDeserializer extends JsonDeserializer<Set<String>> {
    @Override
    public Set<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}