Feature: Employee Lifecycle Management

  @smoke
  Scenario: Login with valid credentials
    Given I login to OrangeHRM with valid credentials
    Then I should be redirected to the dashboard

  @regression
  Scenario: Create a new employee
    Given I login to OrangeHRM with valid credentials
    And I should be redirected to the dashboard
    When I navigate to the Add Employee page
    And I create a new employee using test data
    Then the employee should be created successfully

  @regression
  Scenario: Update employee Job Title and Employment Status
    Given I login to OrangeHRM with valid credentials
    And I navigate to the Add Employee page
    And I create a new employee using test data
    Then the employee should be created successfully
    When I search for the employee using Employee ID
    And I update the employee Job Title and Employment Status
    Then the employee details should be updated successfully

  @regression
  Scenario: Validate employee details through API
    Given I login to OrangeHRM with valid credentials
    And I navigate to the Add Employee page
    And I create a new employee using test data
    Then the employee should be created successfully
    When I search for the employee using Employee ID
    And I update the employee Job Title and Employment Status
    Then the employee details should be updated successfully
    When I validate the employee details through API
    Then the API employee details should match the UI details

  @regression
  Scenario: Delete an employee
    Given I login to OrangeHRM with valid credentials
    And I navigate to the Add Employee page
    And I create a new employee using test data
    Then the employee should be created successfully
    When I delete the employee
    Then the employee should no longer exist in the UI
    And the employee should no longer exist through API

  @smoke
  Scenario: Logout successfully
    Given I login to OrangeHRM with valid credentials
    When I logout
    Then I should be redirected to the login page

  @regression @roleBased
  Scenario: Validate ESS role access
    Given I login to OrangeHRM with ESS credentials
    Then the ESS user should be redirected to the dashboard
    And the ESS user should not have access to the Admin module