package com.stepdefinitions;

import com.api.EmployeeApiClient;
import com.config.ConfigReader;
import com.driver.DriverFactory;
import com.models.Employee;
import com.pages.AddEmployeePage;
import com.pages.DashboardPage;
import com.pages.EmployeeDetailsPage;
import com.pages.LoginPage;
import com.pages.PIMPage;
import com.utils.JsonDataReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.qameta.allure.Allure;

import io.restassured.response.Response;

import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmployeeLifecycleSteps {

    private LoginPage loginPage;

    private DashboardPage dashboardPage;

    private PIMPage pimPage;

    private AddEmployeePage addEmployeePage;

    private EmployeeDetailsPage employeeDetailsPage;

    private Employee employee;

    private EmployeeApiClient apiClient;

    private Response apiResponse;


    @Given("I login to OrangeHRM with valid credentials")
    public void loginToOrangeHRM() {

        employee = JsonDataReader.readEmployeeData();

        String uniqueEmployeeId =
                "A" +
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter
                                                .ofPattern("ddHHmmss")

                                );
        System.out.println(uniqueEmployeeId);

        employee.setEmployeeId(
                uniqueEmployeeId
        );

        loginPage =
                new LoginPage(
                        DriverFactory.getDriver()
                );

        dashboardPage =
                loginPage.login(
                        ConfigReader.get("username"),
                        ConfigReader.get("password")
                );

    }


    @Then("I should be redirected to the dashboard")
    public void verifyDashboard() {

        Assert.assertTrue(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after successful login."
        );
    }


    @When("I navigate to the Add Employee page")
    public void navigateToAddEmployee() {

        pimPage =
                new PIMPage(
                        DriverFactory.getDriver()
                );

        addEmployeePage =
                pimPage.navigateToAddEmployee();
    }


    @When("I create a new employee using test data")
    public void createEmployee() {

        employeeDetailsPage =
                addEmployeePage.createEmployee(
                        employee
                );
    }


    @Then("the employee should be created successfully")
    public void verifyEmployeeCreation() {

        Assert.assertTrue(
                employeeDetailsPage
                        .isEmployeeCreatedSuccessfully(),
                "Employee should be created successfully."
        );
    }


    @When("I search for the employee using Employee ID")
    public void searchEmployee() {

        pimPage =
                new PIMPage(
                        DriverFactory.getDriver()
                );

        pimPage.navigateToEmployeeList();

        pimPage.searchByEmployeeId(
                employee.getEmployeeId()
        );
    }


    @When("I update the employee Job Title and Employment Status")
    public void updateEmployeeDetails() {

        employeeDetailsPage =
                pimPage.editEmployee();

        employeeDetailsPage.updateJobTitle(
                employee.getJobTitle()
        );

        employeeDetailsPage.updateEmploymentStatus(
                employee.getEmploymentStatus()
        );

        employeeDetailsPage.saveChanges();
    }


    @Then("the employee details should be updated successfully")
    public void verifyEmployeeUpdate() {

        Assert.assertTrue(
                employeeDetailsPage
                        .isUpdateSuccessful(employee.getJobTitle(),
                                employee.getEmploymentStatus()),
                "Employee details should be updated successfully."
        );
    }


    @When("I validate the employee details through API")
    public void validateEmployeeThroughAPI() {

        boolean apiEnabled =
                ConfigReader.getBoolean(
                        "api.validation.enabled"
                );

        if (!apiEnabled) {

            Allure.addAttachment(
                    "API Validation",
                    "text/plain",
                    "API validation skipped because " +
                            "api.validation.enabled=false",
                    ".txt"
            );

            return;
        }

        apiClient =
                new EmployeeApiClient();

        // API 1 - Get OAuth token
        apiClient.authenticate(
                ConfigReader.get("api.authorization.code")
        );

        // Get Employee ID dynamically from Employee POJO
        String employeeId =
                employee.getEmployeeId();

        // API 2 - Find Employee ID and capture empNumber
        String empNumber =
                apiClient.findEmployeeNumber(
                        employeeId
                );

        // API 3 - Use empNumber to get employee details
        apiResponse =
                apiClient.getEmployee(
                        empNumber
                );

        Allure.addAttachment(
                "API Response",
                "application/json",
                apiResponse.asPrettyString(),
                ".json"
        );

        Assert.assertEquals(
                apiResponse.statusCode(),
                200,
                "Employee should exist through API."
        );
    }


    @Then("the API employee details should match the UI details")
    public void compareUIAndAPI() {

        boolean apiEnabled =
                ConfigReader.getBoolean(
                        "api.validation.enabled"
                );

        if (!apiEnabled) {
            return;
        }

        String firstName =
                apiResponse.jsonPath()
                        .getString(
                                "data.firstName"
                        );

        String lastName =
                apiResponse.jsonPath()
                        .getString(
                                "data.lastName"
                        );

        Assert.assertEquals(
                firstName,
                employee.getFirstName(),
                "API first name should match UI data."
        );

        Assert.assertEquals(
                lastName,
                employee.getLastName(),
                "API last name should match UI data."
        );
    }


    @When("I delete the employee")
    public void deleteEmployee() {

        pimPage.navigateToEmployeeList();

        pimPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        pimPage.deleteEmployee();
    }


    @Then("the employee should no longer exist in the UI")
    public void verifyEmployeeDeletedFromUI() {

        pimPage.navigateToEmployeeList();

        pimPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        Assert.assertTrue(
                pimPage.isEmployeeDeleted(),
                "Employee should no longer exist in UI."
        );
    }


    @Then("the employee should no longer exist through API")
    public void verifyEmployeeDeletedFromAPI() {

        boolean apiEnabled =
                ConfigReader.getBoolean(
                        "api.validation.enabled"
                );

        if (!apiEnabled) {
            return;
        }

        Response response =
                apiClient.getEmployee(
                        employee.getEmployeeId()
                );

        Assert.assertEquals(
                response.statusCode(),
                404,
                "Deleted employee should not exist through API."
        );
    }


    @When("I logout")
    public void logout() {

        dashboardPage =
                new DashboardPage(
                        DriverFactory.getDriver()
                );

        loginPage =
                dashboardPage.logout();
    }


    @Then("I should be redirected to the login page")
    public void verifyLogout() {

        Assert.assertTrue(
                DriverFactory.getDriver()
                        .getCurrentUrl()
                        .contains("/auth/login"),
                "User should be redirected to login page after logout."
        );
    }
}