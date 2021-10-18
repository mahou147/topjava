package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return service.getAll(userId);
    }

    public List<Meal> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        log.info("getBetween startDate={} and endDate={} with userId={}", startDate, endDate, userId);
        return service.getBetweenDates(startDate, endDate, userId);
    }

    public Meal get(int id, int userId) {
        log.info("get meal with id={} and userId={}", id, userId);
        return  service.get(id, userId);
    }

    public Meal create (Meal meal, int userId) {
        log.info("create {} with userId={}", meal, userId);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int userId) {
        log.info("update {} with id={}", meal, userId);
        service.update(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete meal with id={} and userId={}", id, userId);
        service.delete(id, userId);
    }
}