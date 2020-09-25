Feature: Sign Up
  Scenario: Successful sign up on sign up page
    Given I am on the sign up page
    When I enter a username
    And I enter the first hidden password field
    And I enter the second hidden password field
    And I click the sign up button
    Then I should be brought to the dashboard page

  Scenario: Leave a field blank when creating account
    Given I am on the sign up page
    When I enter a username
    And I leave a password field blank
    And I click the sign up button
    Then I should see an alert "Please fill out this field."

 Scenario: Re-enter the password incorrectly
    Given I am on the sign up page
    When I enter a username
    And I enter the first hidden password field
    And I enter the second hidden password field incorrectly
    And I click the sign up button
    Then I should the alert "Please ensure that your passwords match."
