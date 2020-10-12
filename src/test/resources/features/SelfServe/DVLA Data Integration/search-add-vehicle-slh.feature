#https://jira.dvsacloud.uk/browse/VOL-147

@VOL-147
Feature: Search and add a vehicle

  Scenario Outline: Check page contents
    Given I have applied for a "<Operator>" "<LicenceType>" licence
    When I navigate to manage vehicle page
    And choose to add a vehicle
    Then the add vehicle page should display licence number
    And "Add a vehicle" heading
    Examples:
      | Operator | LicenceType       |
      | goods    | standard_national |

  Scenario Outline: Check error messages
    Given I have applied for a "<Operator>" "<LicenceType>" licence
    When I navigate to manage vehicle page
    And I search without entering a registration number
    Then An error message should be displayed
    Examples:
      | Operator | LicenceType       |
      | goods    | standard_national |

  Scenario Outline: Search for a vehicle registration mark
    Given I have applied for a "<Operator>" "<LicenceType>" licence
    When I navigate to manage vehicle page
    When I search for a valid "<vrm>" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

    Examples:
      | Operator | LicenceType       | vrm     |
      | goods    | standard_national | F95 JGE |