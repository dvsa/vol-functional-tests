@INT-EXTERNAL-SEARCH
@INT-SMOKE
Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
      Given i have a self serve account
      And  I am on the external search page

  Scenario Outline: Licence number external search for lorry and bus operators
    When I search for a lorry and bus operator by "<searchType>" with licence number "<licenceNumber>", business name "<businessName>", person "<person>" and address "<address>"
    Then search results page should only display our "<licenceNumber>"

    Examples:
      | searchType | businessName                | licenceNumber | person        | address                         |
      | business   | ROSEDENE CONSTRUCTION LTD   | OK0230736     | JOHN RYAN     | UNIT 4 TRIPES FARM,             |
      | licence    |                            | OK0230736     |               |                                 |
      | person     |                            |               | JOHN RYAN     |                                 |
      | address    |                            |               |               | EC1A 1BB                        |
