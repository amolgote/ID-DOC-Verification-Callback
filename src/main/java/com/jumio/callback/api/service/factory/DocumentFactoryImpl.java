package com.jumio.callback.api.service.factory;
import com.jumio.callback.api.service.DocumentProcessor.DocumentProcessor;
import com.jumio.callback.api.service.DocumentProcessor.IdDocumentProcessor;
import com.jumio.callback.api.service.DocumentProcessor.OtherDocumentProcessor;
import com.jumio.callback.api.utils.JumioPayloadConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
@Qualifier("documentFactory")
public class DocumentFactoryImpl implements  DocumentFactory {
    @Override
    public DocumentProcessor getDocumentProcessor(MultiValueMap<String, String> payload) {
        DocumentProcessor documentProcessor = null;
        if (payload.containsKey(JumioPayloadConstants.CALLBACK_TYPE)){
            if (payload.containsKey(JumioPayloadConstants.ID_TYPE) && payload.get(JumioPayloadConstants.ID_TYPE) != null){
                String idType =  payload.get(JumioPayloadConstants.ID_TYPE).toString();
                if (idType.equals(JumioPayloadConstants.ID_TYPE_PASSPORT) || idType.equals(JumioPayloadConstants.ID_TYPE_DRIVING_LICENSE)){
                    documentProcessor = new IdDocumentProcessor();
                }
            }
        }
        else if(payload.containsKey(JumioPayloadConstants.OTHER_DOC_TRANSACTION)){
            documentProcessor = new OtherDocumentProcessor();
        }
        return documentProcessor;
    }
}
