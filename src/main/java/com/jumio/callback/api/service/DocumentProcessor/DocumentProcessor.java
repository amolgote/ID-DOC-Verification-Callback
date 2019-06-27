package com.jumio.callback.api.service.DocumentProcessor;

import org.springframework.util.MultiValueMap;

public interface DocumentProcessor {
    void processDocument(MultiValueMap<String, String> payload);
}
