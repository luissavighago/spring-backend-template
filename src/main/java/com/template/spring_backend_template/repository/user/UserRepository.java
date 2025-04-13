package com.template.spring_backend_template.repository.user;

import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.repository.AbstractRepository;
import com.template.spring_backend_template.repository.user.mapper.UserRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.template.spring_backend_template.repository.user.sql.UserSql.insert;
import static com.template.spring_backend_template.repository.user.sql.UserSql.select_by_login;

@Repository
public class UserRepository extends AbstractRepository {

    public User findByLogin(String login) {

        Map<String, Object> params = new HashMap<>();
        params.put("login", login);

        return queryForObject(select_by_login, params, new UserRowMapper());
    }

    public void save(User user) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("lastname", user.getLastname());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("role", user.getRole().getRole());
        params.put("fgactive", getFlag(user.isFgActive()));

        getRepositoryTemplate().update(insert, params);
    }
}
