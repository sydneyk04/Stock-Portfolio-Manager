Feature: Graph Zoom capability
  Scenario: Zoom in and out on graph date range
    Given I am logged in on the dashboard page
    When I drag my cursor across the graph 
    And I click the reset button
    Then I should see the graph date reset to three months 
  
  Scenario: Close view stock addition
    Given I am logged in on the dashboard page
    When I click the view stock button
    And I click the close button
    Then I should not see a stock added to the graph
   
   Scenario: Cancel add stock to portfolio
    Given I am logged in on the dashboard page
    When I click the Add Stock button for the portfolio
    And I click the cancel add stock button
    Then I should not see a stock added to my portfolio
 
    
