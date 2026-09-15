# OrangeHRM Employee Lifecycle Automation

Selenium + Cucumber + TestNG + REST Assured automation framework for end-to-end Employee Lifecycle Management in OrangeHRM.

## Overview

This project automates the Employee Lifecycle Management workflow in OrangeHRM using UI and API automation.

The framework covers:

* Admin login
* Employee creation
* Dynamic Employee ID generation
* Profile picture upload
* Employee search
* Job Title and Employment Status update
* UI validation
* API validation
* Employee deletion
* API-based deletion verification
* Logout validation
* ESS role-based access validation

The framework is designed using Page Object Model (POM), reusable utilities, externalized configuration, external test data, API client abstraction, retry handling, failure screenshots, Allure reporting, centralized logging, automatic test-data cleanup, and GitHub Actions CI.

---

## Technology Stack

| Technology         | Version | Purpose                         |
| ------------------ | ------: | ------------------------------- |
| Java               |      23 | Programming language            |
| Selenium WebDriver |  4.48.0 | UI automation                   |
| Cucumber           |  7.18.1 | BDD framework                   |
| TestNG             |  7.10.2 | Test execution and assertions   |
| REST Assured       |   5.5.0 | API automation                  |
| Jackson            |  2.22.1 | JSON test-data handling         |
| Allure             |  2.29.0 | Test reporting                  |
| Maven              |       - | Build and dependency management |
| GitHub Actions     |       - | CI/CD                           |
| Selenium Manager   |       - | Browser driver management       |

---

## Framework Architecture

The framework follows a layered architecture:

```text
                    Cucumber Feature
                           |
                           v
                  Step Definitions
                    /           \
                   /             \
                  v               v
            Page Objects       API Client
                 |                 |
                 v                 v
          Selenium WebDriver   REST Assured
                 |                 |
                 \                 /
                  \               /
                   v             v
                    Assertions
                         |
                         v
                 Allure Reporting
```

### Main Design Principles

* Page Object Model
* Base Page abstraction
* Separation of UI and API layers
* Reusable utility methods
* Externalized configuration
* Environment-specific configuration
* Externalized test data
* POJO-based test data handling
* Dynamic test data generation
* Explicit waits
* Thread-safe WebDriver management
* Thread-local test context
* Retry handling
* Failure screenshots
* Centralized logging
* API client abstraction
* Automatic employee cleanup
* CI/CD integration

---

## Project Structure

```text
OrangeHRM-Employee-Lifecycle-Automation
│
├── .github
│   └── workflows
│       └── ci.yml
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       ├── api
│   │   │       │   └── EmployeeApiClient.java
│   │   │       │
│   │   │       ├── config
│   │   │       │   └── ConfigReader.java
│   │   │       │
│   │   │       ├── context
│   │   │       │   └── TestContext.java
│   │   │       │
│   │   │       ├── driver
│   │   │       │   └── DriverFactory.java
│   │   │       │
│   │   │       ├── listeners
│   │   │       │   ├── FlakyTestListener.java
│   │   │       │   ├── RetryAnalyzer.java
│   │   │       │   └── RetryListener.java
│   │   │       │
│   │   │       ├── models
│   │   │       │   └── Employee.java
│   │   │       │
│   │   │       ├── pages
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── LoginPage.java
│   │   │       │   ├── DashboardPage.java
│   │   │       │   ├── PIMPage.java
│   │   │       │   ├── AddEmployeePage.java
│   │   │       │   └── EmployeeDetailsPage.java
│   │   │       │
│   │   │       ├── services
│   │   │       │   ├── EmployeeService.java
│   │   │       │   └── TestDataService.java
│   │   │       │
│   │   │       └── utils
│   │   │           ├── JsonDataReader.java
│   │   │           ├── LocatorUtils.java
│   │   │           ├── ScreenshotUtils.java
│   │   │           ├── TestDataGenerator.java
│   │   │           ├── TestLogger.java
│   │   │           ├── VideoRecorder.java
│   │   │           └── WaitUtils.java
│   │   │
│   │   └── resources
│   │       ├── config
│   │       │   ├── config-dev.properties
│   │       │   ├── config-qa.properties
│   │       │   ├── config-prod.properties
│   │       │   └── config.properties.example
│   │       │
│   │       └── testdata
│   │           └── employee.json
│   │
│   └── test
│       └── java
│           └── com
│               ├── feature
│               │   └── employee_lifecycle.feature
│               ├── hooks
│               │   └── Hooks.java
│               ├── runners
│               │   └── TestRunner.java
│               └── stepdefinitions
│                   └── EmployeeLifecycleSteps.java
│
├── .gitignore
├── pom.xml
└── testng.xml
```

---

## Key Framework Components

### BasePage

`BasePage` provides reusable Selenium operations for all page objects.

Common operations include:

* Waiting for visible elements
* Waiting for clickable elements
* Clicking elements
* Entering text
* Reading element text
* Checking element visibility

This avoids duplicating common WebDriver code across individual page classes.

---

### LocatorUtils

`LocatorUtils` provides reusable locator-building methods for OrangeHRM's label-based UI elements.

Examples include:

* Input fields by label
* Dropdowns by label
* Dropdown options by text

This keeps dynamic XPath construction centralized and avoids duplicated locator logic.

---

### TestContext

`TestContext` maintains scenario-specific test data using `ThreadLocal`.

It stores information such as:

* Employee test data
* API responses
* Employee creation state
* Employee deletion state

This allows test data to be shared safely between step definitions and hooks.

---

### DriverFactory

`DriverFactory` manages WebDriver creation and lifecycle.

The framework supports:

* Chrome
* Headless execution
* Thread-local WebDriver management
* Selenium Manager for automatic driver management

WebDriverManager is not required.

---

### ConfigReader

`ConfigReader` supports configuration from multiple sources.

The lookup order is:

```text
1. JVM System Property
          |
          v
2. ORANGEHRM_* Environment Variable
          |
          v
3. Properties File
```

This allows the framework to use local configuration during development while allowing GitHub Actions to inject sensitive configuration through environment variables.

---

## Configuration

The repository contains environment-specific configuration files:

```text
src/main/resources/config/
├── config-dev.properties
├── config-qa.properties
├── config-prod.properties
└── config.properties.example
```

The actual local configuration file is:

```text
src/main/resources/config/config.properties
```

This file is intentionally excluded from Git because it can contain local credentials and other sensitive configuration.

### Local Execution

For local execution, the framework is intended to use:

```text
config.properties
```

Example:

```properties
base.url=https://opensource-demo.orangehrmlive.com/web/index.php/auth/login

browser=chrome
headless=false

username=Admin
password=admin123

explicit.wait=15

api.base.url=https://opensource-demo.orangehrmlive.com

api.client.id=<your-client-id>
api.client.secret=<your-client-secret>
api.redirect.uri=<your-redirect-uri>
api.authorization.code=<your-authorization-code>

api.validation.enabled=true

ess.username=<ess-username>
ess.password=<ess-password>
```

Do not commit the actual `config.properties` file to source control.

---

## Environment Selection

The framework supports:

```text
local
dev
qa
prod
```

The environment can be selected using the Maven system property:

```bash
mvn clean test -Denv=qa
```

Examples:

```bash
mvn clean test -Denv=dev
mvn clean test -Denv=qa
mvn clean test -Denv=prod
```

If no environment is specified, `local` is used by default.

For local execution, the base configuration is loaded from `config.properties`.

For non-local environments, the corresponding environment-specific properties file is loaded.

---

## Security and Secrets

Sensitive information should never be committed to Git.

The following values should be kept outside source control:

* Application passwords
* API client IDs
* API client secrets
* OAuth authorization codes
* ESS credentials
* Other environment-specific secrets

The actual local configuration file is excluded through `.gitignore`:

```text
src/main/resources/config/config.properties
```

### GitHub Actions Secrets

GitHub Actions uses repository secrets to supply sensitive configuration during CI execution.

The workflow expects the following repository secret names:

```text
ORANGEHRM_USERNAME
ORANGEHRM_PASSWORD

ORANGEHRM_API_BASE_URL
ORANGEHRM_API_CLIENT_ID
ORANGEHRM_API_CLIENT_SECRET
ORANGEHRM_API_REDIRECT_URI
ORANGEHRM_API_AUTHORIZATION_CODE
ORANGEHRM_API_VALIDATION_ENABLED

ORANGEHRM_ESS_USERNAME
ORANGEHRM_ESS_PASSWORD
```

The actual secret values are stored securely in GitHub and are not committed to the repository.

The CI workflow maps these secrets to the corresponding `ORANGEHRM_*` environment variables consumed by `ConfigReader`.

This keeps credentials and other sensitive values outside the source code.

---

## Test Data

Employee test data is maintained externally in:

```text
src/main/resources/testdata/employee.json
```

Example:

```json
{
  "firstName": "Automation",
  "lastName": "Tester",
  "jobTitle": "QA Engineer",
  "employmentStatus": "Full-Time Permanent",
  "profilePicture": "src/test/resources/testdata/profile.png"
}
```

The `Employee` class acts as the POJO/model for employee test data.

---

## Dynamic Employee Data

`TestDataGenerator` creates unique employee data for each execution.

The framework dynamically generates:

* First Name
* Last Name
* Employee ID

This reduces the possibility of duplicate employee records during repeated executions.

The generated values are stored in the `Employee` POJO and shared through `TestContext`.

---

## Employee Lifecycle Scenarios

The Cucumber feature file is:

```text
src/test/java/com/feature/employee_lifecycle.feature
```

The current scenarios are:

### Smoke

* Login with valid credentials
* Logout successfully

### Regression

* Create a new employee
* Update Job Title and Employment Status
* Validate employee details through API
* Delete an employee

### Role-Based

* Validate ESS role access
* Verify that ESS users cannot access the Admin module

---

## Cucumber Tags

The framework uses tags to control test execution:

```text
@smoke
@regression
@roleBased
```

Examples:

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

```bash
mvn clean test -Dcucumber.filter.tags="@regression"
```

```bash
mvn clean test -Dcucumber.filter.tags="@roleBased"
```

---

## API Automation

API automation is implemented using REST Assured and encapsulated in:

```text
EmployeeApiClient.java
```

The API client handles authentication and employee retrieval while the Cucumber step definitions orchestrate the business flow.

### OAuth Authentication

The framework obtains an access token using:

```text
POST /oauth2/token
```

The OAuth request uses:

```text
grant_type
code
client_id
client_secret
redirect_uri
```

The returned access token is stored dynamically in `EmployeeApiClient`.

The same Bearer token is then used for subsequent employee API requests.

---

## Employee API Validation Flow

The API validation flow is:

```text
UI creates employee
        |
        v
Employee POJO stores Employee ID
        |
        v
OAuth Token API
        |
        | access_token
        v
Get Employees API
        |
        | search employeeId
        v
Find matching employee
        |
        | capture empNumber
        v
Get Employee Details API
        |
        v
Validate employee information
```

### API 1 – OAuth Token

```text
POST /oauth2/token
```

Obtains the access token used by subsequent API requests.

### API 2 – Get Employees

```text
GET /api/v2/pim/employees
```

The response is searched using the dynamically generated Employee ID.

The corresponding internal `empNumber` is obtained from the API response.

### API 3 – Get Employee Details

```text
GET /api/v2/pim/employees/{empNumber}
```

The internal `empNumber` is used to retrieve the employee details.

The API response is validated against the employee data used by the UI automation.

---

## UI and API Validation

The same employee model is shared between the UI and API layers.

```text
Employee
├── firstName
├── lastName
├── employeeId
├── jobTitle
└── employmentStatus
```

The UI uses the employee data to create and update the employee.

The API layer uses the generated Employee ID to locate the corresponding employee and validate the returned employee information.

---

## API Validation Configuration

API validation can be enabled or disabled using:

```properties
api.validation.enabled=true
```

Enable:

```bash
mvn clean test -Dapi.validation.enabled=true
```

Disable:

```bash
mvn clean test -Dapi.validation.enabled=false
```

When disabled, the UI automation can execute without requiring API validation.

In GitHub Actions, this setting can also be supplied through:

```text
ORANGEHRM_API_VALIDATION_ENABLED
```

---

## Employee Deletion

Employee deletion is performed through the UI.

After deletion, the framework validates the employee removal through the UI.

When API validation is enabled, the framework also verifies that the employee no longer exists through the employee API.

This provides both:

```text
UI Validation
+
Backend API Validation
```

---

## Test Cleanup

The framework contains automatic cleanup logic in `Hooks.java`.

If an employee was created successfully but was not deleted by the scenario, the `@After` hook attempts to delete the employee automatically.

This helps prevent test data from accumulating in the OrangeHRM environment.

The cleanup is designed to avoid masking the original scenario failure.

---

## Wait and Synchronization

The framework uses reusable explicit waits through:

```text
WaitUtils.java
```

The framework avoids unnecessary hard-coded delays.

Explicit waits are used for:

* Element visibility
* Element clickability
* Loader disappearance
* Dynamic UI elements
* OrangeHRM form overlays

This improves stability when interacting with dynamic OrangeHRM UI components.

---

## Retry and Flaky Test Handling

The framework includes:

```text
RetryAnalyzer.java
RetryListener.java
FlakyTestListener.java
```

A failed TestNG test can be retried once.

The framework also logs when:

* A test is retried
* A test passes after retry
* A test still fails after the retry

This helps identify potentially flaky tests without hiding the original failure.

---

## Failure Handling

When a Cucumber scenario fails:

1. The failure is logged.
2. A screenshot is captured.
3. The screenshot is attached to Allure.
4. Test cleanup is attempted when applicable.
5. The browser is closed.

This provides additional information for troubleshooting failed UI scenarios.

---

## Logging

Centralized logging is provided through:

```text
TestLogger.java
```

The framework logs important events such as:

* Scenario start
* Driver initialization
* API authentication
* API response status
* Test retry attempts
* Employee cleanup
* Scenario failures

---

## Allure Reporting

Allure is integrated using the Cucumber 7 adapter.

Generated results are stored under:

```text
allure-results/
```

The generated Allure report can be viewed locally using:

```bash
allure serve allure-results
```

The report can contain:

* Scenario execution details
* Step execution status
* Environment information
* Screenshots
* API response attachments
* Failure information
* Retry information

---

## Cucumber Reporting

Cucumber HTML reporting is generated under:

```text
target/cucumber-reports/
```

GitHub Actions uploads the generated Cucumber reports as workflow artifacts.

---

## Running Tests Locally

### Run the complete test suite

```bash
mvn clean test
```

### Run Smoke tests

```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

### Run Regression tests

```bash
mvn clean test -Dcucumber.filter.tags="@regression"
```

### Run Role-Based tests

```bash
mvn clean test -Dcucumber.filter.tags="@roleBased"
```

### Run using a specific environment

```bash
mvn clean test -Denv=qa
```

### Run in headless mode

```bash
mvn clean test -Dheadless=true
```

### Disable API validation

```bash
mvn clean test -Dapi.validation.enabled=false
```

---

## CI/CD – GitHub Actions

The project uses GitHub Actions for CI execution.

Workflow:

```text
.github/workflows/ci.yml
```

The workflow supports:

* Push to `main`
* Pull requests targeting `main`
* Manual workflow execution
* Environment selection
* Smoke test execution
* Regression test execution
* Maven dependency caching
* Cucumber report artifacts
* Allure result artifacts
* Test artifacts

### Manual Execution

The workflow supports selecting:

```text
Environment:
- dev
- qa
- prod

Test Suite:
- smoke
- regression
```

The selected values are passed to Maven using:

```text
-Denv=<environment>
-Dcucumber.filter.tags="@<test-suite>"
```

### CI Configuration

GitHub Actions supplies sensitive configuration through repository secrets.

The workflow maps GitHub secrets to `ORANGEHRM_*` environment variables before Maven execution.

This allows CI to run without storing credentials or API secrets in the repository.

The local `config.properties` file is not required to contain CI credentials.

---

## Browser Driver Management

The project uses Selenium Manager provided by Selenium.

No separate WebDriverManager dependency is required.

Selenium automatically manages the required browser driver.

---

## Maven

Maven is used for:

* Dependency management
* Compilation
* Test execution
* Build lifecycle management

Run:

```bash
mvn clean test
```

---

## Coding Practices

The framework follows the following practices:

* Page Object Model
* Single Responsibility Principle
* Separation of UI and API layers
* Reusable utility methods
* Base Page abstraction
* Centralized locator utilities
* Explicit waits
* Externalized test data
* Externalized configuration
* Environment-specific configuration
* POJO-based test data handling
* Dynamic Employee ID generation
* Meaningful assertions
* Thread-safe WebDriver management
* Thread-local test context
* Centralized logging
* Automatic test-data cleanup
* Retry handling
* Failure screenshots
* API client abstraction
* Selenium Manager
* No hard-coded secrets in test classes

---

## Test Execution Flow

```text
Cucumber Feature
       |
       v
Hooks - Setup
       |
       v
DriverFactory
       |
       v
Step Definitions
       |
       +--------------------+
       |                    |
       v                    v
Page Objects          EmployeeApiClient
       |                    |
       v                    v
Selenium             REST Assured
       |                    |
       +---------+----------+
                 |
                 v
             Assertions
                 |
                 v
          Scenario Result
                 |
          +------+------+
          |             |
          v             v
      Screenshot      Allure
          |
          v
       Cleanup
          |
          v
      Driver Quit
```

---

## Repository

GitHub repository:

`https://github.com/BalajiG555/OrangeHRM-Employee-Lifecycle-Automation`

---

## Future Enhancements

Potential future improvements include:

* Parallel execution
* Cross-browser execution
* API schema validation
* API response POJOs
* Advanced Allure attachments
* Improved OAuth token management
* API pagination handling
* API validation of Job Title and Employment Status
* Reusable API assertion utilities
* Dockerized Selenium Grid
* Expanded environment-specific CI execution

---

## Author

**Balaji G**

QA Automation Engineer

`Selenium | Java | Cucumber | TestNG | REST Assured | API Automation`