package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.filterByPredicate(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY, meal -> true);
    }

    public List<MealTo> getBetweenDates(@Nullable LocalDateTime startDate, @Nullable LocalDateTime endDate, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("getBetween startDate={} and endDate={} startTime={} and endTime={}with userId={}", startDate, endDate, startTime, endTime, SecurityUtil.authUserId());
        return MealsUtil.getFilteredTos(service.getBetweenDates(startDate, endDate, SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY, startDate, endDate, startTime, endTime);
    }

    public Meal get(int id) {
        log.info("get meal with id={} and userId={}", id, SecurityUtil.authUserId());
        return  service.get(id, SecurityUtil.authUserId());
    }

    public Meal create (Meal meal) {
        log.info("create {} with userId={}", meal, SecurityUtil.authUserId());
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, SecurityUtil.authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete meal with id={} and userId={}", id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
    }
}