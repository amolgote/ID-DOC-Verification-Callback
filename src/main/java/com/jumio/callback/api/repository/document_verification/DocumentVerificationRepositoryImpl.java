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

@Repository
@Qualifier("documentVerificationRepository")
public class DocumentVerificationRepositoryImpl implements DocumentVerificationRepository {

    @PersistenceContext
    private EntityManager entityMgr;

    @Transactional
    @Override
    public Boolean updateUserVerificationDocResultAndDocument(UserDocumentData userDocumentData, UserAttributeVerificationResult userAttributeVerificationResult) {

        Boolean docVerificationResultUpdate = this.updateUserVerificationDocResult(userAttributeVerificationResult);
        if (docVerificationResultUpdate){
            return this.updateUserDocument(userDocumentData);
        }
        return true;
    }

    private Boolean updateUserVerificationDocResult(UserAttributeVerificationResult userAttributeVerificationResult) {

        StoredProcedureQuery userAttributeResultQuery = entityMgr.createStoredProcedureQuery("p_verif_update_user_attribute_result");
        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ID, userAttributeVerificationResult.getUserId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ATTRIBUTE_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_USER_ATTRIBUTE_ID, userAttributeVerificationResult.getUserAttributeId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_DOC_TYPE_ID, userAttributeVerificationResult.getDocTypeId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_PARAM_RESULT, Boolean.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserAttributeVerificationResultDbConstants.V_PARAM_RESULT, userAttributeVerificationResult.getResult());

        userAttributeResultQuery.registerStoredProcedureParameter(UserAttributeVerificationResultDbConstants.V_STATUS, Integer.class, ParameterMode.OUT);
        userAttributeResultQuery.execute();
        int status = (int) userAttributeResultQuery.getOutputParameterValue(UserAttributeVerificationResultDbConstants.V_STATUS);
        if (status == 1) {
            return false;
        }

        return true;
    }

    private Boolean updateUserDocument(UserDocumentData userDocumentData) {

        StoredProcedureQuery userAttributeResultQuery = entityMgr.createStoredProcedureQuery("p_verif_update_user_document_data");
        userAttributeResultQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_USER_ID, userDocumentData.getUserId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_DOC_TYPE_ID, Integer.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_DOC_TYPE_ID, userDocumentData.getDocTypeId());

        userAttributeResultQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_VERIFICATION_DATA, String.class, ParameterMode.IN);
        userAttributeResultQuery.setParameter(UserDocumentDataDbConstants.V_PARAM_VERIFICATION_DATA, userDocumentData.getData());

        userAttributeResultQuery.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_STATUS, Integer.class, ParameterMode.OUT);
        userAttributeResultQuery.execute();
        int status = (int) userAttributeResultQuery.getOutputParameterValue(UserDocumentDataDbConstants.V_STATUS);
        if (status == 1) {
            return false;
        }

        return true;
    }
}
