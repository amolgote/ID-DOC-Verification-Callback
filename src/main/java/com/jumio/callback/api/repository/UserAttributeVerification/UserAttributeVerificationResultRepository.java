package com.jumio.callback.api.repository.UserAttributeVerification;

import com.jumio.callback.api.model.DocumentProcessor.UserAttributeVerificationResult;

public interface UserAttributeVerificationResultRepository {
    UserAttributeVerificationResult getUserAttributeResult(int userId, String attributeName);
    Boolean updateUserAttributeResult(UserAttributeVerificationResult userAttributeVerificationResult);

}
