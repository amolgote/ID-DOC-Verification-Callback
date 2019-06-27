package com.jumio.callback.api.service;

import com.jumio.callback.api.service.DocumentProcessor.DocumentProcessor;
import com.jumio.callback.api.service.factory.DocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
@Qualifier("documentVerificationService")
public class DocumentVerificationServiceImpl implements DocumentVerificationService {

    @Autowired
    @Qualifier("documentFactory")
    private DocumentFactory documentFactory;

    public DocumentVerificationServiceImpl(){
    }

    @Override
    public void processDocument(MultiValueMap<String, String> payload) {
        DocumentProcessor docProcessor =  this.documentFactory.getDocumentProcessor(payload);
        docProcessor.processDocument(payload);
    }
}
