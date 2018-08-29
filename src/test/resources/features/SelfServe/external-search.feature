@OLCS-20956
  @SS-EXTERNAL-SEARCH
  @Business Need: External users should be to search by Address, Business name, Licence Number and Person's name

Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given i have a valid "goods" licence

  Scenario: Search for lorry and bus operators by Address
    Given I am on the external search page
    When I search for a lorry and bus operator by "address"
    Then search results page addresses should only contain the correct address

  Scenario: Search for lorry and bus operators by Business name
    Given I am on the external search page
    When I search for a lorry and bus operator by "business"
    Then search results page should display operator names containing our business name

  Scenario: Search for lorry and bus operators by Licence number
    Given I am on the external search page
    When I search for a lorry and bus operator by "licence"
    Then search results page should only display our licence number

  Scenario: Search for lorry and bus operators by Person's name
    Given I am on the external search page
    When I search for a lorry and bus operator by "person"
    Then search results page should display names containing our operator name