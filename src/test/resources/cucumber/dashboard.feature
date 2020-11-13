Feature: Dashboard
  Scenario: Logout successfully on dashboard page
    Given I am logged in on the dashboard page
    When I click the logout button
    Then I should be on the login page    