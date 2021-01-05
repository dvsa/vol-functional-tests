@multiple-surrenders
@SS
@Surrender
@ss_regression
@gov-verify

Feature: Multiple licence holder

  Background:
    Given I have all "goods" "standard_national" traffic area licences

  Scenario: Surrender multiple licences
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    Then the licence status should be "surrendered"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve