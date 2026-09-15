package com.pages;

import com.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement waitForVisible(By locator) {
        return WaitUtils.waitForVisible(locator);
    }

    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(locator);
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void enterText(
            By locator,
            String text) {

        WebElement element =
                waitForVisible(locator);

        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {

        try {
            return waitForVisible(locator)
                    .isDisplayed();

        } catch (Exception e) {
            return false;
        }
    }
}