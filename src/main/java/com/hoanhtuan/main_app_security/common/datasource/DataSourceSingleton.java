package com.hoanhtuan.main_app_security.common.datasource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class DataSourceSingleton {

    // Dùng Map để lưu trữ và quản lý các DataSource cho từng userDB
    private static final Map<String, DataSource> dataSourceMap = new HashMap<>();

    // Phương thức lấy DataSource dựa trên tên userDB
    public static DataSource getInstance(DBType dbType, String userDB) {

        // Nếu DataSource đã tồn tại cho userDB thì trả về, nếu chưa thì tạo mới
        if (dataSourceMap.containsKey(userDB)) {
            return dataSourceMap.get(userDB);
        }

        UserDataSource dataSourceProvider = new UserDataSource(userDB);
        DataSource dataSource = dataSourceProvider.getDataSource(dbType);
        dataSourceMap.put(userDB, dataSource);

        return dataSourceMap.get(userDB);
    }

    public static void intDataSource(DBType dbType, String userDB) {
        UserDataSource dataSourceProvider = new UserDataSource(userDB);
        DataSource dataSource = dataSourceProvider.getDataSource(dbType);
        dataSourceMap.put(userDB, dataSource);
    }

}
