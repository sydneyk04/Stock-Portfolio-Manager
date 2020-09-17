Feature: Login
  Scenario: Login successfully on login page
    Given I am on the login page
    When I enter my email
    And I enter my password
    And I click the login button
    Then I should be on the dashboard page

  Scenario: Login with an incorrect on login page
    Given I am on the login page
    When I enter my email
    And I enter an incorrect password
    And I click the login button
    Then I should the error message "The email address or password you entered is incorrect."

  Scenario: Login with an invalid email on login page
    Given I am on the login page
    When I enter an invalid email
    And I click the login button
    Then I should the alert "Please include an '@' in the email address."

  Scenario: Login with an empty input field on login page
    Given I am on the login page
    When I enter my email
    And I click the login button
    Then I should the alert "Please fill out this field."
