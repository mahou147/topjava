package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Transactional
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final BeanPropertyRowMapper<Role> ROLE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Role.class);
    private static final String SQL_GET_ALL_USERS_WITH_ROLES = "SELECT DISTINCT * FROM users ORDER BY name, email";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    private final RoleExtractor roleExtractor;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, RoleExtractor roleExtractor) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.roleExtractor = roleExtractor;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0
                    && namedParameterJdbcTemplate.update("UPDATE user_roles SET role=:role" +
                    " WHERE user_id=:id", parameterSource) == 0) {
                return null;
            }
        }
        return user;
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role)  VALUES (?, ?)",
                roles,
                2,
                (ps, role) -> {
                    ps.setInt(1, user.getId());
                    ps.setString(2, role.name());
                });
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users JOIN user_roles " +
                "on users.id = user_roles.user_id WHERE id=?", roleExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", USER_ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        user.setRoles(jdbcTemplate.query("SELECT * FROM user_roles  WHERE user_id=?",ROLE_ROW_MAPPER, user.id()));
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(SQL_GET_ALL_USERS_WITH_ROLES, USER_ROW_MAPPER);
        users.forEach(user -> user.setRoles(jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, user.id())));
        return users;
    }
}
