package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

public class AbstractMealController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private final MealService service;

    public AbstractMealController(MealService service) {
        this.service = service;
    }

    Meal create(Meal meal) {
        log.info("create {} for user {}", meal, meal.getUser());
        return service.create(meal, SecurityUtil.authUserId());
    }

    void update(Meal meal) {
        log.info("update {} for user {}", meal, meal.getUser());
        service.update(meal, SecurityUtil.authUserId());
    }

    void delete(int id) {
        log.info("delete meal {} for user {}", id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
    }

    Meal get(int id) {
        log.info("get meal {} for user {}", id, SecurityUtil.authUserId());
        return service.get(id, SecurityUtil.authUserId());
    }

    List<Meal> getAll() {
        log.info("getAll for user {}", SecurityUtil.authUserId());
        return service.getAll(SecurityUtil.authUserId());
    }

    List<Meal> getBetween(LocalDate startDate, LocalDate endDate) {
        log.info("getBetween ({} - {}) for user {}", startDate, endDate, SecurityUtil.authUserId());
        return service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId());
    }
}
