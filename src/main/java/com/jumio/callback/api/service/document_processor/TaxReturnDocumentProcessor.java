package com.jumio.callback.api.service.document_processor;
import com.jumio.callback.api.repository.user.UserRepository;
import com.jumio.callback.api.repository.user_attribute_verification.UserAttributeVerificationResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
@Qualifier("TaxReturnDocumentProcessor")
public class TaxReturnDocumentProcessor extends BaseDocumentProcessor implements DocumentProcessor {
    @Autowired
    @Qualifier("userAttributeVerificationResultRepository")
    private UserAttributeVerificationResultRepository userAttributeVerificationResultRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public void processDocument(MultiValueMap<String, String> payload) {

    }
}
