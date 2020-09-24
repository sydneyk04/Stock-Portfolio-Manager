Feature: Login
  Scenario: Login successfully on login page
    Given I am on the login page
    When I enter my username
    And I enter my password
    And I click the login button
    Then I should be on the dashboard page

  Scenario: Login with an incorrect password on login page
    Given I am on the login page
    When I enter my username
    And I enter an incorrect password
    And I click the login button
    Then I should the error message "Invalid login and/or password"

  Scenario: Login with an empty input field on login page
    Given I am on the login page
    When I enter my username
    And I click the login button
    Then I should the alert "Please fill out this field."
