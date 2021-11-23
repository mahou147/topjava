package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.util.JdbcValidationUtil.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final String SQL_GET_ALL_USERS_WITH_ROLES = "SELECT DISTINCT * FROM users ORDER BY name, email";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<User>> roleExtractor;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ResultSetExtractor<List<User>> roleExtractor) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.roleExtractor = roleExtractor;
        this.validator = vf.getValidator();
    }


    @Override
    @Transactional
    public User save(User user) {
        validate(user, validator);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {

                return null;
            }
        }
        deleteRoles(user);
        insertRoles(user);
        return user;
    }

    @Override
    @Transactional
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
        user.setRoles(getRoles(user.id()));
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(SQL_GET_ALL_USERS_WITH_ROLES, USER_ROW_MAPPER);
        users.forEach(user -> user.setRoles(getRoles(user.id())));
        return users;
    }

    @Component
    public static class RoleExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<User> usersWithRoles = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                Set<Role> roles = EnumSet.of(Role.valueOf(rs.getString("role")));
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getTimestamp("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setRoles(roles);
                usersWithRoles.add(user);
            }
            return usersWithRoles;
        }
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
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

    private Set<Role> getRoles(int id) {
        Set<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", new ResultSetExtractor<Set<Role>>() {
            @Override
            public Set<Role> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Set<Role> roles = new HashSet<>();
                while (rs.next()) {
                    roles.add(Enum.valueOf(Role.class, rs.getString("role")));
                }
                return roles;
            }
        }, id);
        return roles;
    }
}
