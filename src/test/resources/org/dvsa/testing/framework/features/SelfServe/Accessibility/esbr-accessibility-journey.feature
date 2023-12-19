@accessibility @ss_regression
Feature: Check the EBSR journey is accessibile

  Scenario Outline: Scan for accessibility violations
    Given I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "41" days notice with axeScanner <scanOrNot>
    Then A short notice flag should be displayed in selfserve
    And Documents are generated with axeScanner <scanOrNot>
    Then no issues should be present on the page

    Examples:
      |  | scanOrNot |
      |  | true      |

