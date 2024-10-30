package com.hoanhtuan.main_app_security.controller;

import com.google.gson.Gson;
import com.hoanhtuan.main_app_security.common.entity.UserInfo;
import com.hoanhtuan.main_app_security.model.UserInfoReq;
import com.hoanhtuan.main_app_security.service.UserService;
import com.hoanhtuan.main_app_security.utils.Handle;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private static  final Gson gson = new Gson();
    private UserService userService;

    @PostMapping("/login-info")
    public UserInfo getUserLogin(@RequestBody UserInfoReq userInfoReq) {
        Handle.addRequestId(userInfoReq.getRequestId());
        LOG.info("Request body api = {}", gson.toJson(userInfoReq));
        return userService.getUserLogin(userInfoReq.getUserName());
    }

    @PostMapping("/login-info-postgres")
    public UserInfo getUserLoginPostgres(@RequestBody UserInfoReq userInfoReq) {
        Handle.addRequestId(userInfoReq.getRequestId());
        LOG.info("Request body api = {}", gson.toJson(userInfoReq));
        return userService.getUserPostgres(userInfoReq.getUserName());
    }


}
