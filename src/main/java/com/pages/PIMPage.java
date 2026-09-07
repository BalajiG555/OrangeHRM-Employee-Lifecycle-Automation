package com.pages;

import com.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PIMPage {

    private final WebDriver driver;

    private final By pimMenu =
            By.xpath("//span[normalize-space()='PIM']");

    private final By addEmployeeMenu =
            By.xpath("//a[normalize-space()='Add Employee']");

    private final By employeeListMenu =
            By.xpath("//a[normalize-space()='Employee List']");

    private final By employeeIdSearch =
            By.xpath(
                    "//label[normalize-space()='Employee Id']" +
                            "/ancestor::div[contains(@class,'oxd-input-group')]//input"
            );

    private final By searchButton =
            By.xpath("//button[normalize-space()='Search']");

    private final By resetButton =
            By.xpath("//button[normalize-space()='Reset']");

    private final By deleteButton =
            By.cssSelector(
                    "i.bi-trash"
            );

    private final By confirmDeleteButton =
            By.xpath(
                    "//button[normalize-space()='Yes, Delete']"
            );

    private final By noRecordsMessage =
            By.xpath(
                    "//*[contains(normalize-space(),'No Records Found')]"
            );

    private final By editButton =
            By.cssSelector("i.bi-pencil-fill");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
    }

    public AddEmployeePage navigateToAddEmployee() {

        WaitUtils.waitForClickable(pimMenu).click();

        WaitUtils.waitForClickable(addEmployeeMenu).click();

        return new AddEmployeePage(driver);
    }

    public PIMPage navigateToEmployeeList() {

        WaitUtils.waitForClickable(pimMenu).click();

        WaitUtils.waitForClickable(employeeListMenu).click();

        return this;
    }

    public void searchByEmployeeId(
            String employeeId) {

        WaitUtils.waitForVisible(employeeIdSearch)
                .sendKeys(employeeId);

        WaitUtils.waitForClickable(searchButton)
                .click();
    }

    public boolean isEmployeePresent(
            String employeeId) {

        By employeeRow = By.xpath(
                "//div[@role='row']" +
                        "[.//div[normalize-space()='" +
                        employeeId +
                        "']]"
        );

        try {

            return WaitUtils
                    .waitForVisible(employeeRow)
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    public EmployeeDetailsPage editEmployee() {

        WaitUtils.waitForClickable(editButton)
                .click();

        return new EmployeeDetailsPage(driver);
    }

    public void deleteEmployee() {

        WaitUtils.waitForClickable(deleteButton)
                .click();

        WaitUtils.waitForClickable(
                confirmDeleteButton
        ).click();
    }

    public boolean isEmployeeDeleted() {

        try {

            return WaitUtils
                    .waitForVisible(noRecordsMessage)
                    .isDisplayed();

        } catch (Exception e) {

            return true;
        }
    }
}