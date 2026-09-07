Feature: Employee Lifecycle Management

  @employeeLifecycle
  Scenario: Create, update, validate and delete an employee

    Given I login to OrangeHRM with valid credentials

    Then I should be redirected to the dashboard

    When I navigate to the Add Employee page

    And I create a new employee using test data

    Then the employee should be created successfully

    When I search for the employee using Employee ID

    And I update the employee Job Title and Employment Status

    Then the employee details should be updated successfully

    When I validate the employee details through API

    Then the API employee details should match the UI details

    When I delete the employee

    Then the employee should no longer exist in the UI

    And the employee should no longer exist through API

    When I logout

    Then I should be redirected to the login page