package com.jumio.callback.api.repository.user_attribute_verification;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;

public interface UserAttributeVerificationResultRepository {
    UserAttributeVerificationResult getUserAttributeResult(int userId, String attributeName);
}
