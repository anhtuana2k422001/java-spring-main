package com.hoanhtuan.main_app_security.common.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public abstract class AbstractDataSource {

    // Mỗi lớp con sẽ tự triển khai logic của nó để xác định UserDB
    protected abstract String getUserDB();

    // Phương thức dùng chung để tạo DataSource dựa trên các thông số được cung cấp
    protected DataSource createDataSource(DBType dbType,  String username, String password) {
        HikariConfig config = new HikariConfig();
        if (dbType == DBType.ORACLE){
            config.setJdbcUrl("jdbc:oracle:thin:@//localhost:1521/ORACLE");
            config.setDriverClassName("oracle.jdbc.OracleDriver");
        } else if(dbType == DBType.POSTGRESQL) {
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/dbapp");
            config.setDriverClassName("org.postgresql.Driver");
        } else {
            LOG.info("Unknown database type: {}", dbType);
            throw new IllegalArgumentException("Unsupported DBType: " + dbType);
        }
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

    // Phương thức chung để lấy DataSource tùy theo UserDB
    public DataSource getDataSource(DBType dbType) {
        String userDB = getUserDB();

        // Tùy chỉnh từng DataSource dựa trên UserDB
        if ("SYSTEM".equals(userDB)) {
            return createDataSource(dbType, "SYSTEM", "");
        } else if ("POSTGRES".equals(userDB)) {
            return createDataSource(dbType, "postgres", "");
        }
        throw new IllegalArgumentException("Unknown database: " + userDB);
    }
}
