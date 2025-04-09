package com.template.spring_backend_template.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public abstract class AbstractRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NamedParameterJdbcTemplate getRepositoryTemplate() {
        return namedParameterJdbcTemplate;
    }

    protected <T> T queryForObject(String sql, Map<String, ?> params, RowMapper<T> rowMapper) {
        try {
            return getRepositoryTemplate().queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Integer getFlag(boolean flag) {
        return flag ? 1 : 0;
    }
}
