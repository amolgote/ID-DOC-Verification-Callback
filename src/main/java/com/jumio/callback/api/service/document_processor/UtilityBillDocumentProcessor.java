package com.jumio.callback.api.service.document_processor;
import com.fasterxml.jackson.core.JsonProcessingException;
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


@Service
@Qualifier("utilityBillDocumentProcessor")
public class UtilityBillDocumentProcessor extends BaseDocumentProcessor implements DocumentProcessor {
    @Autowired
    @Qualifier("userAttributeVerificationResultRepository")
    private UserAttributeVerificationResultRepository userAttributeVerificationResultRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("documentVerificationRepository")
    private DocumentVerificationRepository documentVerificationRepository;


    @Override
    public void processDocument(MultiValueMap<String, String> payload) {
        UserDocumentData userDocument = super.GetUserDocument(payload, JumioPayloadConstants.JUMIO_DOC_SCAN_REFERENCE);
        if (userDocument != null) {
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE, DocumentTypeConstant.UTILITY_BILL);
            if (userAttributeVerificationResult != null) {
                if (!userAttributeVerificationResult.getResult()) { //Check if the Residence User attribute is not validated
                    User user = this.userRepository.getUser(userDocument.getUserId());
                    if (user != null) {
                        Boolean verificationResult = super.performResidenceValidation(payload, user, userAttributeVerificationResult);
                        if (verificationResult) {
                            userAttributeVerificationResult.setResult(true);
                            userAttributeVerificationResult.setVerificationNotes("Residence verified with " + DocumentTypeConstant.UTILITY_BILL);
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
                }
            }
        }
        /*if (userDocument != null) {
            ObjectMapper mapper = new ObjectMapper();
            UserAttributeVerificationResult userAttributeVerificationResult = this.userAttributeVerificationResultRepository.getUserAttributeResult(userDocument.getUserId(), DocumentAttributesConstant.RESIDENCE, DocumentTypeConstant.UTILITY_BILL);
            if (userAttributeVerificationResult != null) {
                if (!userAttributeVerificationResult.getResult()) { //Check if the Residence User attribute is not validated
                    if (payload.containsKey(JumioPayloadConstants.DOC_TRANSACTION)) {
                        String transactionPayload = payload.get(JumioPayloadConstants.DOC_TRANSACTION).get(0);
                        if (transactionPayload != null && !transactionPayload.isEmpty()) {
                            try {
                                Map<String, String> transactionPayloadMapper = mapper.readValue(transactionPayload, Map.class);
                                String transactionStatus = transactionPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_TRANSACTION_STATUS);
                                if (transactionStatus.equals(JumioPayloadConstants.DOC_DOCUMENT_TRANSACTION_STATUS_DONE)) {
                                    if (payload.containsKey(JumioPayloadConstants.DOC_DOCUMENT)) {
                                        String documentPayload = payload.get(JumioPayloadConstants.DOC_DOCUMENT).get(0);
                                        Map<String, String> documentPayloadMapper = mapper.readValue(documentPayload, Map.class);
                                        String extractedData = documentPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA);
                                        if (extractedData != null && !extractedData.isEmpty()) {
                                            Map<String, String> documentExtractedDataMapper = mapper.readValue(extractedData, Map.class);
                                            String addressPayload = documentExtractedDataMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS);
                                            if (addressPayload != null && !addressPayload.isEmpty()) {
                                                Map<String, String> addressPayloadMapper = mapper.readValue(addressPayload, Map.class);
                                                String city = addressPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_CITY) != null ? addressPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_CITY).toLowerCase() : "";
                                                String zip = addressPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_POSTALCODE) != null ? addressPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_POSTALCODE).toLowerCase() : "";
                                                User user = this.userRepository.getUser(userDocument.getUserId());
                                                if (user != null) {
                                                    String userCity = user.getCity() != null ? user.getCity().toLowerCase() : "";
                                                    String userZip = user.getZip() != null ? user.getZip().toLowerCase() : "";
                                                    if (userCity.equals(city) && userZip.equals(zip)) {
                                                        userAttributeVerificationResult.setResult(true);
                                                        userAttributeVerificationResult.setVerificationNotes("Residence verified with " + DocumentTypeConstant.UTILITY_BILL);
                                                        Boolean updateResult = this.userAttributeVerificationResultRepository.updateUserVerificationDocResult(userAttributeVerificationResult);
                                                        if (updateResult) {

                                                        } else {

                                                        }
                                                    }
                                                }
                                            }
                                        }
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
        }*/
    }
}
