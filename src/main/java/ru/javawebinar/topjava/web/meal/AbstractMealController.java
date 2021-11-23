package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.util.List;

public class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private final MealService service;

    public AbstractMealController(MealService service) {
        this.service = service;
    }

    Meal create(Meal meal, int userId) {
        log.info("create {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    void update(Meal meal, int userId) {
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }

    void delete(int id, int userId) {
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
    }

    Meal get(int id, int userId) {
        log.info("get meal {} for user {}", id, userId);
        return service.get(id, userId);
    }

    List<Meal> getAll(int userId) {
        log.info("getAll for user {}", userId);
        return service.getAll(userId);
    }

    List<Meal> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("getBetween ({} - {}) for user {}", startDate, endDate, userId);
        return service.getBetweenInclusive(startDate, endDate, userId);
    }
}
