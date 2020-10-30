Feature: Dashboard
  Scenario: Logout successfully on dashboard page
    Given I am logged in on the dashboard page
    When I click the logout button
    Then I should be on the login page
    
  Scenario: Auto-logout after 2 minutes on dashboard page
    Given I am logged in on the dashboard page
    When I am on the dashboard page for two minutes
    Then I should be on the login page