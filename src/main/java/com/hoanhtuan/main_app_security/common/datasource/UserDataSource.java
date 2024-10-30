package com.hoanhtuan.main_app_security.common.datasource;

public class UserDataSource extends  AbstractDataSource{
    private final String userDB;

    public UserDataSource(String userDB) {
        this.userDB = userDB;
    }

    @Override
    protected String getUserDB() {
        return this.userDB;
    }
}
