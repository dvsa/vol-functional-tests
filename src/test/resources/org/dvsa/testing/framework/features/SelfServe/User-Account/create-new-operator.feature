@create_account @consultant @ss_regression @FullRegression
Feature: Create an Account

  Background:
    Given I am on the registration page

  Scenario: User who has a VOL licence or application in progress wishes to Create an Account
    And I have an existing application or licence
    Then I should be advised that I cannot create a new account

  Scenario: Consultant creates new VOL accounts for them and the operator
    And a Consultant creating accounts on behalf of the operator
    Then accounts should be registered for both Operator and Consultant

  @localsmoke
  @smoke @testRerun
  Scenario: User who does not have a VOL or application in progress wishes to Create an Account
    And an Operator with no licence
    Then I should be able to register an account
