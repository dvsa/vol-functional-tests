@multiple-surrenders
@Surrender
@ss_regression
@gov-sign-in
@FullRegression

Feature: Multiple licence holder

  Background:
    Given I have "2" "goods" "standard_national" licences

  @multiple-surrenders
  Scenario: Surrender multiple licences
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    Then the licence status should be "Surrendered"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve