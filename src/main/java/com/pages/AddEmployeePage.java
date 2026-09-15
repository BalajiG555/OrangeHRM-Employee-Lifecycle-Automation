package com.pages;

import com.models.Employee;
import com.utils.LocatorUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;

public class AddEmployeePage extends BasePage {

    private final By firstName =
            By.name("firstName");

    private final By lastName =
            By.name("lastName");

    private final By employeeId =
            LocatorUtils.inputByLabel("Employee Id");

    private final By profilePicture =
            By.cssSelector("input[type='file']");

    private final By saveButton =
            By.xpath("//button[normalize-space()='Save']");

    private final By successToast =
            By.cssSelector(".oxd-toast--success");

    private final By personalDetailsHeader =
            By.xpath("//h6[normalize-space()='Personal Details']");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public EmployeeDetailsPage createEmployee(Employee employee) {

        enterText(
                firstName,
                employee.getFirstName()
        );

        enterText(
                lastName,
                employee.getLastName()
        );

        enterEmployeeId(
                employee.getEmployeeId()
        );

        String imagePath =
                new File(
                        employee.getProfilePicture()
                ).getAbsolutePath();

        driver.findElement(profilePicture).sendKeys(imagePath);

        click(saveButton);

        waitForVisible(personalDetailsHeader);

        return new EmployeeDetailsPage(driver);
    }

    private void enterEmployeeId(String id) {

        WebElement field =
                waitForVisible(employeeId);

        field.click();

        field.sendKeys(
                Keys.CONTROL,
                "a"
        );

        field.sendKeys(
                Keys.BACK_SPACE
        );

        field.sendKeys(id);
    }

    public boolean isSuccessToastDisplayed() {
        return isDisplayed(successToast);
    }
}