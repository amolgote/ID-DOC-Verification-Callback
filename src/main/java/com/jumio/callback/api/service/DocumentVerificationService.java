package com.jumio.callback.api.service;
import org.springframework.util.MultiValueMap;

public interface DocumentVerificationService {
    void processDocument(MultiValueMap<String, String> payload);
}