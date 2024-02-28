@accessibility @ss_regression
Feature: Check the EBSR journey is accessibile

  Scenario:Scan for accessibility violations
    Given I have a psv application with traffic area "west" and enforcement area "west" which has been granted
    When I upload an ebsr file with "41" days notice
    Then A short notice flag should be displayed in selfserve
    And Documents are generated
    Then no issues should be present on the page

