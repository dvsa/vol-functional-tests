@accessibility
@user-check-accessibility
Feature: Check that surrender journey is accessible

  Scenario Outline: Scan for accessibility violations
    Given as a "<user_type>" I have a valid "goods" "standard_national" licence
    And my application to surrender is under consideration
    When the caseworker approves the surrender
    Then the licence status should be "Surrendered"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve
    And i scan for accessibility violations
    Then no issues should be present on the page
    Examples:
      | user_type |
      | admin     |
      | consultant|