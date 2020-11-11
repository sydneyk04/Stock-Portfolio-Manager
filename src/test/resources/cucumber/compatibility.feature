Feature: Web App Compatibility
  Scenario: Resize window on sign up page
    Given I am on the sign up page
    When I resize the window to 75%
    And I click the cancel button
    Then I should be on the login page with full view of the top banner
    
  Scenario: Resize window on login page
    Given I am on the login page
    When I resize the window to 75%
    And I click the Signup here hyperlink
    Then I should be redirected to the signup page with full view of the top banner

  Scenario: Resize window on dashboard page
    Given I am logged in on the dashboard page
    When I resize the window to 75% 
    Then I should have full view of the top banner and graph

  Scenario: Logout successfully on resized window of dashboard page
    Given I am logged in on the dashboard page
    When I resize the window to 75% 
    And I click the logout button
    Then I should be on the login page