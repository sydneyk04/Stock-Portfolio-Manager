Feature: Landing Page
  Scenario: Navigate to login page
    Given I am on the landing page
    When I click the "Login" button
    Then I should be brought to the "Login" page

  Scenario: Navigate to sign up page
    Given I am on the landing page
    When I click the "Sign Up" button
    Then I should be brought to the "Sign up" page
