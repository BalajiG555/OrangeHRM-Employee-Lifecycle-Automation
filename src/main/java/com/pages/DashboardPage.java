package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private final By dashboardHeader =
            By.xpath("//h6[normalize-space()='Dashboard']");

    private final By userDropdown =
            By.cssSelector(".oxd-userdropdown-name");

    private final By logoutLink =
            By.xpath("//a[normalize-space()='Logout']");

    private final By adminMenu =
            By.xpath("//span[normalize-space()='Admin']");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeader);
    }

    public boolean isAdminModuleDisplayed() {
        return isDisplayed(adminMenu);
    }

    public LoginPage logout() {
        click(userDropdown);
        click(logoutLink);
        return new LoginPage(driver);
    }
}