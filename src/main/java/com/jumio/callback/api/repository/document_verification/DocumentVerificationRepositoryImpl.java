package com.jumio.callback.api.repository.document_verification;

import com.jumio.callback.api.model.document_processor.UserAttributeVerificationResult;
import com.jumio.callback.api.model.document_processor.UserDocumentData;
import com.jumio.callback.api.repository.user_attribute_verification.UserAttributeVerificationResultDbConstants;
import com.jumio.callback.api.repository.user_document_data.UserDocumentDataDbConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.util.Date;

@Repository
@Qualifier("documentVerificationRepository")
public class DocumentVerificationRepositoryImpl implements DocumentVerificationRepository {

    @PersistenceContext
    private EntityManager entityMgr;

    //@Transactional(rollbackOn = Exception.class)
    @Override
    public Boolean updateUserVerificationDocResultAndDocument(UserDocumentData userDocumentData, UserAttributeVerificationResult userAttributeVerificationResult) {
        int status;
        StoredProcedureQuery userAttributeResultQuery = entityMgr.createStoredProcedureQuery("p_verif_update_user_attribute_result");
        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, userAttributeVerificationResult.getUserId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ATTRIBUTE_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ATTRIBUTE_ID, userAttributeVerificationResult.getUserAttributeId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE_ID, userAttributeVerificationResult.getDocTypeId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_RESULT, Boolean.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_RESULT, userAttributeVerificationResult.getResult());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_NOTES, String.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_NOTES, userAttributeVerificationResult.getVerificationNotes());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_STATUS, Integer.class, ParameterMode.OUT);
        userAttributeResultQuery.execute();
        status = (int) userAttributeResultQuery.getOutputParameterValue(UserAttributeVerificationResultDbConstants.V_STATUS);
        if (status == 1) {
            return false;
        }

        StoredProcedureQuery userDocumentDataQuery = entityMgr.createStoredProcedureQuery("p_verif_update_user_document_data");
        userDocumentDataQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        userDocumentDataQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_USER_ID, userDocumentData.getUserId());

        userDocumentDataQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_DOC_TYPE_ID, Integer.class, ParameterMode.IN);
        userDocumentDataQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_DOC_TYPE_ID, userDocumentData.getDocTypeId());

        userDocumentDataQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_VERIFICATION_DATA, String.class, ParameterMode.IN);
        userDocumentDataQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_VERIFICATION_DATA, userDocumentData.getData());

        userDocumentDataQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_DOC_EXPIRATION_DATE, Date.class, ParameterMode.IN);
        userDocumentDataQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_DOC_EXPIRATION_DATE, userDocumentData.getExpirationDate());

        userDocumentDataQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_STATUS, Integer.class, ParameterMode.OUT);
        userDocumentDataQuery.execute();
        status = (int) userDocumentDataQuery.getOutputParameterValue(UserDocumentDataDbConstants.V_STATUS);
        if (status == 1) {
            return false;
        }

        return true;
    }
}
