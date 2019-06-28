package com.jumio.callback.api.service.document_processor;

import com.jumio.callback.api.model.user.User;
import org.springframework.util.MultiValueMap;

public interface DocumentProcessor {
    void processDocument(MultiValueMap<String, String> payload);
    /*Boolean compareAddress(User user)*/
}
