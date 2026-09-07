package com.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(

        features =
                "src/test/java/com/feature/employee_lifecycle.feature",

        glue =
                {
                        "com.stepdefinitions",
                        "com.hooks"
                },

        plugin =
                {
                        "pretty",
                        "html:target/cucumber-reports/cucumber.html",
                        "json:target/cucumber-reports/cucumber.json",
                        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
                },

        monochrome = true,

        publish = false,

        tags = "@employeeLifecycle"

)
public class TestRunner
        extends AbstractTestNGCucumberTests {
}