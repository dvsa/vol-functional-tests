@INT-EXTERNAL-SEARCH
@INT-SMOKE
Feature: External user search by Address, Business name, Licence Number and Person's name

  Scenario Outline: Licence number external search for lorry and bus operators
    When i have a self serve account
    And  I am on the external search page
    And I search for a lorry and bus operator by "<searchType>" with licence number "<licenceNumber>", business name "<businessName>", person "<person>" and address "<address>"
    Then search results page should only display our "<licenceNumber>"

    Examples:
      | searchType | businessName              | licenceNumber | person    | address             |
      | business   | ROSEDENE CONSTRUCTION LTD | OK0230736     | JOHN RYAN | UNIT 4 TRIPES FARM, |
      | licence    |                           | OK0230736     |           |                     |
      | person     |                           |               | JOHN RYAN |                     |
      | address    |                           |               |           | EC1A 1BB            |

  Scenario: Partner user external search
    When i have a self serve account
    And i login as a partner user
    And i navigate to partner vehicle search
    And i search for a vehicle
    Then the expected licence results should be shown