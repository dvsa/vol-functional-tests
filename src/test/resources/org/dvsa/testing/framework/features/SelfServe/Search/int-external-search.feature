@INT-EXTERNAL-SEARCH
Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
      Given i have a self serve account
      And  I am on the external search page

  @INT-SMOKE
  Scenario: Business name external search for lorry and bus operators
    When I search for a lorry and bus operator by "business"
    Then search results page should display operator names containing our business name
