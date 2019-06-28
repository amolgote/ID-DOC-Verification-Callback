package com.jumio.callback.api.repository.document_verification;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;

public interface DocumentVerificationRepository {
    Boolean updateUserVerificationDocResultAndDocument(UserDocumentData userDocumentData, UserAttributeVerificationResult userAttributeVerificationResult);
}
