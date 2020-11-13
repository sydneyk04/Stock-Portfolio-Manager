Feature: Adding stocks to portfolio via CSV
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
  	
  Scenario: Unsuccessfully add stocks with invalid ticker using a CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file with invalid ticker
  	And I click the button to upload the file
  	Then I should see an error message about the invalid ticker in the CSV
  	
  Scenario: Unsuccessfully add stocks with invalid number of shares using a CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file with invalid number of shares
  	And I click the button to upload the file
  	Then I should see an error message about the invalid number of shares in the CSV
  	
  Scenario: Unsuccessfully add stocks with missing purchase date using a CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file with missing purchase date
  	And I click the button to upload the file
  	Then I should see an error message about the missing purchase date in the CSV
  	
  Scenario: Unsuccessfully add stocks with sell date before purchase date using a CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file with sell date before purchase date
  	And I click the button to upload the file
  	Then I should see an error message about the sell date before purchase date in the CSV
  	
  Scenario: Unsuccessfully add stocks with malformed date in CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file with malformed date
  	And I click the button to upload the file
  	Then I should see an error message about the malformed date in the CSV
  	
  Scenario: Unsuccessfully add stocks with malformed CSV file 
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose a CSV file with extra entries
  	And I click the button to upload the file
  	Then I should see an error message about the malformed CSV file	
  	
  Scenario: Unsuccessfully add stocks with empty CSV file
  	Given I am logged in on the dashboard page
  	When I click the button to add stocks to my portfolio using a CSV
  	And I choose an empty CSV file
  	And I click the button to upload the file
  	Then I should see an error message about the empty CSV file