package com.jumio.callback.api.repository.user_document_data;

import com.jumio.callback.api.model.document_processor.UserDocumentData;

public interface UserDocumentDataRepository {
    UserDocumentData getUserDocumentBasedOnScanRefNum(String scanReferenceNumber);

    Boolean UpdateUserDocument(UserDocumentData userDocumentData);
}
