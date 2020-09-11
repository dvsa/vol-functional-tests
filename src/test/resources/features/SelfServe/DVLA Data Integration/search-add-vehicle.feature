# https://jira.dvsacloud.uk/browse/VOL-147

@VOL-147
@VOL-147
Feature: Search and add a vehicle

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

  Scenario Outline: Check error messages
    Given I have a new "<Operator>" application
    When I navigate to manage vehicle page
    And I search without entering a registration number
    Then An error message should be displayed
    Examples:
      | Operator |
      | goods    |
      | public   |

#  Scenario Outline:
#    Given I have a new "<Operator>" application
#    Examples:
#      | Operator |
#      | goods    |
#    When I navigate to manage vehicle page
#    When I search for a valid "<vrm>" registration
#    Examples:
#      | vrm      |
#      | VR4 5DFG |
#    Then the vehicle details should be displayed on the page:
#      | Vehicle information       |
#      | Vehicle Registration Mark |
#      | Gross plated weight in kg |
#      | Make                      |