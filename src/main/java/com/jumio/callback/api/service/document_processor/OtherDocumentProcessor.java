package com.jumio.callback.api.service.document_processor;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.repository.user_attribute_verification.UserAttributeVerificationResultRepository;
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

public class OtherDocumentProcessor extends BaseDocumentProcessor implements DocumentProcessor  {
    @Autowired
    @Qualifier("userAttributeVerificationResultRepository")
    private UserAttributeVerificationResultRepository userAttributeVerificationResultRepository;

    @Override
    public void processDocument(MultiValueMap<String, String> payload) {
        UserDocumentData userDocument = super.GetUserDocument(payload);
        if (userDocument != null) {
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE);

        }
    }
}