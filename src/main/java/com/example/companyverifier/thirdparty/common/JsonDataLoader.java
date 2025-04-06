package com.example.companyverifier.thirdparty.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class JsonDataLoader {

    private final ObjectMapper objectMapper;

    public <T> T load(String resourcePath, TypeReference<T> typeRef) throws IOException {
        InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();
        return objectMapper.readValue(inputStream, typeRef);
    }
}
