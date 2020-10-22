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
    Given I am logged in on the home page
    When I click the Portfolio Prediction button
    Then I should be on the Portfolio Prediction page

  Scenario: Portfolio value on the home page
    Given I am logged in on the home page
    When I go to the Portfolio Value section
    Then I should see the total value of my stock portfolio

  Scenario: Nonempty portfolio on the home page
    Given I am logged in on the home page
    When I go to the Portfolio section
    Then I should see a table of stocks in my portfolio.

  Scenario: Empty portfolio on the home page
    Given I am logged in with an empty portfolio on the home page
    When I go to the Portfolio section
    Then I should see the portfolio message "Your portfolio is empty."