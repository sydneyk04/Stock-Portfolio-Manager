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

  Scenario: Add and Remove S&P to graph
    Given I am logged in on the dashboard page
    When I click the button to remove the S&P
    And I click the button to add the S&P
    Then I should see the S&P stock added to the graph
    
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