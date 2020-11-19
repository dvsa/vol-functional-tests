#https://jira.dvsacloud.uk/browse/VOL-147

@VOL-147
Feature: Search and add a vehicle for a multi licence holder

  Background:
    Given I have applied for "standard_national" "goods" licences

  @VOL-933
  Scenario: Check page contents for Single Licence holder
    When I navigate to manage vehicle page
    Then the following should be displayed:
      | Transfer vehicles |

  Scenario: Check error messages
    When I navigate to manage vehicle page
    And I search without entering a registration number
    Then An error message should be displayed

  Scenario Outline: Search for a vehicle registration mark
    When I navigate to manage vehicle page
    When I search for a valid "<VRM>" registration
    Then the vehicle summary should be displayed on the page:
      | Vehicle information       |
      | Vehicle Registration Mark |
      | Gross plated weight in kg |
      | Make                      |
    And the vehicle details should not be empty

    Examples:
      | VRM     |
      | F95 JGE |

    Scenario: Add an a vehicle belonging to another licence
      When I navigate to manage vehicle page
      And I add a vehicle belonging to another licence
      Then I should be prompted that vehicle belongs to another licence

