package com.jumio.callback.api.service.DocumentProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumio.callback.api.model.DocumentProcessor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.DocumentProcessor.UserDocumentData;
import com.jumio.callback.api.repository.UserAttributeVerification.UserAttributeVerificationResultRepository;
import com.jumio.callback.api.repository.UserDocumentData.UserDocumentDataRepository;
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

public class IdDocumentProcessor implements DocumentProcessor {

    @Autowired
    @Qualifier("userDocumentDataRepository")
    private UserDocumentDataRepository userDocumentDataRepository;

    @Autowired
    @Qualifier("userAttributeVerificationResultRepository")
    private UserAttributeVerificationResultRepository userAttributeVerificationResultRepository;

    @Override
    public void processDocument(MultiValueMap<String, String> payload) {
        if (payload.containsKey(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE)
                && payload.get(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE) != null) {
            String jumioIdScanReference = payload.get(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE).toString();
            if (jumioIdScanReference != null && !jumioIdScanReference.isEmpty()) {
                UserDocumentData userDocument = this.userDocumentDataRepository.getUserDocumentBasedOnScanRefNum(jumioIdScanReference);
                if (userDocument != null) {
                    UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.ID);
                    if (payload.containsKey(JumioPayloadConstants.VERIFICATION_STATUS) && payload.get(JumioPayloadConstants.VERIFICATION_STATUS) != null) {
                        String verificationStatus = payload.get(JumioPayloadConstants.VERIFICATION_STATUS).toString();
                        if (verificationStatus == JumioPayloadConstants.VERIFICATION_STATUS_APPROVED) {
                            userAttributeVerificationResult.setResult(true);
                        }
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String json = mapper.writeValueAsString(payload);
                        userDocument.setData(json);
                    }
                    catch(JsonProcessingException ex){

                    }

                }
            }
        } else {
            return;
        }
    }
}
