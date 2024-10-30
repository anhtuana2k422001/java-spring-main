package com.hoanhtuan.main_app_security.repository;

import com.google.gson.Gson;
import com.hoanhtuan.main_app_security.common.datasource.DBType;
import com.hoanhtuan.main_app_security.common.datasource.DataSourceSingleton;
import com.hoanhtuan.main_app_security.common.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Slf4j
@Repository
public class UserRepository {
    private static final Gson gson = new Gson();

    public UserInfo getUserLogin(String username) {
        UserInfo userInfo = new UserInfo();

        String callProcedure = "{ call USER_PACKAGE.GET_USER_LOGIN(?, ?, ?, ?) }";
        LOG.info("[username] = {}, [Query] = {}", username, callProcedure);

        // Lấy DataSource thông qua Singleton và Abstract Class
        try (Connection conn = DataSourceSingleton.getInstance(DBType.ORACLE, "SYSTEM").getConnection();
             CallableStatement cs = conn.prepareCall(callProcedure)) {

            cs.setString(1, username);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.REF_CURSOR);
            cs.execute();

            String code = cs.getString(2);
            String msg = cs.getString(3);

            // result REF_CURSOR
            try (ResultSet rs = (ResultSet) cs.getObject(4)) {
                if (rs != null && rs.next()) {
                    userInfo = new UserInfo(
                            rs.getString("USER_ID"),
                            rs.getString("USERNAME"),
                            rs.getString("EMAIL"),
                            rs.getString("FULL_NAME")
                    );
                }
            }

            // Log result UserInfo
            LOG.info("RSCode = {}, RSMessage = {}, UserInfo = {}", code, msg, gson.toJson(userInfo));

        } catch (Exception e) {
            LOG.info("Failed to execute callableStatement with exception {}", e.getMessage());
        }

        return userInfo;
    }

    public UserInfo getUserInfoFromPostgre(String username) {
        UserInfo userInfo = new UserInfo();

        String query = "SELECT id, username, password, email, registrationdate FROM dbapp.users WHERE username = ?";
        LOG.info("[username] = {}, [Query] = {}", username, query);

        try (Connection conn = DataSourceSingleton.getInstance(DBType.POSTGRESQL, "POSTGRES").getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userInfo = new UserInfo(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }

            // Log result UserInfo
            LOG.info("UserInfo = {}", gson.toJson(userInfo));

        } catch (Exception e) {
            LOG.info("Failed to execute query with exception {}", e.getMessage());
        }

        return userInfo;
    }
}
