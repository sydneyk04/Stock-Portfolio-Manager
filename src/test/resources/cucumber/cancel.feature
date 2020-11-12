Feature: Remove stock from add or view then cancel
  Scenario: Cancel remove stock from portfolio
    Given I am logged in on the dashboard page
    When I remove a stock in the user portfolio
    And I click the cancel remove button
    Then the stock should not be removed from my portfolio

    
