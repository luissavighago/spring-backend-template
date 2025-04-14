package com.template.spring_backend_template.repository.user.mapper;

import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.domain.user.UserRoleEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(UUID.fromString(rs.getString("id")));
        user.setName(rs.getString("name"));
        user.setLastname(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(UserRoleEnum.valueOf(rs.getString("role")));
        user.setFgActive(rs.getInt("fgactive") > 0);
        user.setCreatedAt(rs.getTimestamp("createdat").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updatedat").toLocalDateTime());

        return user;
    }
}
