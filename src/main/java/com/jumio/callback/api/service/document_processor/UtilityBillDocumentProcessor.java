package com.jumio.callback.api.service.document_processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.model.user.User;
import com.jumio.callback.api.repository.user.UserRepository;
import com.jumio.callback.api.repository.user_attribute_verification.UserAttributeVerificationResultRepository;
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

public class UtilityBillDocumentProcessor extends BaseDocumentProcessor implements DocumentProcessor  {
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
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE);
            if (userAttributeVerificationResult != null){
                if (!userAttributeVerificationResult.getResult()){ //Check if the Residence User attribute is not validated
                    User user = this.userRepository.getUser(userDocument.getUserId());
                    if(payload.containsKey(JumioPayloadConstants.ID_ADDRESS)){
                        String address = payload.get(JumioPayloadConstants.ID_ADDRESS).toString();
                        if (address != null && !address.isEmpty()){
                            ObjectMapper mapper = new ObjectMapper();
                        }
                    }
                }
            }
        }
    }
}
