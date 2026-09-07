package com.pages;

import com.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.config.ConfigReader;

public class LoginPage {

    private final WebDriver driver;

    private final By username =
            By.cssSelector("input[placeholder='Username']");

    private final By password =
            By.cssSelector("input[placeholder='Password']");

    private final By loginButton =
            By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public DashboardPage login(
            String username,
            String password) {

        driver.get(ConfigReader.get("base.url"));

        WaitUtils.waitForVisible(this.username)
                .sendKeys(username);

        WaitUtils.waitForVisible(this.password)
                .sendKeys(password);

        WaitUtils.waitForClickable(loginButton)
                .click();

        return new DashboardPage(driver);
    }
}