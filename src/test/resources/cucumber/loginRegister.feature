Feature: Login & Register

  Scenario: Unsuccessfully logging in with an existing account
    Given I am on the log in page
    And I input my correct username but an incorrect password
    When I click the login button
    Then I should see an error message saying I've entered an incorrect password
    
  Scenario: Successfully logging in with an existing account
    Given I am on the log in page
    And I input my correct username and password
    When I click the login button
    Then I should see my portfolio dashboard
    
  Scenario: Trying to log in with a username that does not exist
    Given I am on the log in page
    And I input a username not registered and a password
    When I click the login button
    Then I should see a message redirecting me to the signup page
    
  Scenario: Passwords don't match when registering
    Given I am on the register page
    And I type in a unique username and a password
    And I type in the confirmation password incorrectly
    When I click the register button
    Then I should see a message saying that my passwords don't match
    
  Scenario: Username already taken
    Given I am on the register page
    And I type in an already used username and the password and confirmation password
    When I click the register button
    Then I should see a message saying that my username is already taken

  Scenario: Login with an empty input field on login page
    Given I am on the login page
    When I enter my username
    And I click the login button
    Then I should see the alert "Please fill out this field."

  Scenario: Click the signup-here hyperlink
    Given I am on the login page
    When I click the Signup here hyperlink
    Then I should be redirected to the signup page
    
 Scenario: Leave a field blank when creating account
    Given I am on the sign up page
    When I enter a username
    And I leave a password field blank
    And I click the sign up button
    Then I should see an alert "Please fill out this field."

 Scenario: Re-enter the password incorrectly
    Given I am on the sign up page
    When I enter a username
    And I enter the first hidden password field
    And I enter the second hidden password field incorrectly
    And I click the sign up button
    Then I should the alert "Please ensure that your passwords match."

 Scenario: Click the cancel button
    Given I am on the sign up page
    When I click the cancel button
    Then I should be redirected to the login page