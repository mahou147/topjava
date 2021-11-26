package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest{

    //other realization - not at particular jpa service test class, but in abstract service test makes
    //@Autowired(required = false) - if not found in spring context - OK
    //public JpaUtil jpaUtil;
    //
    //    @Before
    //    public void setup() {
    //        cacheManager.getCache("users").clear();
    //        if(isJpeBased()) {
    //           jpaUtil.clear2ndLevelHibernateCache();}
    @Autowired
    public JpaUtil jpaUtil;

    @Before
    @Override
    public void setup() {
        super.setup();
        jpaUtil.clear2ndLevelHibernateCache();
    }
}
