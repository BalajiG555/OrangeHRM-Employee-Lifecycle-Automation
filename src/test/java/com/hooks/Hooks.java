package com.hooks;

import com.driver.DriverFactory;
import com.utils.ScreenshotUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUp() {

        DriverFactory.initializeDriver();
    }

    @After
    public void tearDown(
            Scenario scenario) {

        if (scenario.isFailed()) {

            ScreenshotUtils.attachScreenshot(
                    "Failure Screenshot"
            );
        }

        DriverFactory.quitDriver();
    }
}