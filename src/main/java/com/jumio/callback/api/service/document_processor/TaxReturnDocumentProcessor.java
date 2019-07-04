package com.jumio.callback.api.service.document_processor;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.model.user.User;
import com.jumio.callback.api.repository.user.UserRepository;
import com.jumio.callback.api.repository.user_attribute_verification.UserAttributeVerificationResultRepository;
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import com.jumio.callback.api.utils.DocumentTypeConstant;
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
        UserDocumentData userDocument = super.GetUserDocument(payload);
        if (userDocument != null) {
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.INCOME, DocumentTypeConstant.TAX_RETURN);
            if (userAttributeVerificationResult != null) {
                if (!userAttributeVerificationResult.getResult()) { //Check if the Residence User attribute is not validated
                    User user = this.userRepository.getUser(userDocument.getUserId());
                }
            }
        }
    }
}
