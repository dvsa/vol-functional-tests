@forgotten_username
@ss_regression

  Feature: User that has forgotten their username sends request

    Scenario: User requests forgotten username
      Given i have a valid "public" "standard_international" licence
      When I have navigated to the Forgotten User page
      Then I complete the forgotten username process





