Feature: Prediction value of portfolio
  Scenario: Get future value of portfolio
    Given I am on the predict page
    When I choose a future date
    Then I will see the predicted portfolio value

  Scenario: Go to home page
  	Given I am on the predict page
  	When I click the top banner in predict page
  	Then I should be on the home page 