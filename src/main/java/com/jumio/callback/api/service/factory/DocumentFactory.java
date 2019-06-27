package com.jumio.callback.api.service.factory;

import com.jumio.callback.api.service.DocumentProcessor.DocumentProcessor;
import org.springframework.util.MultiValueMap;

public interface DocumentFactory {
    DocumentProcessor getDocumentProcessor(MultiValueMap<String, String> payload);
}
