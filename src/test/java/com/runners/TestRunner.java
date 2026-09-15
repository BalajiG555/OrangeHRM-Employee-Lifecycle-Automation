package com.runners;

import com.listeners.FlakyTestListener;
import com.listeners.RetryListener;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.Listeners;

@Listeners({
        RetryListener.class,
        FlakyTestListener.class
})
@CucumberOptions(
        features = "src/test/java/com/feature",
        glue = {
                "com.stepdefinitions",
                "com.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        publish = false
)
public class TestRunner
        extends AbstractTestNGCucumberTests {
}