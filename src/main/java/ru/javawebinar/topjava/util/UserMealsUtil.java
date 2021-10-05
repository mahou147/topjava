package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> allDayCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate day = meal.getDateTime().toLocalDate();
            int dayCalories = meal.getCalories();
            allDayCalories.merge(day, dayCalories, Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                LocalDate day = meal.getDateTime().toLocalDate();
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), caloriesPerDay, allDayCalories.get(day) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> allDayCalories;
        allDayCalories = meals.stream()
                .collect(Collectors.groupingBy(request -> request.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), caloriesPerDay, allDayCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static Collector<UserMeal, ?, List<UserMealWithExcess>> myCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return Collector.<UserMeal, Map.Entry<List<UserMeal>, Map<LocalDate, Integer>>, List<UserMealWithExcess>>of(
                () -> new AbstractMap.SimpleImmutableEntry<>(
                        new ArrayList<UserMeal>(), new HashMap<LocalDate, Integer>()),
                (entry, meal) -> entry.getValue().merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum),
                (entry1, entry2) -> {
                    entry1.getKey().addAll(entry2.getKey());
                    return entry1;
                },
                entry -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    meals.forEach(meal -> {
                        if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                            result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), caloriesPerDay, entry.getValue().get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
                        }
                    });
                    return result;
                }
        );
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(myCollector(meals, startTime, endTime, caloriesPerDay));
    }
}
