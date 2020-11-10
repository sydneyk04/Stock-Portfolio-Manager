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