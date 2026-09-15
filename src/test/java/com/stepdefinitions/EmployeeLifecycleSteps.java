package com.stepdefinitions;

import com.api.EmployeeApiClient;
import com.config.ConfigReader;
import com.context.TestContext;
import com.driver.DriverFactory;
import com.models.Employee;
import com.pages.AddEmployeePage;
import com.pages.DashboardPage;
import com.pages.EmployeeDetailsPage;
import com.pages.LoginPage;
import com.pages.PIMPage;
import com.utils.TestDataGenerator;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;

import io.restassured.response.Response;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class EmployeeLifecycleSteps {

    private final TestContext testContext =
            new TestContext();

    private WebDriver driver;

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private PIMPage pimPage;
    private AddEmployeePage addEmployeePage;
    private EmployeeDetailsPage employeeDetailsPage;

    private EmployeeApiClient apiClient;

    private boolean isApiValidationEnabled;

    @Given("I login to OrangeHRM with valid credentials")
    public void loginToOrangeHRM() {

        driver = DriverFactory.getDriver();

        Employee employee =
                TestDataGenerator.createEmployee();

        testContext.setEmployee(employee);

        loginPage =
                new LoginPage(driver);

        dashboardPage =
                loginPage.loginWithAdminCredentials();

        isApiValidationEnabled =
                ConfigReader.getBoolean(
                        "api.validation.enabled"
                );
    }

    @Given("I login to OrangeHRM with ESS credentials")
    public void loginWithEssCredentials() {

        driver = DriverFactory.getDriver();

        loginPage =
                new LoginPage(driver);

        dashboardPage =
                loginPage.loginWithEssCredentials();
    }

    @Then("I should be redirected to the dashboard")
    public void verifyDashboard() {

        Assert.assertTrue(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after successful login."
        );
    }

    @Then("the ESS user should be redirected to the dashboard")
    public void verifyEssDashboard() {

        Assert.assertTrue(
                dashboardPage.isDashboardDisplayed(),
                "ESS user should be redirected to the dashboard."
        );
    }

    @Then("the ESS user should not have access to the Admin module")
    public void verifyEssAdminAccess() {

        Assert.assertFalse(
                dashboardPage.isAdminModuleDisplayed(),
                "ESS user should not have access to the Admin module."
        );
    }

    @When("I navigate to the Add Employee page")
    public void navigateToAddEmployee() {

        pimPage =
                new PIMPage(driver);

        addEmployeePage =
                pimPage.navigateToAddEmployee();
    }

    @When("I create a new employee using test data")
    public void createEmployee() {

        Employee employee =
                testContext.getEmployee();

        Assert.assertNotNull(
                employee,
                "Employee test data should be available."
        );

        employeeDetailsPage =
                addEmployeePage.createEmployee(employee);

        testContext.markEmployeeCreated();
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
                new PIMPage(driver);

        pimPage.navigateToEmployeeList();

        pimPage.searchByEmployeeId(
                testContext
                        .getEmployee()
                        .getEmployeeId()
        );
    }

    @When("I update the employee Job Title and Employment Status")
    public void updateEmployeeDetails() {

        Employee employee =
                testContext.getEmployee();

        Assert.assertNotNull(
                employee,
                "Employee test data should be available."
        );

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

        Employee employee =
                testContext.getEmployee();

        Assert.assertTrue(
                employeeDetailsPage.isUpdateSuccessful(
                        employee.getJobTitle(),
                        employee.getEmploymentStatus()
                ),
                "Employee details should be updated successfully."
        );
    }

    @When("I validate the employee details through API")
    public void validateEmployeeThroughAPI() {

        if (!isApiValidationEnabled) {

            addApiValidationSkippedAttachment();

            return;
        }

        apiClient =
                new EmployeeApiClient();

        apiClient.authenticate();

        String employeeId =
                testContext
                        .getEmployee()
                        .getEmployeeId();

        String empNumber =
                apiClient.findEmployeeNumber(
                        employeeId
                );

        Assert.assertNotNull(
                empNumber,
                "Employee should exist in API."
        );

        Response response =
                apiClient.getEmployee(empNumber);

        testContext.setApiResponse(response);

        addApiResponseAttachment();
    }

    @Then("the API employee details should match the UI details")
    public void compareUIAndAPI() {

        if (!isApiValidationEnabled) {
            return;
        }

        Response apiResponse =
                testContext.getApiResponse();

        Assert.assertNotNull(
                apiResponse,
                "API response should be available."
        );

        Assert.assertEquals(
                apiResponse.statusCode(),
                200,
                "Employee should exist through API."
        );

        Employee employee =
                testContext.getEmployee();

        String firstName =
                apiResponse.jsonPath()
                        .getString("data.firstName");

        String lastName =
                apiResponse.jsonPath()
                        .getString("data.lastName");

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
                testContext
                        .getEmployee()
                        .getEmployeeId()
        );

        pimPage.deleteEmployee();

        testContext.markEmployeeDeleted();
    }

    @Then("the employee should no longer exist in the UI")
    public void verifyEmployeeDeletedFromUI() {

        pimPage.navigateToEmployeeList();

        pimPage.searchByEmployeeId(
                testContext
                        .getEmployee()
                        .getEmployeeId()
        );

        Assert.assertTrue(
                pimPage.isEmployeeDeleted(),
                "Employee should no longer exist in UI."
        );
    }

    @Then("the employee should no longer exist through API")
    public void verifyEmployeeDeletedFromAPI() {

        if (!isApiValidationEnabled) {
            return;
        }

        if (apiClient == null) {

            apiClient =
                    new EmployeeApiClient();

            apiClient.authenticate();
        }

        boolean employeeExists =
                apiClient.employeeExists(
                        testContext
                                .getEmployee()
                                .getEmployeeId()
                );

        Assert.assertFalse(
                employeeExists,
                "Deleted employee should no longer exist through API."
        );
    }

    @When("I logout")
    public void logout() {

        dashboardPage =
                new DashboardPage(driver);

        loginPage =
                dashboardPage.logout();
    }

    @Then("I should be redirected to the login page")
    public void verifyLogout() {

        Assert.assertTrue(
                driver.getCurrentUrl()
                        .contains("/auth/login"),
                "User should be redirected to login page after logout."
        );
    }

    private void addApiValidationSkippedAttachment() {

        Allure.addAttachment(
                "API Validation",
                "text/plain",
                "API validation skipped because " +
                        "api.validation.enabled=false",
                ".txt"
        );
    }

    private void addApiResponseAttachment() {

        Response response =
                testContext.getApiResponse();

        if (response == null) {
            return;
        }

        Allure.addAttachment(
                "API Response",
                "application/json",
                response.asPrettyString(),
                ".json"
        );
    }
}