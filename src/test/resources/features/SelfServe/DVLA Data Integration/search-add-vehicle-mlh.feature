#https://jira.dvsacloud.uk/browse/VOL-147

@VOL-147
Feature: Search and add a vehicle for a multi licence holder

  Scenario Outline: Check page contents
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    And choose to add a vehicle
    Then the add vehicle page should display licence number
    And "Add a vehicle" heading
    Examples:
      | Operator |
      | goods    |
      | public   |

  @VOL-933
  Scenario Outline: Check page contents for Single Licence holder
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    Then the following should be displayed:
      | Transfer vehicles |
    Examples:
      | Operator |
      | goods    |

  Scenario Outline: Check error messages
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    And I search without entering a registration number
    Then An error message should be displayed
    Examples:
      | Operator |
      | goods    |
      | public   |

  Scenario Outline: Search for a vehicle registration mark
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    When I search for a valid "<vrm>" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

    Examples:
      | Operator | vrm     |
      | goods    | F95 JGE |
