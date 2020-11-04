Feature: Security
  Scenario: Lock out after three failed logins 
    Given I am on the login page
    When I log in with my username and incorrect password three times
    And I log in with my username and correct password
    Then I should see the lockout error message "Account locked for 1 minute"
  Scenario: Valid log in after one invalid login
    Given I am on the login page
    When I enter my username
    And I enter an incorrect password
    And I click the login button
    When I enter my username
    And I enter my password
    And I click the login button
    Then I should be on the dashboard page
  Scenario: Access dashboard without being logged in
    Given I am on the login page
    And I try to access the dashboard page without logging in
    Then I should be redirected to login page
  Scenario: Password is hashed on log in
    Given I am on the login page
    When I enter my username
    And I enter my password
    Then my password should not match the hash stored in the user database
  Scenario: Use https
    Given I am on the landing page
    When I enter in the same URL using https
    Then I should be redirected to the https landing page

  
    
    