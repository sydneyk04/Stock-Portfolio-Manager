Feature: Adding and removing stocks from portfolio


  Scenario: Successfully adding new stock to portfolio
  	Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter a stock ticker not in my portfolio and a certain number of shares
    And I click the submit button
    Then I should see the value of my portfolio increase and the stocks in my portfolio be updated
    
  Scenario: Unsuccessfully adding a stock to portfolio with bad ticker
    Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter an invalid stock ticker and number of shares
    And I click the submit button
    Then I should see an error message saying stock ticker was not found
    
  Scenario: Unsuccessfully adding a stock to portfolio with bad quantity
   	Given I am logged in on the dashboard page
   	And I click the button to add stocks to my portfolio
   	And I enter an invalid quantity of shares
   	And I click the submit button
   	Then I should see an error message saying invalid number of shares
   	
  Scenario: Unscueesfully adding a stock to portfolio with no date purchased
  	Given I am logged in on the dashboard page
  	And I click the button to add stocks to my portfolio
  	And I do not enter a purchase date
  	And I click the submit button
  	Then I should see an error message saying I need to enter a purchase date
  	
  Scenario: Unsucessfully adding a stock to portfolio with invalid sold date
  	Given I am logged in on the dashboard page 
  	And I click the button to add stocks to my portfolio
  	And I enter a sold date earlier than the purchase date
  	And I click the submit button
  	Then I should see an error message saying my sold date is invalid
    
  Scenario: Successfully delete a stock from portfolio
  	Given I am logged in and on the dashboard page
  	And I click the button to remove stocks from my portfolio
  	And i confirn that I want to delete the stock
  	Then I should be on the dashboard page without the stock

  Scenario: Successfully add stocks using a CSV file 
  	Given I am logged in on the dashboard page
  	And I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file
  	And I click the button to upload the file
  	Then I should see the new stocks added
  	
  Scenario: Download the example CSV file 
  	Given I am logged in on the dashboard page
  	And I click the button to add stocks to my portfolio using a CSV
  	And I click the button to download an example CSV file
  	Then I should see a file downloaded 
  	
  	