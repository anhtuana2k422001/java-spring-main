package com.hoanhtuan.main_app_security.repository.inf;

import com.hoanhtuan.main_app_security.common.entity.UserInfo;

public interface UserDAO {
    UserInfo getUserInfoByUsername(String username);
    UserInfo getUserInfoByUsernameV1(String username);
    UserInfo getUserLogin(String username);
}
