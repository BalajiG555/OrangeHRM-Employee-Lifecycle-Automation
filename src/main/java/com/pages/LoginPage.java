package com.pages;

import com.config.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField =
            By.name("username");

    private final By passwordField =
            By.name("password");

    private final By loginButton =
            By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public DashboardPage login(
            String username,
            String password) {

        enterText(usernameField, username);
        enterText(passwordField, password);
        click(loginButton);

        return new DashboardPage(driver);
    }

    public DashboardPage loginWithAdminCredentials() {

        return login(
                ConfigReader.getRequired("username"),
                ConfigReader.getRequired("password")
        );
    }

    public DashboardPage loginWithEssCredentials() {

        return login(
                ConfigReader.getRequired("ess.username"),
                ConfigReader.getRequired("ess.password")
        );
    }
}