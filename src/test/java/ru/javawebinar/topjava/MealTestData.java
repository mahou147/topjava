package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int ID_USER_MEAL = 100002;
    public static final int ID_ADMIN_MEAL = 100003;

    public static final Meal user_meal_example = new Meal(100002, LocalDateTime.of(
            2020, 12, 19, 10, 7, 4), "User Breakfast", 416);
    public static final Meal admin_meal_example = new Meal(100003, LocalDateTime.of(
            2021, 7, 11, 6, 51, 10), "Admin Breakfast", 2128);
    public static final Meal meal1 = new Meal(100004, LocalDateTime.of(
            2020, 11, 12, 5, 31, 33), "User Afternoon snack", 1084);
    public static final Meal meal2 = new Meal(100005, LocalDateTime.of(
            2021, 4, 15, 7, 37, 50), "User Afternoon snack", 1907);
    public static final Meal meal3 = new Meal(100006, LocalDateTime.of(
            2021, 3, 12, 10, 28, 8), "User Breakfast", 637);
    public static final Meal meal4 = new Meal(100007, LocalDateTime.of(
            2021, 4, 27, 17, 25, 54), "Admin Lunch", 1021);
    public static final Meal meal5 = new Meal(100008, LocalDateTime.of(
            2020, 11, 6, 20, 52, 52), "Admin Afternoon snack", 1016);
    public static final Meal meal6 = new Meal(100009, LocalDateTime.of(
            2021, 3, 23, 13, 29, 11), "Admin Lunch", 1488);
    public static final Meal meal7 = new Meal(100010, LocalDateTime.of(
            2021, 3, 24, 18, 5, 52), "Admin Breakfast", 1885);
    public static final Meal meal8 = new Meal(100011, LocalDateTime.of(
            2021, 7, 31, 5, 1, 50), "User Dinner", 1806);
    public static final Meal meal9 = new Meal(100012, LocalDateTime.of(
            2021, 4, 2, 3, 11, 45), "User Lunch", 2141);
    public static final Meal meal10 = new Meal(100013, LocalDateTime.of(
            2020, 11, 4, 16, 57, 43), "User Lunch", 1516);
    public static final Meal meal11 = new Meal(100014, LocalDateTime.of(
            2021, 9, 2, 14, 8, 47), "Admin Afternoon snack", 1223);
    public static final Meal meal12 = new Meal(100015, LocalDateTime.of(
            2021, 3, 21, 18, 27, 0), "Admin Dinner", 1151);
    public static final Meal meal13 = new Meal(100016, LocalDateTime.of(
            2021, 6, 21, 3, 23, 57), "Admin Dinner", 2311);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(
                2021, 10, 27, 17, 23, 0), "User Dinner", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(user_meal_example);
        updated.setDateTime(LocalDateTime.of(2021, 10, 21, 7, 7, 7));
        updated.setDescription("description");
        updated.setCalories(777);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
