Feature: Home
  Scenario: Logout successfully on home page
    Given I am logged in on the home page
    When I click the logout button
    Then I should be on the login page

  Scenario: Navigate to Portfolio Performance from home page
    Given I am logged in on the home page
    When I click the Portfolio Performance button
    Then I should be on the Portfolio Performance page

  Scenario: Navigate to Portfolio Prediction from home page
    Given I am on the home page
    When I enter my username
    And I click the login button
    Then I should see the alert "Please fill out this field."

  Scenario: Portfolio value on the home page
    Given I am on the home page
    When I go to the Portfolio Value section
    Then I should see the total value of my stock portfolio.

  Scenario: Nonempty portfolio on the home page
    Given I am on the home page
    When I have stocks in the portfolio
    And I go to the Portfolio section
    Then I should see a table of stocks in my portfolio.

  Scenario: Empty portfolio on the home page
    Given I am on the home page
    When I have no stocks in the portfolio
    And I go to the Portfolio section
    Then I should see the message "Your stock portfolio is empty."

  Scenario: Navigate to a stock in user portfolio from home page
    Given I am on the home page
    When I click a stock in the user portfolio table
    Then I should be on the stock page