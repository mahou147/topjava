package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class JdbcValidationUtil<T> {
    public static final ValidatorFactory vf = Validation.buildDefaultValidatorFactory();

    public static final Validator validator = vf.getValidator();

    private static final Logger log = LoggerFactory.getLogger(JdbcValidationUtil.class);

    public void validate(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        for (ConstraintViolation<T> violation : violations) {
            log.info(violation.getMessage());
        }
    }
}
