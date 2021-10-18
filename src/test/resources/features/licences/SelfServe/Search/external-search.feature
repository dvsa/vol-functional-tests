@OLCS-20956
@SS
@SS-EXTERNAL-SEARCH
@ss_regression

Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i login as a partner user
    And I am on the external search page

  Scenario: [Positive]Search for lorry and bus operators by Address
    When I search for a lorry and bus operator by "address"
    Then search results page addresses should only display address belonging to our post code

  @smoketest
  Scenario: [Positive]Search for lorry and bus operators by Business name
    When I search for a lorry and bus operator by "business"
    Then search results page should display operator names containing our business name
    And I am able to view the applications license number

  Scenario: [Positive]Search for lorry and bus operators by Licence number
    When I search for a lorry and bus operator by "licence"
    Then search results page should only display our licence number
    And I am able to view the license number

  Scenario: [Positive]Search for lorry and bus operators by Person's name
    When I search for a lorry and bus operator by "person"
    Then search results page should display names containing our operator name
