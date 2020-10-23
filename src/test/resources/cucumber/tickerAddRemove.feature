Feature: Adding and removing stocks from portfolio

  Scenario: Successfully adding new stock to portfolio
  	Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter a stock ticker not in my portfolio and a certain number of shares
    When I click the submit button
    Then I should see the value of my portfolio increase and the stocks in my portfolio be updated
    
  Scenario: Successfully removing shares of a stock from my portfolio
    Given I am logged in on the dashboard page
    And I click the button to remove stocks from my portfolio
    And I enter a stock ticker and number of shares less than the number I have of that stock
    When I click the submit button
    Then I should see the value of my portfolio decrease and the number of shares of that stock in my portfolio be updated
 
  Scenario: Unsuccessfully adding a stock to portfolio
    Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter an invalid stock ticker and number of shares
    When I click the submit button
    Then I should see an error message saying stock ticker was not found
    
  Scenario: Unsuccessfully removing more shares from portfolio than exist
    Given I am logged in on the dashboard page
    And I click the button to remove stocks from my portfolio
    And I enter a stock ticker and number of shares greater than I have of this stock
    When I click the submit button
    Then I should see an error message saying I'm trying to remove more shares than I have
    
  Scenario: Successfully adding shares of a stock already owned
    Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter a stock ticker in my portfolio and a certain number of shares
    When I click the submit button
    Then I should see the value of my portfolio increase and the shares owned updated