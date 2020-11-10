Feature: Adding and removing stocks from portfolio

  Scenario: Successfully adding a new stock to portfolio
  	Given I am logged in on the dashboard page
    When I click the Add Stock button for the portfolio
    And I enter a stock ticker not in my portfolio and a certain number of shares
    And I click the Add Stock button in the popup window for the portfolio
    Then I should see the value of my portfolio increase and the stocks in my portfolio be updated
 
  Scenario: Portfolio value increases and percentage changes after adding a stock
    Given I am logged in on the dashboard page
    When I click the Add Stock button for the portfolio
    And I add a new stock to the user portfolio
    Then I should see the portfolio value increase and the percentage change 
    
  Scenario: Unsuccessfully adding a stock to portfolio with bad ticker
    Given I am logged in on the dashboard page
    When I click the Add Stock button for the portfolio
    And I enter an invalid stock ticker and number of shares
	And I click the Add Stock button in the popup window for the portfolio
    Then I should see an error message saying stock ticker was not found
    
  Scenario: Unsuccessfully adding a stock to portfolio with bad quantity
   	Given I am logged in on the dashboard page
   	When I click the Add Stock button for the portfolio
   	And I enter an invalid quantity of shares
   	And I click the Add Stock button in the popup window for the portfolio
   	Then I should see an error message saying invalid number of shares
   	
  Scenario: Unsuccessfully adding a stock to portfolio with no date purchased
  	Given I am logged in on the dashboard page
  	When I click the Add Stock button for the portfolio
  	And I do not enter a purchase date
  	And I click the Add Stock button in the popup window for the portfolio
  	Then I should see an error message saying I need to enter a purchase date
  	
  Scenario: Unsuccessfully adding a stock to portfolio with invalid sold date
  	Given I am logged in on the dashboard page 
  	When I click the Add Stock button for the portfolio
  	And I enter a sold date earlier than the purchase date
  	And I click the Add Stock button in the popup window for the portfolio
  	Then I should see an error message saying my sold date is invalid
    
  Scenario: Remove a stock successfully
    Given I am logged in on the dashboard page
    When I remove a stock in the user portfolio with confirmation
    Then I should see the value of my portfolio decrease and my portfolio without the stock

  Scenario: Portfolio value decreases and percentage changes after removing a stock
    Given I am logged in on the dashboard page
    When I remove a stock in the user portfolio with confirmation
    Then I should see the portfolio value decrease and the percentage change

  Scenario: Successfully add stocks using a CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file
  	And I click the button to upload the file
  	Then I should see the new stocks added
  	
  Scenario: Download the example CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I click the button to download an example CSV file
  	Then I should see a file downloaded 
  	
  	