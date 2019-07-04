package com.jumio.callback.api.service.document_processor;

import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataRepository;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.MultiValueMap;

public class BaseDocumentProcessor {

    @Autowired
    @Qualifier("userDocumentDataRepository")
    protected UserDocumentDataRepository userDocumentDataRepository;

    public UserDocumentData GetUserDocument(MultiValueMap<String, String> payload) {
        if (payload.containsKey(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE)
                && payload.get(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE) != null && !payload.get(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE).isEmpty()) {
            String jumioIdScanReference = payload.get(JumioPayloadConstants.JUMIO_ID_SCAN_REFERENCE).get(0);
            if (jumioIdScanReference != null && !jumioIdScanReference.isEmpty()) {
                UserDocumentData userDocument = this.userDocumentDataRepository.getUserDocumentBasedOnScanRefNum(jumioIdScanReference);
                return userDocument;
            }
        }
        return null;
    }
}