package com.pages;

import com.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage {

    private final WebDriver driver;

    private final By dashboardHeader =
            By.xpath("//h6[normalize-space()='Dashboard']");

    private final By userDropdown =
            By.cssSelector(".oxd-userdropdown-name");

    private final By logoutLink =
            By.xpath("//a[normalize-space()='Logout']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isDashboardDisplayed() {

        return WaitUtils
                .waitForVisible(dashboardHeader)
                .isDisplayed();
    }

    public LoginPage logout() {

        WaitUtils.waitForClickable(userDropdown)
                .click();

        WaitUtils.waitForClickable(logoutLink)
                .click();

        return new LoginPage(driver);
    }
}