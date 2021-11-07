package ru.javawebinar.topjava;

import org.springframework.util.ClassUtils;

public class Profiles {
    public static final String
            JDBC = "jdbc",
            JPA = "jpa",
            DATAJPA = "datajpa";

    public static final String REPOSITORY_IMPLEMENTATION = DATAJPA;

    public static final String
            POSTGRES_DB = "postgres",
            HSQL_DB = "hsqldb";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        if (ClassUtils.isPresent("org.postgresql.Driver", null)) {
            return POSTGRES_DB;
        } else if (ClassUtils.isPresent("org.hsqldb.jdbcDriver", null)) {
            return HSQL_DB;
        } else {
            throw new IllegalStateException("Could not find DB driver");
        }
    }

    public static String getActiveRepositoryProfile() {
        if (ClassUtils.isPresent("org.springframework.jdbc.core.JdbcTemplate", null)) {
            return JDBC;
        } else if (ClassUtils.isPresent("org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean", null)) {
            return JPA;
        } else if (ClassUtils.isPresent("org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean", null)) {
            return JPA + DATAJPA;
        } else {
            throw new IllegalStateException("Could not find DB driver");
        }
    }
}
