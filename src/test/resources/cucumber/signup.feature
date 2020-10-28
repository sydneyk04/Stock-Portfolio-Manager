Feature: Sign Up
  Scenario: Signup successfully
    Given I am on the sign up page
    When I enter a username
    And I enter the first hidden password field
    And I enter the second hidden password field
    And I click the sign up button
    Then I should be redirected to the login page

  Scenario: Leave a field blank when creating account
    Given I am on the sign up page
    When I enter a username
    And I leave a password field blank
    And I click the sign up button
    Then I should see an alert "Please fill out this field."

  Scenario: Passwords don't match when registering
    Given I am on the sign up page
    When I enter a username
    And I enter the first hidden password field
    And I enter the second hidden password field incorrectly
    And I click the sign up button
    Then I should the alert "Please ensure that your passwords match."

  Scenario: Username already taken
    Given I am on the sign up page
    When I enter an existing username
    And I enter the first hidden password field
    And I enter the second hidden password field
    And I click the sign up button
    Then I should see an existing username error message "That account already exists! Please try again."

  Scenario: Click the cancel button
    Given I am on the sign up page
    When I click the cancel button
    Then I should be redirected to the login page