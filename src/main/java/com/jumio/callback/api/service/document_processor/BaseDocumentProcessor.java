package com.jumio.callback.api.service.document_processor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.model.user.User;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataRepository;
import com.jumio.callback.api.utils.DocumentAttributesConstant;
import com.jumio.callback.api.utils.DocumentTypeConstant;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

public class BaseDocumentProcessor {

    @Autowired
    @Qualifier("userDocumentDataRepository")
    protected UserDocumentDataRepository userDocumentDataRepository;

    public UserDocumentData GetUserDocument(MultiValueMap<String, String> payload, String scanReferenceNumberKey) {
        if (payload.containsKey(scanReferenceNumberKey)
                && payload.get(scanReferenceNumberKey) != null && !payload.get(scanReferenceNumberKey).isEmpty()) {
            String jumioIdScanReference = payload.get(scanReferenceNumberKey).get(0);
            if (jumioIdScanReference != null && !jumioIdScanReference.isEmpty()) {
                UserDocumentData userDocument = this.userDocumentDataRepository.getUserDocumentBasedOnScanRefNum(jumioIdScanReference);
                return userDocument;
            }
        }
        return null;
    }

    public Boolean performResidenceValidation(MultiValueMap<String, String> payload, User user, UserAttributeVerificationResult userAttributeVerificationResult) {
        Boolean result = false;
        if (userAttributeVerificationResult != null) {
            ObjectMapper mapper = new ObjectMapper();
            if (payload.containsKey(JumioPayloadConstants.DOC_TRANSACTION)) {
                String transactionPayload = payload.get(JumioPayloadConstants.DOC_TRANSACTION).get(0);
                if (transactionPayload != null && !transactionPayload.isEmpty()) {
                    try {
                        Map<String, String> transactionPayloadMapper = mapper.readValue(transactionPayload, Map.class);
                        String transactionStatus = transactionPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_TRANSACTION_STATUS);
                        if (transactionStatus.equals(JumioPayloadConstants.DOC_DOCUMENT_TRANSACTION_STATUS_DONE)) {
                            if (payload.containsKey(JumioPayloadConstants.DOC_DOCUMENT)) {
                                String documentPayload = payload.get(JumioPayloadConstants.DOC_DOCUMENT).get(0);
                                Map<String, Map> documentPayloadMapper = mapper.readValue(documentPayload, Map.class);
                                Map<String, Map> extractedData = documentPayloadMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA);
                                if (extractedData != null && !extractedData.isEmpty()) {
                                    Map<String, String> documentExtractedDataMapper = extractedData.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS);

                                    //String addressPayload = documentExtractedDataMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS);
                                    if (documentExtractedDataMapper != null && !documentExtractedDataMapper.isEmpty()) {
                                        String city = documentExtractedDataMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_CITY) != null ? documentExtractedDataMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_CITY).toLowerCase() : "";
                                        String zip = documentExtractedDataMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_POSTALCODE) != null ? documentExtractedDataMapper.get(JumioPayloadConstants.DOC_DOCUMENT_EXTRACTED_DATA_ADDRESS_POSTALCODE).toLowerCase() : "";
                                        if (user != null) {
                                            String userCity = user.getCity() != null ? user.getCity().toLowerCase() : "";
                                            String userZip = user.getZip() != null ? user.getZip().toLowerCase() : "";
                                            if (userCity.equals(city) && userZip.equals(zip)) {
                                                result = true;
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
        return result;
    }
}