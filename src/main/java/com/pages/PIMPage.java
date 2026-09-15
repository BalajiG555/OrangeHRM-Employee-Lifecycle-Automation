package com.pages;

import com.utils.LocatorUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PIMPage extends BasePage {

    private final By pimMenu =
            By.xpath("//span[normalize-space()='PIM']");

    private final By addEmployeeMenu =
            By.xpath("//a[normalize-space()='Add Employee']");

    private final By employeeListMenu =
            By.xpath("//a[normalize-space()='Employee List']");

    private final By employeeIdSearch =
            LocatorUtils.inputByLabel("Employee Id");

    private final By searchButton =
            By.xpath("//button[normalize-space()='Search']");

    private final By deleteButton =
            By.cssSelector("i.bi-trash");

    private final By confirmDeleteButton =
            By.xpath("//button[normalize-space()='Yes, Delete']");

    private final By noRecordsMessage =
            By.xpath(
                    "//*[contains(normalize-space(),'No Records Found')]"
            );

    private final By editButton =
            By.cssSelector("i.bi-pencil-fill");

    public PIMPage(WebDriver driver) {
        super(driver);
    }

    public AddEmployeePage navigateToAddEmployee() {
        click(pimMenu);
        click(addEmployeeMenu);
        return new AddEmployeePage(driver);
    }

    public PIMPage navigateToEmployeeList() {
        click(pimMenu);
        click(employeeListMenu);
        return this;
    }

    public void searchByEmployeeId(String employeeId) {
        enterText(employeeIdSearch, employeeId);
        click(searchButton);
    }

    public boolean isEmployeePresent(String employeeId) {

        By employeeRow = By.xpath(
                "//div[@role='row']" +
                        "[.//div[normalize-space()='" +
                        employeeId +
                        "']]"
        );

        return isDisplayed(employeeRow);
    }

    public EmployeeDetailsPage editEmployee() {
        click(editButton);
        return new EmployeeDetailsPage(driver);
    }

    public void deleteEmployee() {
        click(deleteButton);
        click(confirmDeleteButton);
    }

    public boolean isEmployeeDeleted() {
        return isDisplayed(noRecordsMessage);
    }
}