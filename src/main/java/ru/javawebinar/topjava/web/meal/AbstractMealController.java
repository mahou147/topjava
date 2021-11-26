package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected final MealService service;

    public AbstractMealController(MealService service) {
        this.service = service;
    }

    Meal create(Meal meal) {
        log.info("create {} for user {}", meal, meal.getUser());
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    void update(Meal meal) {
        log.info("update {} for user {}", meal, meal.getUser());
        assureIdConsistent(meal, meal.id());
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

    List<MealTo> getAll() {
        log.info("getAll for user {}", SecurityUtil.authUserId());
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime, @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        log.info("getBetween ({} - {}) for user {}", startDate, endDate, SecurityUtil.authUserId());
        return MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate, endDate, SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime );
    }
}
