package com.jumio.callback.api.service.factory;

import com.jumio.callback.api.service.document_processor.*;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
@Qualifier("documentFactory")
public class DocumentFactoryImpl implements DocumentFactory {
    @Override
    public DocumentProcessor getDocumentProcessor(MultiValueMap<String, String> payload) {
        DocumentProcessor documentProcessor = null;
        if (payload.containsKey(JumioPayloadConstants.CALLBACK_TYPE)) {
            if (payload.containsKey(JumioPayloadConstants.ID_TYPE) && payload.get(JumioPayloadConstants.ID_TYPE) != null) {
                String idType = payload.get(JumioPayloadConstants.ID_TYPE).toString();
                if (idType.equals(JumioPayloadConstants.ID_TYPE_PASSPORT) || idType.equals(JumioPayloadConstants.ID_TYPE_DRIVING_LICENSE)) {
                    documentProcessor = new IdDocumentProcessor();
                }
            }
        } else if (payload.containsKey(JumioPayloadConstants.DOC_TRANSACTION)) {
            if (payload.containsKey(JumioPayloadConstants.DOC_TYPE) && payload.get(JumioPayloadConstants.DOC_TYPE) != null) {
                String docType = payload.get(JumioPayloadConstants.DOC_TYPE).toString();
                switch (docType) {
                    case JumioPayloadConstants.DOC_TYPE_UTILITY_BILL:
                        documentProcessor = new UtilityBillDocumentProcessor();
                        break;
                    case JumioPayloadConstants.DOC_TYPE_TAX_RETURN:
                        documentProcessor = new TaxReturnDocumentProcessor();
                        break;
                    case JumioPayloadConstants.DOC_TYPE_BANK_STATEMENT:
                        documentProcessor = new OtherDocumentProcessor();
                        break;
                    default:
                }
            }
        }
        return documentProcessor;
    }
}
