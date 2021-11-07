package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.topjava.TimingRules;

public abstract class AbstractServiceTest {

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
    }

    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;

    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;
}