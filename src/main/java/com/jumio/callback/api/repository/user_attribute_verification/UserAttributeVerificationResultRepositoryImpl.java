package com.jumio.callback.api.repository.user_attribute_verification;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataDbConstants;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.Date;
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
    public UserAttributeVerificationResult getUserAttributeResult(int userId, String attributeName, String docType) {
        List<UserAttributeVerificationResult> results;
        StoredProcedureQuery query = entityMgr.createStoredProcedureQuery("p_verif_get_user_attribute_result", UserAttributeVerificationResult.class);
        query.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        query.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, userId);

        query.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_ATTRIBUTE_NAME, String.class, ParameterMode.IN);
        query.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_ATTRIBUTE_NAME, attributeName);

        query.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE, String.class, ParameterMode.IN);
        query.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE, docType);

        query.execute();
        results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    public Boolean updateUserVerificationDocResult(UserAttributeVerificationResult userAttributeVerificationResult) {
        int status;
        StoredProcedureQuery storedProcedureQuery = entityMgr.createStoredProcedureQuery("p_verif_update_user_attribute_result");
        storedProcedureQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        storedProcedureQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, userAttributeVerificationResult.getUserId());

        storedProcedureQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ATTRIBUTE_ID, Integer.class, ParameterMode.IN);
        storedProcedureQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ATTRIBUTE_ID, userAttributeVerificationResult.getUserAttributeId());

        storedProcedureQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE_ID, Integer.class, ParameterMode.IN);
        storedProcedureQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE_ID, userAttributeVerificationResult.getDocTypeId());

        storedProcedureQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_RESULT, Boolean.class, ParameterMode.IN);
        storedProcedureQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_RESULT, userAttributeVerificationResult.getResult());

        storedProcedureQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_NOTES, String.class, ParameterMode.IN);
        storedProcedureQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_NOTES, userAttributeVerificationResult.getVerificationNotes());

        storedProcedureQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_STATUS, Integer.class, ParameterMode.OUT);
        storedProcedureQuery.execute();
        status = (int) storedProcedureQuery.getOutputParameterValue(UserAttributeVerificationResultDbConstants.V_STATUS);
        if (status == 1) {
            return false;
        }
        return true;
    }
}
