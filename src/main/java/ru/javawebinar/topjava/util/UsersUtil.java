package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User(null,"User 1", "user1@gmail.com", "12345", 555, true, EnumSet.of(Role.USER)),
            new User(null,"User 2", "user5@gmail.com", "12346", 666, true, EnumSet.of(Role.USER)),
            new User(null,"User 3", "user3@gmail.com", "12348", 777, true, EnumSet.of(Role.USER)),
            new User(null,"User 2", "user4@gmail.com", "12346", 666, true, EnumSet.of(Role.USER)),
            new User(null,"User 2", "user2@gmail.com", "12346", 666, true, EnumSet.of(Role.USER))
    );
}
