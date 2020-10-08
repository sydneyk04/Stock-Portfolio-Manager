Feature: notfound page functionality
  
  Scenario: Go to homepage
    Given I am on the notfound page
    When I click the top banner
    Then I should be on the home page

  Scenario: Search a valid symbol
    Given I am on the notfound page
    When I enter "GOOG" in the search bar
    Then I should be on the goog stock page

  Scenario: Search an invalid symbol
  	Given I am on the notfound page
  	When I enter "1234" in the search bar
  	Then I should be on the notfound page