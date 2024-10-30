package com.hoanhtuan.main_app_security.repository;

import com.google.gson.Gson;
import com.hoanhtuan.main_app_security.common.entity.UserInfo;
import com.hoanhtuan.main_app_security.repository.inf.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Optional;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;
    private static final Gson gson = new Gson();

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public UserInfo getUserInfoByUsername(String username) {
        String sql = "SELECT user_id, username, email, full_name FROM user_info WHERE username = ?";

        return jdbcTemplate.query(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, username);
                    return ps;
                },
                (rs, rowNum) -> new UserInfo(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("full_name")
                )
        ).stream().findFirst().orElse(null);
    }

    @Override
    public UserInfo getUserInfoByUsernameV1(String username) {
        String sql = "SELECT user_id, username, email, full_name FROM user_info WHERE username = :username";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("username", username);

        return namedParameterJdbcTemplate.query(sql, parameters, (rs, rowNum) -> new UserInfo(
                rs.getString("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("full_name")
        )).stream().findFirst().orElse(null);
    }


    public UserInfo getUserLogin(String username) {
        String callProcedure = "{ call USER_PACKAGE.GET_USER_LOGIN(?, ?, ?, ?) }";

        return jdbcTemplate.execute(callProcedure, (CallableStatementCallback<UserInfo>) cs -> {
            cs.setString(1, username);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.REF_CURSOR);
            cs.execute();

            String code = cs.getString(2);
            String msg = cs.getString(3);
            UserInfo userInfo = null; // Khởi tạo biến userInfo

            // Result REF_CURSOR
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

            return null; // hoặc xử lý kết quả theo cách khác
        });
    }

    public UserInfo getUserInfoFromPostgres(String username) {
        UserInfo userInfo = new UserInfo();
        String query = "SELECT id, username, password, email FROM dbapp.users WHERE username = :username"; // Sử dụng tham số named

        LOG.info("[username] = {}, [Query] = {}", username, query);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("username", username);

        try {
            userInfo = namedParameterJdbcTemplate.queryForObject(query, parameters, (rs, rowNum) -> new UserInfo(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
            ));
        } catch (Exception e) {
            LOG.info("Failed to execute query with exception {}", e.getMessage());
        }

        // Log result UserInfo
        LOG.info("UserInfo = {}", gson.toJson(userInfo));

        return userInfo;
    }

    public Optional<UserInfo> getUserInfoFromPostgre(String username) {
        String query = "SELECT id, username, password, email FROM dbapp.users WHERE username = ?"; // Sử dụng tham số ?

        LOG.info("[username] = {}, [Query] = {}", username, query);

        try {
            // Sử dụng query với Optional
            return jdbcTemplate.query(query, new Object[]{username}, (rs, rowNum) -> new UserInfo(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
            )).stream().findFirst(); //
        } catch (Exception e) {
            LOG.info("Failed to execute query with exception {}", e.getMessage());
            return Optional.empty(); // Trả về Optional rỗng nếu có lỗi
        }
    }
}
