Feature: Adding and removing stocks from portfolio


  Scenario: Successfully adding new stock to portfolio
  	Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter a stock ticker not in my portfolio and a certain number of shares
    When I click the submit button
    Then I should see the value of my portfolio increase and the stocks in my portfolio be updated
    
  Scenario: Unsuccessfully adding a stock to portfolio
    Given I am logged in on the dashboard page
    And I click the button to add stocks to my portfolio
    And I enter an invalid stock ticker and number of shares
    When I click the submit button
    Then I should see an error message saying stock ticker was not found
    

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
  	
  	