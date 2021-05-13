#https://jira.dvsacloud.uk/browse/VOL-147

@VOL-147
Feature: Search and add a vehicle

  Background:
    Given I have "1" "goods" "standard_national" licences with "0" vehicles and a vehicleAuthority of "5"

  Scenario Outline: Check page contents
    When I navigate to manage vehicle page on a licence
    And choose to add a "<VRM>" vehicle
    Then the "<VRM>" should be displayed on the page

    Examples:
      | VRM     |
      | E304MSG |

  Scenario: Check error messages
    When I navigate to manage vehicle page on a licence
    And I search without entering a registration number
    Then An error message should be displayed