package com.hoanhtuan.main_app_security.service;

import com.hoanhtuan.main_app_security.common.entity.UserInfo;
import com.hoanhtuan.main_app_security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public UserInfo getUserLogin(String username) {
        return userRepository.getUserLogin(username);
    }

    public UserInfo getUserPostgres(String username) {
        return userRepository.getUserInfoFromPostgre(username);
    }
}
