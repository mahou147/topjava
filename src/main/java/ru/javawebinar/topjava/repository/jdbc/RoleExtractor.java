package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class RoleExtractor implements ResultSetExtractor<List<User>> {
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
