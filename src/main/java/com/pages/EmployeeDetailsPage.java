package com.pages;

import com.utils.LocatorUtils;
import com.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeDetailsPage extends BasePage {

    private final By jobTab =
            By.xpath("//a[normalize-space()='Job']");

    private final By jobTitleDropdown =
            LocatorUtils.dropdownByLabel("Job Title");

    private final By employmentStatusDropdown =
            LocatorUtils.dropdownByLabel("Employment Status");

    private final By saveButton =
            By.xpath("//button[normalize-space()='Save']");

    private final By successToast =
            By.cssSelector(".oxd-toast--success");

    private final By personalDetailsHeader =
            By.xpath("//h6[normalize-space()='Personal Details']");

    private final By formLoader =
            By.cssSelector(".oxd-form-loader");

    public EmployeeDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void updateJobTitle(String jobTitle) {

        WaitUtils.waitForInvisibility(formLoader);

        click(jobTab);

        WaitUtils.clickAfterLoaderDisappears(
                jobTitleDropdown,
                formLoader
        );

        click(LocatorUtils.optionByText(jobTitle));

        WaitUtils.waitForInvisibility(formLoader);
    }

    public void updateEmploymentStatus(String status) {

        WaitUtils.clickAfterLoaderDisappears(
                employmentStatusDropdown,
                formLoader
        );

        click(LocatorUtils.optionByText(status));

        WaitUtils.waitForInvisibility(formLoader);
    }

    public void saveChanges() {

        WaitUtils.clickAfterLoaderDisappears(
                saveButton,
                formLoader
        );

        waitForVisible(successToast);
    }

    public boolean isEmployeeCreatedSuccessfully() {
        return isDisplayed(personalDetailsHeader);
    }

    public boolean isUpdateSuccessful(
            String expectedJobTitle,
            String expectedEmploymentStatus) {

        WaitUtils.waitForInvisibility(formLoader);

        waitForVisible(jobTitleDropdown);
        waitForVisible(employmentStatusDropdown);

        String actualJobTitle =
                getText(jobTitleDropdown);

        String actualEmploymentStatus =
                getText(employmentStatusDropdown);

        return actualJobTitle.equals(expectedJobTitle)
                && actualEmploymentStatus.equals(expectedEmploymentStatus);
    }
}