Feature: Prediction value of portfolio
  Scenario: Get future value of portfolio
    Given I am on the predict page
    When I choose a future date
    Then I will see the predicted portfolio value
