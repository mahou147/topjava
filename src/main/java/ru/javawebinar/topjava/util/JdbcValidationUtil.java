package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.web.meal.MealRestController;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class JdbcValidationUtil {
    public static final ValidatorFactory vf = Validation.buildDefaultValidatorFactory();

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    public static void validate(Object target, Validator validator) {
        Set<ConstraintViolation<Object>> violations = validator.validate(target);
        for (ConstraintViolation<Object> violation : violations) {
            log.error(violation.getMessage());
        }
    }
}
