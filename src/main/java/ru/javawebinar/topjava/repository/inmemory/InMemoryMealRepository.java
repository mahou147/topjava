package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counterMealId = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save Meal id={} and userId={}", meal, userId);
        if (meal.isNew() & userId != 0) {
            meal.setUserId(userId);
            meal.setId(counterMealId.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal with id={} and userId={}", id, userId);
        return repository.remove(id) != null && repository.get(id).getUserId() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal with id={} and userId={}", id, userId);
        if (userId != 0) {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        if (userId != 0)
            return repository.values()
                    .stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("getBetween startDate={} and endDate={} with userId={}", startDate, endDate, userId);
        if (userId != 0)
            return repository.values()
                    .stream()
                    .limit(ChronoUnit.DAYS.between(startDate, endDate))
                    .sorted(Collections.reverseOrder())
                    .collect(Collectors.toList());
        return Collections.emptyList();
    }
}

