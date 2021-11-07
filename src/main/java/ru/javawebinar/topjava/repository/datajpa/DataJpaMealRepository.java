package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort ALL_SORTED = Sort.by(Sort.Direction.DESC, "date_time");

    private final CrudMealRepository crudMealRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository) {
        this.crudMealRepository = crudRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return crudMealRepository.save(meal, userId);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.findById(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAll(ALL_SORTED);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getAllByDateIsBetween(startDateTime, endDateTime, userId);
    }
}
