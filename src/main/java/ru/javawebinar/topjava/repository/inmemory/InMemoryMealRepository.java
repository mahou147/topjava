package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
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
        if (meal.isNew()) {
            meal.setId(counterMealId.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (meal.getUserId() == userId) return repository.computeIfAbsent(meal.getId(), id -> meal);
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal with id={} and userId={}", id, userId);
        return repository.remove(id).getUserId() == userId;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal with id={} and userId={}", id, userId);
        if (repository.get(id).getUserId() == userId) {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        if (repository.isEmpty()) return Collections.emptyList();
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        log.info("getBetween startDate={} and endDate={} with userId={}", startDateTime, endDateTime, userId);
        if (repository.isEmpty()) return Collections.emptyList();
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> (meal.getDateTime().toLocalDate().isAfter(startDateTime.toLocalDate())
                        || meal.getDateTime().toLocalDate().isEqual(startDateTime.toLocalDate()))
                        && (meal.getDateTime().toLocalDate().isBefore(endDateTime.toLocalDate()))
                        || meal.getDateTime().toLocalDate().isEqual(endDateTime.toLocalDate()))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

