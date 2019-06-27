package com.jumio.callback.api.repository.UserDocumentData;

import com.jumio.callback.api.model.DocumentProcessor.UserDocumentData;

public interface UserDocumentDataRepository {
    UserDocumentData getUserDocumentBasedOnScanRefNum(String scanReferenceNumber);

    Boolean UpdateUserDocument(UserDocumentData userDocumentData);
}
