package com.jumio.callback.api.repository.user_attribute_verification;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
@Qualifier("userAttributeVerificationResultRepository")
public class UserAttributeVerificationResultRepositoryImpl implements UserAttributeVerificationResultRepository {

    @Autowired
    @Qualifier("userDocumentDataRepository")
    private UserDocumentDataRepository userDocumentDataRepository;

    @PersistenceContext
    private EntityManager entityMgr;

    @Override
    public UserAttributeVerificationResult getUserAttributeResult(int userId, String attributeName) {
        List<UserAttributeVerificationResult> results;
        StoredProcedureQuery query = entityMgr.createStoredProcedureQuery("p_get_user_attribute_result", UserAttributeVerificationResult.class);
        query.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        query.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, userId);

        query.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_ATTRIBUTE_NAME, Integer.class, ParameterMode.IN);
        query.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_ATTRIBUTE_NAME, attributeName);

        query.execute();
        results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
}
