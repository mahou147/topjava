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
import ru.javawebinar.topjava.util.JdbcValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final BeanPropertyRowMapper<Meal> MEAL_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

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
        JdbcValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            deleteRoles(user); //delete old roles only if update
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
        }
        insertRoles(user);
        return user;
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
    }

    //insert in DB
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
        List<User> users = jdbcTemplate.query("SELECT u.*, string_agg(ur.role, ',') as roles " +
                "FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE u.id=? GROUP BY u.id", rsExtractor, id);
        User user = DataAccessUtils.singleResult(users); //when try deleted/get not found tests, service not throws NFE, but NPE
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT u.*, string_agg(ur.role, ',') as roles FROM users u " +
                "LEFT JOIN user_roles ur on u.id = ur.user_id WHERE email=? GROUP BY u.id", rsExtractor, email);
        User user = DataAccessUtils.singleResult(users);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT u.*, string_agg(ur.role, ',') as roles FROM users u " +
                "LEFT JOIN user_roles ur on u.id = ur.user_id GROUP BY u.id", rsExtractor);
        return users;
    }

    public User getWithMeals(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, string_agg(ur.role, ',') as roles " +
                "FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE u.id=? GROUP BY u.id", rsExtractor, id);
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? ORDER BY date_time DESC", MEAL_ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setMeals(meals);
        }
        meals.forEach(meal -> meal.setUser(user));
        return user;
    }

    @Component
    public static class UserWithRolesExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> usersWithRoles = new HashMap<>();
            while (rs.next()) {
                User user = USER_ROW_MAPPER.mapRow(rs, rs.getRow());
                if (user.getRoles() == null) {
                    user.setRoles(Collections.emptyList());
                }
                usersWithRoles.merge(user.id(), user, (oldUser, newUser) -> {
                    oldUser.getRoles().addAll(newUser.getRoles());
                    return oldUser;
                });
            }
            return List.copyOf(usersWithRoles.values());
        }
    }
}
