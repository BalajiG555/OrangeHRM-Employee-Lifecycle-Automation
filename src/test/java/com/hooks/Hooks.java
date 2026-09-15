package com.hooks;

import com.config.ConfigReader;
import com.context.TestContext;
import com.driver.DriverFactory;
import com.pages.PIMPage;
import com.utils.ScreenshotUtils;
import com.utils.TestLogger;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {

        TestLogger.info(
                Hooks.class,
                "Starting scenario: "
                        + scenario.getName()
        );

        DriverFactory.initializeDriver();

        addEnvironmentInformation(scenario);
    }

    @After
    public void tearDown(Scenario scenario) {

        try {

            if (scenario.isFailed()) {

                TestLogger.error(
                        Hooks.class,
                        "Scenario failed: "
                                + scenario.getName(),
                        new RuntimeException(
                                "Scenario execution failed."
                        )
                );

                ScreenshotUtils.attachScreenshot(
                        "Failure Screenshot"
                );
            }

            cleanupTestData();

        } finally {

            TestLogger.info(
                    Hooks.class,
                    "Closing browser for scenario: "
                            + scenario.getName()
            );

            TestContext.clearCurrent();

            DriverFactory.quitDriver();
        }
    }

    private void cleanupTestData() {

        TestContext context =
                TestContext.current();

        if (context == null
                || !context.isEmployeeCreated()
                || context.isEmployeeDeleted()) {

            return;
        }

        if (context.getEmployee() == null) {
            return;
        }

        try {

            WebDriver driver =
                    DriverFactory.getDriver();

            if (driver == null) {
                return;
            }

            String employeeId =
                    context.getEmployee()
                            .getEmployeeId();

            TestLogger.info(
                    Hooks.class,
                    "Cleaning up employee: "
                            + employeeId
            );

            PIMPage pimPage =
                    new PIMPage(driver);

            pimPage.navigateToEmployeeList();

            pimPage.searchByEmployeeId(
                    employeeId
            );

            if (pimPage.isEmployeePresent(employeeId)) {

                pimPage.deleteEmployee();

                context.markEmployeeDeleted();

                TestLogger.info(
                        Hooks.class,
                        "Employee cleanup completed: "
                                + employeeId
                );
            }

        } catch (Exception e) {

            TestLogger.error(
                    Hooks.class,
                    "Employee cleanup failed. " +
                            "Original scenario result will be preserved.",
                    e
            );
        }
    }

    private void addEnvironmentInformation(
            Scenario scenario) {

        Allure.parameter(
                "Environment",
                ConfigReader.getEnvironment()
        );

        Allure.parameter(
                "Browser",
                ConfigReader.get("browser")
        );

        Allure.parameter(
                "Java Version",
                System.getProperty("java.version")
        );

        Allure.parameter(
                "OS",
                System.getProperty("os.name")
        );

        Allure.parameter(
                "Scenario",
                scenario.getName()
        );

        Allure.parameter(
                "Tags",
                String.join(
                        ", ",
                        scenario.getSourceTagNames()
                )
        );
    }
}