package com.utils;

import com.config.ConfigReader;
import com.driver.DriverFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait getWait() {

        return new WebDriverWait(
                DriverFactory.getDriver(),
                Duration.ofSeconds(
                        ConfigReader.getInt("explicit.wait")
                )
        );
    }

    public static WebElement waitForVisible(By locator) {

        return getWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public static WebElement waitForClickable(By locator) {

        return getWait().until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    public static void waitForInvisibility(By locator) {

        getWait().until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    /**
     * Wait until the element can actually be clicked
     * without the loader overlay intercepting the click.
     */
    public static void clickAfterLoaderDisappears(
            By elementLocator,
            By loaderLocator) {

        getWait().until(driver -> {

            try {

                // Wait until loader is not visible
                if (!ExpectedConditions
                        .invisibilityOfElementLocated(loaderLocator)
                        .apply(driver)) {

                    return false;
                }

                WebElement element =
                        driver.findElement(elementLocator);

                if (!element.isDisplayed()
                        || !element.isEnabled()) {

                    return false;
                }

                element.click();

                return true;

            } catch (ElementClickInterceptedException e) {

                // Loader appeared again.
                // Retry until the click succeeds.
                return false;

            } catch (Exception e) {

                return false;
            }
        });
    }

    public static boolean waitForUrlContains(String text) {

        return getWait().until(
                ExpectedConditions.urlContains(text)
        );
    }
}