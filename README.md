# OrangeHRM Employee Lifecycle Automation

## Overview

This project automates an end-to-end Employee Lifecycle Management workflow for OrangeHRM using:

* Selenium WebDriver
* Java
* Cucumber BDD
* TestNG
* REST Assured
* Allure Reporting
* Maven
* Page Object Model (POM)

The automation covers the complete employee lifecycle:

1. Login to OrangeHRM
2. Navigate to PIM
3. Add a new employee
4. Create employee using external test data
5. Generate a unique Employee ID dynamically
6. Upload employee profile picture
7. Verify employee creation
8. Search for the employee using Employee ID
9. Update Job Title and Employment Status
10. Verify employee information update
11. Validate employee details through OrangeHRM APIs
12. Delete the employee
13. Verify employee deletion through the UI
14. Verify employee deletion through the API
15. Logout
16. Verify successful logout

---

## Technology Stack

| Technology         | Purpose                             |
| ------------------ | ----------------------------------- |
| Java 23            | Programming language                |
| Selenium 4.48.0    | Web UI automation                   |
| Cucumber 7.18.1    | BDD framework                       |
| TestNG 7.10.2      | Test execution                      |
| REST Assured 5.5.0 | API automation and validation       |
| Allure 2.29.0      | Test reporting                      |
| Maven              | Build and dependency management     |
| Page Object Model  | UI automation design pattern        |
| Selenium Manager   | Automatic browser driver management |

---

## Framework Design

The framework follows the Page Object Model and separates UI automation, API automation, configuration, test data, utilities, and step definitions.

```text
                       Feature File
                            |
                            v
                     Step Definitions
                       /          \
                      /            \
                     v              v
              Page Objects      API Client
                   |                 |
                   v                 v
          Selenium WebDriver     REST Assured
                   |                 |
                   \                 /
                    \               /
                     v             v
                       Assertions
                            |
                            v
                      Allure Report
```

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com
│   │       ├── api
│   │       │   └── EmployeeApiClient.java
│   │       │
│   │       ├── config
│   │       │   └── ConfigReader.java
│   │       │
│   │       ├── driver
│   │       │   └── DriverFactory.java
│   │       │
│   │       ├── models
│   │       │   └── Employee.java
│   │       │
│   │       ├── pages
│   │       │   ├── LoginPage.java
│   │       │   ├── DashboardPage.java
│   │       │   ├── PIMPage.java
│   │       │   ├── AddEmployeePage.java
│   │       │   └── EmployeeDetailsPage.java
│   │       │
│   │       └── utils
│   │           ├── JsonDataReader.java
│   │           ├── ScreenshotUtils.java
│   │           └── WaitUtils.java
│   │
│   └── resources
│       └── config
│           └── config.properties
│
└── test
    ├── java
    │   └── com
    │       ├── features
    │       │   └── employee_lifecycle.feature
    │       │
    │       ├── hooks
    │       │   └── Hooks.java
    │       │
    │       ├── runners
    │       │   └── TestRunner.java
    │       │
    │       └── stepdefinitions
    │           └── EmployeeLifecycleSteps.java
    │
    └── resources
        └── testdata
            └── profile.png
```

---

## Prerequisites

Install the following:

* Java 23
* Maven
* Google Chrome
* IntelliJ IDEA or another Java IDE
* Allure CLI (optional for viewing Allure reports)

Selenium Manager is used for browser driver management. Therefore, **WebDriverManager is not required**.

---

## Configuration

Application and API configuration is maintained in:

```text
src/main/resources/config/config.properties
```

Example:

```properties
# Application
base.url=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login

# Browser
browser=chrome
headless=false

# UI credentials
username=Admin
password=admin123

# Timeout
explicit.wait=15

# API
api.base.url=https://opensource-demo.orangehrmlive.com/web/index.php

# OrangeHRM OAuth configuration
api.client.id=<your-client-id>
api.client.secret=<your-client-secret>
api.redirect.uri=https://reqres.in
api.authorization.code=<your-authorization-code>

# API validation
api.validation.enabled=true
```

### Configuration Override

Configuration values can also be overridden through Maven system properties.

For example:

```bash
mvn clean test -Dbrowser=chrome
```

or:

```bash
mvn clean test -Dheadless=true
```

This allows the framework to be executed with different runtime configurations without modifying the properties file.

---

## Security

API credentials and authorization codes should **never be committed to source control**.

For a real project, sensitive values should be supplied through:

* Environment variables
* CI/CD secrets
* Secure configuration management

The authorization code used by the OrangeHRM OAuth flow may be short-lived and/or single-use, so a fresh authorization code may be required for subsequent test executions.

---

# Test Data

Employee test data is maintained externally in:

```text
src/main/resources/testdata/employee.json
```

Example:

```json
{
  "firstName": "Automation",
  "lastName": "Tester",
  "employeeId": "",
  "jobTitle": "QA Engineer",
  "employmentStatus": "Full-Time Permanent"
}
```

The `Employee` class acts as the POJO/model for employee test data.

The Employee ID is generated dynamically during test execution to avoid duplicate employee records.

The generated Employee ID is stored in the `Employee` POJO and can be retrieved using:

```java
employee.getEmployeeId()
```

---

# Profile Picture

The employee profile picture used during employee creation is maintained under:

```text
src/test/resources/testdata/profile.png
```

The image is treated as test data and is used during the employee profile picture upload flow.

Keeping the image in the test resources makes the test asset easy to maintain and separate from the application/framework source code.

---

# Feature File

The Cucumber feature file is maintained under:

```text
src/test/java/com/features/employee_lifecycle.feature
```

The feature file describes the employee lifecycle scenario using Gherkin syntax.

The scenario covers the complete flow from employee creation through deletion and logout.

---

# API Validation

API validation is implemented using **REST Assured** and is integrated into the employee lifecycle scenario.

The API validation uses three OrangeHRM APIs.

## API 1 – OAuth Token

The first API is used to obtain an access token.

```text
POST /oauth2/token
```

The request uses:

```text
grant_type
code
client_id
client_secret
redirect_uri
```

The `access_token` returned by this API is stored dynamically in `EmployeeApiClient`.

The same access token is then used for the subsequent employee APIs.

---

## API 2 – Get Employees

The second API retrieves employees:

```text
GET /api/v2/pim/employees?limit=10
```

The request uses:

```text
Authorization: Bearer <access_token>
```

The response contains multiple employee records.

The automation takes the dynamically generated Employee ID from the `Employee` POJO:

```java
String employeeId = employee.getEmployeeId();
```

It then searches the API 2 response for the matching:

```text
employeeId
```

Once the matching employee is found, the automation captures:

```text
empNumber
```

For example:

```text
Employee ID = 07142658
empNumber   = 512
```

---

## API 3 – Get Employee Details

The third API retrieves the employee using the internal OrangeHRM employee number.

```text
GET /api/v2/pim/employees/{empNumber}
```

For example:

```text
GET /api/v2/pim/employees/512
```

The important distinction is:

```text
Employee ID
    |
    | Used to find employee in API 2
    v
07142658
    |
    | API 2 response
    v
empNumber = 512
    |
    | Used by API 3
    v
GET /api/v2/pim/employees/512
```

This prevents the UI Employee ID from being incorrectly passed directly to API 3.

---

## API Validation Flow

The complete API validation flow is:

```text
UI creates employee
        |
        v
Employee POJO stores dynamic Employee ID
        |
        v
API 1
OAuth Token
        |
        | access_token
        v
API 2
Get Employees
        |
        | Search employeeId
        v
Find matching employee
        |
        | Capture empNumber
        v
API 3
Get Employee Details
        |
        v
Validate employee details
```

---

# UI vs API Validation

The same employee data is used across the UI and API layers.

```text
Employee POJO
     |
     +---- firstName
     |
     +---- lastName
     |
     +---- employeeId
     |
     +---- jobTitle
     |
     +---- employmentStatus
```

The UI uses these values to create and update the employee.

The API layer uses the dynamically generated Employee ID to locate the corresponding internal `empNumber` and retrieve the employee details.

The API response is then validated against the expected employee information.

---

# Employee Deletion

After the employee has been created and updated, the automation deletes the employee through the UI.

The framework then performs two validations.

### UI Validation

The employee is searched using the dynamically generated Employee ID.

The test verifies that the employee is no longer present in the employee list.

### API Validation

The internal `empNumber` captured during API validation is retained and reused for the deletion check.

The API request is:

```text
GET /api/v2/pim/employees/{empNumber}
```

For example:

```text
GET /api/v2/pim/employees/512
```

Expected result:

```text
HTTP 404
```

This provides an additional backend-level verification that the employee was successfully removed.

---

# API Validation Configuration

API validation can be enabled or disabled using:

```properties
api.validation.enabled=true
```

When enabled:

```text
UI Validation
      +
API Validation
```

When disabled:

```text
UI Validation only
```

This allows the UI automation to be executed independently when API credentials or authorization configuration are unavailable.

---

# Wait and Synchronization

The framework uses reusable explicit wait utilities through:

```text
WaitUtils.java
```

The framework handles dynamic OrangeHRM UI elements and loading overlays using explicit waits rather than fixed delays.

Interactions with elements that may be temporarily covered by the OrangeHRM form loader are synchronized before performing the action.

This improves test stability and reduces failures caused by timing issues.

---

# Screenshot Handling

Screenshot functionality is centralized in:

```text
ScreenshotUtils.java
```

The utility is located under:

```text
src/main/java/com/utils/ScreenshotUtils.java
```

Screenshots can be captured when a Cucumber scenario fails and attached to the reporting output.

Centralizing screenshot functionality keeps screenshot-related operations separate from the page objects and step definitions.

---

# Execute Tests

Run the complete test suite:

```bash
mvn clean test
```

Run with a specific browser:

```bash
mvn clean test -Dbrowser=chrome
```

Run in headless mode:

```bash
mvn clean test -Dheadless=true
```

Enable API validation:

```bash
mvn clean test -Dapi.validation.enabled=true
```

Disable API validation:

```bash
mvn clean test -Dapi.validation.enabled=false
```

---

# Cucumber Report

After execution, the Cucumber HTML report is generated under:

```text
target/cucumber-reports/cucumber.html
```

The Cucumber JSON report is also generated under the configured Cucumber report location.

---

# Allure Report

Allure results are generated under:

```text
target/allure-results
```

To generate and open the Allure report:

```bash
allure serve target/allure-results
```

The report can contain:

* Scenario execution details
* Step execution status
* Screenshots for failed scenarios
* API response attachments
* Failure information

---

# Failure Handling

When a Cucumber scenario fails:

* A screenshot is automatically captured.
* The screenshot is attached to the Allure report.
* Cucumber HTML and JSON reports are generated.
* API responses can be attached to Allure when API validation is executed.

This helps identify whether a failure occurred in:

* UI automation
* API automation
* UI/API data validation
* Test data handling

---

# Coding Practices

The framework follows:

* Page Object Model
* Single Responsibility Principle
* Separation of UI and API layers
* Reusable utility methods
* Explicit waits
* Externalized test data
* Externalized configuration
* POJO-based test data handling
* Dynamic Employee ID generation
* Meaningful assertions
* Thread-safe WebDriver management
* No hard-coded credentials in test classes
* Selenium Manager for driver management
* REST Assured for API automation

---

# Current API Architecture

The API automation is encapsulated inside:

```text
EmployeeApiClient.java
```

The class is responsible for:

```text
authenticate()
     |
     v
getEmployees()
     |
     v
findEmployeeNumber()
     |
     v
getEmployee()
```

The step definitions orchestrate the API calls, while the API client handles the REST Assured implementation.

This keeps API implementation details separate from the Cucumber step definitions.

---

# Future Enhancements

Possible future improvements include:

* Parallel execution
* Cross-browser execution
* CI/CD integration
* Dockerized Selenium Grid
* Retry mechanism
* Environment-specific configuration
* API schema validation
* API response POJOs
* Advanced Allure attachments
* Automated video recording
* API pagination handling
* Improved OAuth token management
* Environment variables for sensitive credentials
* API validation of Job Title and Employment Status
* Reusable API assertion utilities

---

# Author

QA Automation Engineer

**Selenium | Java | Cucumber | TestNG | REST Assured | API Automation**
