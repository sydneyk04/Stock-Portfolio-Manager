Feature: Using the graph and other basic functionality on dashboard

   Scenario: Select all stocks to view on graph
  	Given I am logged in on the dashboard page
  	When I click the selectall button
  	Then I should see the portfolio performance on the graph and the total portfolio value

  Scenario: Add to portfolio from view stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the Add to Portfolio button in view
    Then I should see the stock in my portfolio
    

 Scenario: Different values show in different colors
    Given I am logged in on the dashboard page
    When I have more than two lines on the graph
    Then I should see two different colored lines

  Scenario: Click remove view stock but then cancel
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the remove stock button in view
    And I click the cancel delete stock button in view
    Then I should see the stock on the graph
    
  Scenario: Remove View Stock
    Given I am logged in on the dashboard page
   	When I click the view stock button
    And I fill out correct stock info
    And I click the remove stock button in view
     And I click the delete stock button in view
    Then The stock should be removed

 Scenario: Successfully selecting date range (1y)
    Given I am logged in on the dashboard page
    When I click the button to change the graph date range
    And I select an appropriate date range
    And I click the date range confirm button
    Then I should see the graph date range change
    
  Scenario: Successfully input date range
    Given I am logged in on the dashboard page
    When I click the button to change the graph date range
    When I enter an appropriate date range
    And I click the date range confirm button
    Then I should see the graph date range change

  Scenario: Unsuccessfully input date range
    Given I am logged in on the dashboard page
    When I click the button to change the graph date range
    When I enter a sell date after purchase date
    And I click the date range confirm button
    Then I should see an error message under calendar
    
  Scenario: Enter invalid date range with purchase date before 1y
  	Given I am logged in on the dashboard page
  	When I click the button to change the graph date range
  	When I enter a date range with purchase date before 1y
  	And I click the date range confirm button
  	Then I should see an error message under calendar
  
 

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
   	When I click the button to change the graph date range
    And I select an appropriate date range
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

  

  Scenario: Deselect all stocks to view on graph
  	Given I am logged in on the dashboard page
  	When I click the deselectall button
  	Then I should see zero for the portfolio performance on the graph and the portfolio value