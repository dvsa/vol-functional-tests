@forgotten_username
#@ss_regression
@FullRegression

  Feature: User that has forgotten their username sends request

    Scenario: User requests forgotten username
      Given I have a "goods" "standard_international" licence
      When I have forgotten my username and want it to be sent
      Then I will be sent an email with my username
