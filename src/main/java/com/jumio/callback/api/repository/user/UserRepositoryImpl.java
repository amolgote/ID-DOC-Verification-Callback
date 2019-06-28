package com.jumio.callback.api.repository.user;


import com.jumio.callback.api.model.user.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
@Qualifier("userRepository")
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityMgr;

    @Override
    public User getUser(int userId) {
        List<User> results;
        StoredProcedureQuery query = entityMgr.createStoredProcedureQuery("p_get_user_info", User.class);
        query.registerStoredProcedureParameter(UserDbConstants.V_PARAM_USER_ID, Integer.class, ParameterMode.IN);
        query.setParameter(UserDbConstants.V_PARAM_USER_ID, userId);
        query.execute();
        results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
}
