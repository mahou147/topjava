package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int ID_USER_MEAL = START_SEQ + 2;
    public static final int ID_ADMIN_MEAL = START_SEQ +3;

    public static final Meal user_meal_1 = new Meal(START_SEQ + 2, LocalDateTime.of(
            2019, 12, 19, 10, 7, 4), "User Breakfast", 416);
    public static final Meal admin_meal_1 = new Meal(START_SEQ + 3, LocalDateTime.of(
            2021, 7, 11, 6, 51, 10), "Admin Breakfast", 2128);
    public static final Meal user_meal_2 = new Meal(START_SEQ + 4, LocalDateTime.of(
            2019, 11, 12, 5, 31, 33), "User Afternoon snack", 1084);
    public static final Meal user_meal_3 = new Meal(START_SEQ + 5, LocalDateTime.of(
            2021, 4, 15, 7, 37, 50), "User Afternoon snack", 1907);
    public static final Meal user_meal_4 = new Meal(START_SEQ + 6, LocalDateTime.of(
            2021, 3, 12, 10, 28, 8), "User Breakfast", 637);
    public static final Meal admin_meal_2 = new Meal(START_SEQ + 7, LocalDateTime.of(
            2021, 4, 27, 17, 25, 54), "Admin Lunch", 1021);
    public static final Meal admin_meal_3 = new Meal(START_SEQ + 8, LocalDateTime.of(
            2020, 11, 6, 20, 52, 52), "Admin Afternoon snack", 1016);
    public static final Meal admin_meal_4 = new Meal(START_SEQ + 9, LocalDateTime.of(
            2021, 3, 23, 13, 29, 11), "Admin Lunch", 1488);
    public static final Meal admin_meal_5 = new Meal(START_SEQ + 10, LocalDateTime.of(
            2021, 3, 24, 18, 5, 52), "Admin Breakfast", 1885);
    public static final Meal user_meal_5 = new Meal(START_SEQ + 11, LocalDateTime.of(
            2021, 7, 31, 5, 1, 50), "User Dinner", 1806);
    public static final Meal user_meal_6 = new Meal(START_SEQ + 12, LocalDateTime.of(
            2021, 4, 2, 3, 11, 45), "User Lunch", 2141);
    public static final Meal user_meal_7 = new Meal(START_SEQ + 13, LocalDateTime.of(
            2019, 11, 4, 16, 57, 43), "User Lunch", 1516);
    public static final Meal admin_meal_6 = new Meal(START_SEQ + 14, LocalDateTime.of(
            2021, 9, 2, 14, 8, 47), "Admin Afternoon snack", 1223);
    public static final Meal admin_meal_7 = new Meal(START_SEQ + 15, LocalDateTime.of(
            2021, 3, 21, 18, 27, 0), "Admin Dinner", 1151);
    public static final Meal meal_at_the_boundary_value = new Meal(START_SEQ + 16, LocalDateTime.of(
            2020, 1, 31, 0, 0, 0), "User Meal at the boundary value", 2311);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(
                2021, 10, 27, 17, 23, 0), "User Dinner", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(user_meal_1);
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
