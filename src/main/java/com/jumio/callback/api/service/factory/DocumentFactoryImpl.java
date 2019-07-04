package com.jumio.callback.api.service.factory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumio.callback.api.service.DocumentVerificationService;
import com.jumio.callback.api.service.document_processor.*;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

@Service
@Qualifier("documentFactory")
public class DocumentFactoryImpl implements DocumentFactory {

    @Autowired
    @Qualifier("idDocumentProcessor")
    IdDocumentProcessor idDocumentProcessor;

    @Autowired
    @Qualifier("otherDocumentProcessor")
    OtherDocumentProcessor otherDocumentProcessor;

    @Autowired
    @Qualifier("taxReturnDocumentProcessor")
    TaxReturnDocumentProcessor taxReturnDocumentProcessor;

    @Autowired
    @Qualifier("utilityBillDocumentProcessor")
    UtilityBillDocumentProcessor utilityBillDocumentProcessor;

    @Override
    public DocumentProcessor getDocumentProcessor(MultiValueMap<String, String> payload) {
        DocumentProcessor documentProcessor = null;
        if (payload.containsKey(JumioPayloadConstants.CALLBACK_TYPE)) {
            if (payload.containsKey(JumioPayloadConstants.ID_TYPE) && payload.get(JumioPayloadConstants.ID_TYPE) != null && !payload.get(JumioPayloadConstants.ID_TYPE).isEmpty()) {
                String idType = payload.get(JumioPayloadConstants.ID_TYPE).get(0);
                if (idType.equals(JumioPayloadConstants.ID_TYPE_PASSPORT) || idType.equals(JumioPayloadConstants.ID_TYPE_DRIVING_LICENSE)) {
                    return this.idDocumentProcessor;
                }
            }
        } else if (payload.containsKey(JumioPayloadConstants.DOC_TRANSACTION)) {
            if (payload.containsKey(JumioPayloadConstants.DOC_DOCUMENT)) {
                String documentPayload = payload.get(JumioPayloadConstants.DOC_DOCUMENT).get(0);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Map<String, String> documentPayloadMapper = mapper.readValue(documentPayload, Map.class);
                    String docType = documentPayloadMapper.get(JumioPayloadConstants.DOC_TYPE);
                    if (docType != null && !docType.isEmpty()) {
                        switch (docType) {
                            case JumioPayloadConstants.DOC_TYPE_UTILITY_BILL:
                                documentProcessor = this.utilityBillDocumentProcessor;
                                break;
                            case JumioPayloadConstants.DOC_TYPE_TAX_RETURN:
                                documentProcessor = this.taxReturnDocumentProcessor;
                                break;
                            case JumioPayloadConstants.DOC_TYPE_BANK_STATEMENT:
                                documentProcessor = this.otherDocumentProcessor;
                                break;
                            default:
                        }
                    }

                } catch (JsonMappingException ex) {

                } catch (JsonParseException ex) {

                } catch (IOException ex) {

                }
            }
        }
        return documentProcessor;
    }
}
