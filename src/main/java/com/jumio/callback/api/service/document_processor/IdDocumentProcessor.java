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
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import com.jumio.callback.api.utils.DocumentTypeConstant;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

@Service
@Qualifier("idDocumentProcessor")
public class IdDocumentProcessor extends BaseDocumentProcessor implements DocumentProcessor {

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
        if (payload.containsKey(JumioPayloadConstants.VERIFICATION_STATUS) && payload.get(JumioPayloadConstants.VERIFICATION_STATUS) != null && !payload.get(JumioPayloadConstants.VERIFICATION_STATUS).isEmpty()) {
            String verificationStatus = payload.get(JumioPayloadConstants.VERIFICATION_STATUS).get(0);
            String thirdPartyIdType = "";
            String icwIdType = "";
            if (payload.containsKey(JumioPayloadConstants.ID_TYPE) && payload.get(JumioPayloadConstants.ID_TYPE) != null && !payload.get(JumioPayloadConstants.ID_TYPE).isEmpty()) {
                thirdPartyIdType = payload.get(JumioPayloadConstants.ID_TYPE).get(0);
                if (thirdPartyIdType.equals(JumioPayloadConstants.ID_TYPE_PASSPORT)) {
                    icwIdType = DocumentTypeConstant.PASSPORT;
                } else if (thirdPartyIdType.equals(JumioPayloadConstants.ID_TYPE_DRIVING_LICENSE)) {
                    icwIdType = DocumentTypeConstant.DRIVING_LICENSE;
                }
            }

            UserDocumentData userDocument = super.GetUserDocument(payload);
            this.processIdVerification(payload, userDocument, verificationStatus, icwIdType);
            this.processResidenceVerification(payload, userDocument, verificationStatus, icwIdType);
        }

    }

    private void processIdVerification(MultiValueMap<String, String> payload, UserDocumentData userDocument, String verificationStatus, String icwIdType) {
        if (userDocument != null) {
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.ID, icwIdType);
            if (verificationStatus.equals(JumioPayloadConstants.VERIFICATION_STATUS_APPROVED)) {
                userAttributeVerificationResult.setResult(true);
                userAttributeVerificationResult.setVerificationNotes(JumioPayloadConstants.VERIFICATION_STATUS_APPROVED);
            } else {
                userAttributeVerificationResult.setResult(false);
                userAttributeVerificationResult.setVerificationNotes(verificationStatus);
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

    private void processResidenceVerification(MultiValueMap<String, String> payload, UserDocumentData userDocument, String verificationStatus, String icwIdType) {
        if (userDocument != null && verificationStatus.equals(JumioPayloadConstants.VERIFICATION_STATUS_APPROVED)) {
            UserAttributeVerificationResult creditBureauResidenceVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE, DocumentTypeConstant.CREDIT_BUREAU_REPORT);
            if (creditBureauResidenceVerificationResult != null && !creditBureauResidenceVerificationResult.getResult()) { //Check if the Residence User attribute is not validated
                UserAttributeVerificationResult idResidenceVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE, icwIdType);
                if (idResidenceVerificationResult != null && !idResidenceVerificationResult.getResult()) {
                    User user = this.userRepository.getUser(userDocument.getUserId());
                    if (payload.containsKey(JumioPayloadConstants.ID_ADDRESS) && payload.get(JumioPayloadConstants.ID_ADDRESS) != null && !payload.get(JumioPayloadConstants.ID_ADDRESS).isEmpty()) {
                        String address = payload.get(JumioPayloadConstants.ID_ADDRESS).get(0);
                        if (address != null && !address.isEmpty()) {
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                Map<String, String> addressPayload = mapper.readValue(address, Map.class);
                                String city = addressPayload.get(JumioPayloadConstants.ID_CITY) != null ? addressPayload.get(JumioPayloadConstants.ID_CITY).toLowerCase() : "";
                                String stateCode = addressPayload.get(JumioPayloadConstants.ID_STATE_CODE) != null ? addressPayload.get(JumioPayloadConstants.ID_STATE_CODE).toLowerCase() : "";
                                String state = stateCode.replace("us-", "");
                                String zipCode = addressPayload.get(JumioPayloadConstants.ID_ZIP) != null ? addressPayload.get(JumioPayloadConstants.ID_ZIP).toLowerCase() : "";
                                String zipCodeExtension = addressPayload.get(JumioPayloadConstants.ID_ZIP_EXTENSION) != null ? addressPayload.get(JumioPayloadConstants.ID_ZIP_EXTENSION).toLowerCase() : "";
                                String zip = zipCode.concat(zipCodeExtension);


                                String userCity = user.getCity() != null ?  user.getCity().toLowerCase() : "";
                                String userState = user.getState() != null ?  user.getState().toLowerCase() : "";
                                String userZip = user.getZip() != null ?  user.getZip().toLowerCase() : "";
                                if (userCity.equals(city) && userState.equals(state) && userZip.equals(zip)){
                                    idResidenceVerificationResult.setResult(true);
                                    idResidenceVerificationResult.setVerificationNotes("Residence verified with " + icwIdType);
                                    Boolean updateResult = this.userAttributeVerificationResultRepository.updateUserVerificationDocResult(idResidenceVerificationResult);
                                    if (updateResult) {

                                    } else {

                                    }
                                }

                            } catch (JsonMappingException ex) {

                            } catch (JsonParseException ex) {

                            } catch (IOException ex) {

                            }
                        }
                    }
                }

            }
        }
    }
}
