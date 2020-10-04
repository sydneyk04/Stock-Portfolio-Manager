Feature: Portfolio Performance
  Scenario: Navigate to Home Page
    Given I am logged in on the Portfolio Performance page
    When I click the top banner of the Portfolio Performance page
    Then I should be on the home page

  Scenario: Adjust date to view past performance  
    Given I am logged in on the Portfolio Performance page
    When I click the drop down list of the Portfolio Performance page.
    Then the graph should re-adjust on the Portfolio Performance page.