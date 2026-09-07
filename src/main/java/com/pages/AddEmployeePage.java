package com.pages;

import com.models.Employee;
import com.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class AddEmployeePage {

    private final WebDriver driver;

    private final By firstName =
            By.name("firstName");

    private final By lastName =
            By.name("lastName");

    private final By employeeId =
            By.xpath(
                    "//label[normalize-space()='Employee Id']" +
                            "/ancestor::div[contains(@class,'oxd-input-group')]//input"
            );

    private final By profilePicture =
            By.cssSelector("input[type='file']");

    private final By saveButton =
            By.xpath("//button[normalize-space()='Save']");

    private final By successToast =
            By.cssSelector(".oxd-toast--success");

    private final By personalDetailsHeader =
            By.xpath(
                    "//h6[normalize-space()='Personal Details']"
            );

    public AddEmployeePage(WebDriver driver) {
        this.driver = driver;
    }

    public EmployeeDetailsPage createEmployee(
            Employee employee) {

        WaitUtils.waitForVisible(firstName)
                .sendKeys(employee.getFirstName());

        WaitUtils.waitForVisible(lastName)
                .sendKeys(employee.getLastName());

        WaitUtils.waitForVisible(employeeId)
                .clear();

        enterEmployeeId(employee.getEmployeeId());

        String imagePath =
                new File(employee.getProfilePicture()).getAbsolutePath();

        driver.findElement(profilePicture)
                .sendKeys(imagePath);

        WaitUtils.waitForClickable(saveButton)
                .click();

        WaitUtils.waitForVisible(personalDetailsHeader);

        return new EmployeeDetailsPage(driver);
    }

    private void enterEmployeeId(String id) {
        WebElement field = WaitUtils.waitForVisible(employeeId);

        field.click();
        field.sendKeys(Keys.CONTROL, "a");
        field.sendKeys(Keys.BACK_SPACE);
        field.sendKeys(id);
    }

    public boolean isSuccessToastDisplayed() {

        return WaitUtils
                .waitForVisible(successToast)
                .isDisplayed();
    }

}