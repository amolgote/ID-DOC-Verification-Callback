package com.jumio.callback.api.repository.user_document_data;

import com.jumio.callback.api.model.document_processor.UserDocumentData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
@Qualifier("userDocumentDataRepository")
public class UserDocumentDataRepositoryImpl implements UserDocumentDataRepository {
    @PersistenceContext
    private EntityManager entityMgr;

    protected void setEntityManager(EntityManager entityManager) {
        this.entityMgr = entityManager;
    }

    @Override
    public UserDocumentData getUserDocumentBasedOnScanRefNum(String scanReferenceNumber) {
        List<UserDocumentData> results;
        StoredProcedureQuery query = entityMgr.createStoredProcedureQuery("p_verif_get_user_document", UserDocumentData.class);
        query.registerStoredProcedureParameter(UserDocumentDataDbConstants.V_PARAM_SCAN_REFERENCE_NUMBER, String.class, ParameterMode.IN);
        query.setParameter(UserDocumentDataDbConstants.V_PARAM_SCAN_REFERENCE_NUMBER, scanReferenceNumber);

        query.execute();
        results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public Boolean UpdateUserDocument(UserDocumentData userDocumentData) {
        return true;
    }
}
