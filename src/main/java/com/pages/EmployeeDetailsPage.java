package com.pages;

import com.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeDetailsPage {

    private final WebDriver driver;

    private final By jobTab =
            By.xpath("//a[normalize-space()='Job']");

    private final By jobTitleDropdown =
            By.xpath(
                    "//label[normalize-space()='Job Title']" +
                            "/ancestor::div[contains(@class,'oxd-input-group')]" +
                            "//div[contains(@class,'oxd-select-text')]"
            );

    private final By employmentStatusDropdown =
            By.xpath(
                    "//label[normalize-space()='Employment Status']" +
                            "/ancestor::div[contains(@class,'oxd-input-group')]" +
                            "//div[contains(@class,'oxd-select-text')]"
            );

    private final By saveButton =
            By.xpath("//button[normalize-space()='Save']");

    private final By successToast =
            By.cssSelector(".oxd-toast--success");

    private final By personalDetailsHeader =
            By.xpath("//h6[normalize-space()='Personal Details']");

    private final By formLoader =
            By.cssSelector(".oxd-form-loader");

    public EmployeeDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void updateJobTitle(String jobTitle) {

        WaitUtils.waitForInvisibility(formLoader);

        WaitUtils.waitForClickable(jobTab)
                .click();

        /*
         * OrangeHRM loads the Job form asynchronously.
         * The loader can appear after the Job tab is clicked,
         * so let the click helper handle the race condition.
         */
        WaitUtils.clickAfterLoaderDisappears(
                jobTitleDropdown,
                formLoader
        );

        By option =
                By.xpath(
                        "//div[@role='option']" +
                                "//span[normalize-space()='" +
                                jobTitle +
                                "']"
                );

        WaitUtils.waitForClickable(option)
                .click();

        WaitUtils.waitForInvisibility(formLoader);
    }

    public void updateEmploymentStatus(String status) {

        WaitUtils.clickAfterLoaderDisappears(
                employmentStatusDropdown,
                formLoader
        );

        By option =
                By.xpath(
                        "//div[@role='option']" +
                                "//span[normalize-space()='" +
                                status +
                                "']"
                );

        WaitUtils.waitForClickable(option)
                .click();

        WaitUtils.waitForInvisibility(formLoader);
    }

    public void saveChanges() {

        WaitUtils.clickAfterLoaderDisappears(
                saveButton,
                formLoader
        );

        WaitUtils.waitForVisible(successToast);
    }

    public boolean isEmployeeCreatedSuccessfully() {

        return WaitUtils
                .waitForVisible(personalDetailsHeader)
                .isDisplayed();
    }

    public boolean isUpdateSuccessful(
            String expectedJobTitle,
            String expectedEmploymentStatus) {

        WaitUtils.waitForInvisibility(formLoader);

        WaitUtils.waitForVisible(jobTitleDropdown);
        WaitUtils.waitForVisible(employmentStatusDropdown);

        String actualJobTitle =
                driver.findElement(jobTitleDropdown)
                        .getText();

        String actualEmploymentStatus =
                driver.findElement(employmentStatusDropdown)
                        .getText();

        return actualJobTitle.equals(expectedJobTitle)
                && actualEmploymentStatus.equals(expectedEmploymentStatus);
    }
}