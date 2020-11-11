Feature: Using the graph and other basic functionality on dashboard

  Scenario: Successfully selecting date range
    Given I am logged in on the dashboard page
    When I click the button to change the graph date range
    And I select an appropriate date range
    And I click the date range confirm button
    Then I should see the graph date range change
    
  Scenario: Successfully input date range
    Given I am logged in on the dashboard page
    When I enter an appropriate date range
    And I click the date range confirm button
    Then I should see the graph date range change
   
  Scenario: Unsuccessfully input date range
    Given I am logged in on the dashboard page
    When I try to enter an invalid date
    Then I should not be able to click the date
   
  Scenario: Different values show in different colors
    Given I am logged in on the dashboard page
    When I have more than two lines on the graph
    Then I should see two different colored lines
    
  Scenario: Default time unit is 3 months
    Given I am logged in on the dashboard page
    When I have more than two lines on the graph
    Then The default time frame should be 3 months
  
  Scenario: Add and Remove S&P to graph
    Given I am logged in on the dashboard page
    When I click the button to remove the S&P
    And I click the button to add the S&P
    Then I should see the S&P stock added to the graph
    
  Scenario: Portfolio value changes on date change
    Given I am logged in on the dashboard page
   	When I enter an appropriate date range
    And I click the date range confirm button
    Then I should see the graph value update
    
  Scenario: View stock on graph
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    Then I should see the stock on the graph
    
  Scenario: Add invalid element to view stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out incorrect stock info
    Then I should see a view stock error
    
  Scenario: Remove View Stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the remove stock button in view
    Then The stock should be removed
    
  Scenario: Toggle View Stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the toggle stock button in view
    Then The stock should not be shown on the graph
    
  Scenario: Don't enter every field in view stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out some stock info
    Then I should be told to fill out all info
    
  Scenario: Add to portfolio from view stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the Add to Portfolio button in view
    Then I should see the stock in my portfolio
    
 Scenario: Add stock you already own to portfolio from view stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the Add to Portfolio button in view
    Then I should see an error message in my portfolio
    
  Scenario: Calculate stock in portfolio value
    Given I am logged in on the dashboard page
   	When I click calculate in portfolio
    Then the portfolio value should go down
    
  Scenario: Select all stocks to view on graph
  	Given I am logged in on the dashboard page
  	When I click the selectall button
  	Then I should see all stocks displayed on the graph
  	And I should see all stocks displayed in the view stocks list
  	
  Scenario: Deselect all stocks to view on graph
  	Given I am logged in on the dashboard page
  	When I click the deselectall button
  	Then I should see no stocks displayed on the graph
  	And I should see no stocks displayed in the view stocks list
