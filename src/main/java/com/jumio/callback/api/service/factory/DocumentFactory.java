package com.jumio.callback.api.service.factory;

import com.jumio.callback.api.service.document_processor.DocumentProcessor;
import org.springframework.util.MultiValueMap;

public interface DocumentFactory {
    DocumentProcessor getDocumentProcessor(MultiValueMap<String, String> payload);
}
