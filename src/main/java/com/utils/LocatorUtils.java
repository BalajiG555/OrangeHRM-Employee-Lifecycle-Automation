package com.utils;

import org.openqa.selenium.By;

public final class LocatorUtils {

    private LocatorUtils() {
    }

    public static By inputByLabel(String label) {
        return By.xpath(
                "//label[normalize-space()='" + label + "']" +
                        "/ancestor::div[contains(@class,'oxd-input-group')]//input"
        );
    }

    public static By dropdownByLabel(String label) {
        return By.xpath(
                "//label[normalize-space()='" + label + "']" +
                        "/ancestor::div[contains(@class,'oxd-input-group')]" +
                        "//div[contains(@class,'oxd-select-text')]"
        );
    }

    public static By optionByText(String optionText) {
        return By.xpath(
                "//div[@role='option']//span[normalize-space()='" +
                        optionText +
                        "']"
        );
    }
}