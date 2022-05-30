Feature: Create an Account

  @WIP
  Scenario: User who has a VOL or application in progress wishes to Create an Account
    Given i have a valid "goods" "standard_national" licence
    And I am on the registration page
    Then I should be able to Create account with my existing licence

  @ss_reg
  @ss_regression
  Scenario: User who does not have a VOL or application in progress wishes to Create an Account
    Given I am on the registration page
    And an Operator with no licence
    Then I should be able to register an account