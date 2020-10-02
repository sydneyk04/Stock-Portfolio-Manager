Feature: Stock Performance
  Scenario: Navigate to Home Page
    Given I am logged in on the stock performance page
    When I click the top banner
    Then I should be on the home page
    
  Scenario: Adjust date to view past performance  
    Given I am logged in on the stock performance page
    When I click the drop down list
    Then the graph should re-adjust

 