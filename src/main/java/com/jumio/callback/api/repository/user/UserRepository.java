package com.jumio.callback.api.repository.user;

import com.jumio.callback.api.model.user.User;

public interface UserRepository {
    User getUser(int userId);
}
