@accessibility
Feature: Check that Submissions journey is accessible

  Background:
    Given i have a valid "goods" "standard_national" licence

  Scenario Outline: Scan for accessibility violations
    And my application to surrender is under consideration with axeScanner <scanOrNot>
    When the caseworker approves the surrender
    Then the licence status should be "surrendered"
    And the surrender menu should be hidden in internal
    And the licence should not displayed in selfserve
    Then no issues should be present on the page


    Examples:
      |  | scanOrNot |
      |  | true      |

