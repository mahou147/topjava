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
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.javawebinar.topjava.util.JdbcValidationUtil.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final BeanPropertyRowMapper<Meal> MEAL_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    private static final String SQL_GET_ALL_USERS_WITH_ROLES = "SELECT DISTINCT * FROM users ORDER BY name, email";
    private static final String SQL_GET_WITH_MEALS = "SELECT DISTINCT * FROM users WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<User>> rsExtractor;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ResultSetExtractor<List<User>> rsExtractor) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rsExtractor = rsExtractor;
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

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
    }

    private void insertRoles(User user) {
        Set<Role> roles = user.getRoles();
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role)  VALUES (?, ?)",
                roles,
                roles.size(),
                (ps, role) -> {
                    ps.setInt(1, user.getId());
                    ps.setString(2, role.name());
                });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users JOIN user_roles " +
                "ON users.id = user_roles.user_id WHERE id=?", rsExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", USER_ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setRoles(getRoles(user.id()));
        } else {
            throw new NotFoundException("User with " + email + "is not found");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(SQL_GET_ALL_USERS_WITH_ROLES, USER_ROW_MAPPER);
        users.forEach(user -> user.setRoles(getRoles(user.id())));
        return users;
    }

    private Set<Role> getRoles(int id) {
        return jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", rs -> {
            Set<Role> roles = new HashSet<>();
            while (rs.next()) {
                roles.add(Enum.valueOf(Role.class, rs.getString("role")));
            }
            return roles;
        }, id);
    }

    public User getWithMeals(int id) {
        List<User> users = jdbcTemplate.query(SQL_GET_WITH_MEALS, USER_ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setMeals(getMeals(id));
            user.setRoles(getRoles(id));
        } else {
            throw new NotFoundException("User " + id + "is not found");
        }
        return user;
    }

    private List<Meal> getMeals(int id) {
        return jdbcTemplate.query("SELECT meals.id, date_time, description, calories FROM meals JOIN users " +
                "ON users.id = meals.user_id WHERE user_id =? ORDER BY id DESC", rs -> {
            List<Meal> meals = new ArrayList<>();
            while (rs.next()) {
                meals.add(MEAL_ROW_MAPPER.mapRow(rs, rs.getRow()));
            }
            return meals;
        }, id);
    }

    @Component
    public static class UserWithRolesExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<User> usersWithRoles = new ArrayList<>();
            while (rs.next()) {
                User user = USER_ROW_MAPPER.mapRow(rs, rs.getRow());
                Set<Role> roles = EnumSet.of(Role.valueOf(rs.getString("role")));
                user.setRoles(roles);
                usersWithRoles.add(user);
            }
            return usersWithRoles;
        }
    }
}
