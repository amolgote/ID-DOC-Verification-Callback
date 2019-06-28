package com.jumio.callback.api.service.document_processor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.model.user.User;
import com.jumio.callback.api.repository.document_verification.DocumentVerificationRepository;
import com.jumio.callback.api.repository.user.UserRepository;
import com.jumio.callback.api.repository.user_attribute_verification.UserAttributeVerificationResultRepository;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataRepository;
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

public class IdDocumentProcessor extends BaseDocumentProcessor implements DocumentProcessor {

    @Autowired
    @Qualifier("userDocumentDataRepository")
    private UserDocumentDataRepository userDocumentDataRepository;

    @Autowired
    @Qualifier("userAttributeVerificationResultRepository")
    private UserAttributeVerificationResultRepository userAttributeVerificationResultRepository;

    @Autowired
    @Qualifier("documentVerificationRepository")
    private DocumentVerificationRepository documentVerificationRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public void processDocument(MultiValueMap<String, String> payload) {
        UserDocumentData userDocument = super.GetUserDocument(payload);
        this.processIdVerification(payload, userDocument);
        this.processResidenceVerification(payload, userDocument);
    }

    private void processIdVerification(MultiValueMap<String, String> payload, UserDocumentData userDocument ){
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
            } catch (JsonProcessingException ex) {

            }
            Boolean result = this.documentVerificationRepository.updateUserVerificationDocResultAndDocument(userDocument, userAttributeVerificationResult);
            if (result) {

            } else {

            }
        }
    }

    private void processResidenceVerification(MultiValueMap<String, String> payload, UserDocumentData userDocument ){
        if (userDocument != null) {
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE);
            if (userAttributeVerificationResult != null){
                if (!userAttributeVerificationResult.getResult()){ //Check if the Residence User attribute is not validated
                    User user = this.userRepository.getUser(userDocument.getUserId());
                    if(payload.containsKey(JumioPayloadConstants.ID_ADDRESS)){
                        String address = payload.get(JumioPayloadConstants.ID_ADDRESS).toString();
                        if (address != null && !address.isEmpty()){
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                MultiValueMap<String, String> addressPayload = mapper.readValue(address, MultiValueMap.class);
                                String city = addressPayload.get(JumioPayloadConstants.ID_CITY) != null ? addressPayload.get(JumioPayloadConstants.ID_CITY).toString() : "";
                                String stateCode = addressPayload.get(JumioPayloadConstants.ID_STATE_CODE) != null ? addressPayload.get(JumioPayloadConstants.ID_STATE_CODE).toString() : "";
                                String zipCode = addressPayload.get(JumioPayloadConstants.ID_ZIP) != null ? addressPayload.get(JumioPayloadConstants.ID_ZIP).toString() : "";
                                String zipCodeExtension = addressPayload.get(JumioPayloadConstants.ID_ZIP_EXTENSION) != null ? addressPayload.get(JumioPayloadConstants.ID_ZIP_EXTENSION).toString() : "";

                            }
                            catch(JsonMappingException ex){

                            }
                            catch(JsonParseException ex){

                            }
                            catch(IOException ex){

                            }
                        }
                    }
                }
            }
        }
    }
}
