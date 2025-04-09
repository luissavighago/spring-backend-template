package com.template.spring_backend_template.repository.user.sql;

public class UserSql {

    public static final String select_by_login = """
        SELECT
            ID,
            NAME,
            LASTNAME,
            EMAIL,
            PASSWORD,
            ROLE,
            FGACTIVE,
            CREATEDAT,
            UPDATEDAT
        FROM
            TB_USERS
        WHERE
            EMAIL = :login
    """;

    public static final String insert = """
        INSERT INTO TB_USERS (
            ID,
            NAME,
            LASTNAME,
            EMAIL,
            PASSWORD,
            ROLE,
            FGACTIVE
        ) VALUES (
            GEN_RANDOM_UUID(),
            :name,
            :lastname,
            :email,
            :password,
            :role,
            :fgactive
        );
    """;
}
