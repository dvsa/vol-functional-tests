@PREP-SMOKE
@PREP-EXTERNAL-SEARCH

Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given I log into prep "self serve" account with user "prepUser"

  @PREP-EXTERNAL-SEARCH
  Scenario Outline: Licence number external search for lorry and bus operators
    When I am on the external search page
    And I search for a lorry and bus operator by "<searchType>" with licence number "<licenceNumber>", business name "<businessName>", person "<person>" and address "<address>"
    Then search results page should only display our "<licenceNumber>"

    Examples:
      | searchType | businessName | licenceNumber | person        | address        |
      | business   | TEST PHAS 5  | OC1057274     |               |                |
      | licence    |              | OC1057274     |               |                |
      | person     |              | OC1057274     | DOB DATE TEST |                |
      | address    |              | PC1057277    |               | LEEDS, LS9 6NF |

  Scenario: Partner user external search
    When i login as a partner user
    And i search for a vehicle
    Then the expected licence results should be shown