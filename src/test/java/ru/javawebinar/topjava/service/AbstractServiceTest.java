package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import ru.javawebinar.topjava.TimingRules;

public abstract class AbstractServiceTest {
    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;

    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;
}