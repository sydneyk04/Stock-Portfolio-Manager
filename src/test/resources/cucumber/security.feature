Feature: Security
  Scenario: Unauthorized attempt to access dashboard page
    Given I am on the sign up page
    When I attempt to navigate to the dashboard page 
    Then I should be on the login page
    
  Scenario: Auto-logout after 2 minutes on dashboard page
    Given I am logged in on the dashboard page
    When I am on the dashboard page for two minutes
    Then I should be on the login page