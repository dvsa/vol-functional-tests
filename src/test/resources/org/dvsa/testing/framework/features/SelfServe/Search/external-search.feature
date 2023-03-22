@OLCS-20956 @ap
@SS-EXTERNAL-SEARCH
Feature: External user search by Address, Business name, Licence Number and Person's name

  Background:
    Given i have a valid "goods" "standard_national" licence
    And  I am on the external search page

  Scenario: Address external user search for lorry and bus operators
    When I search for a lorry and bus operator by "address","","","",""
    Then search results page addresses should only display address belonging to our post code

  @smoketest
  Scenario: Business name external search for lorry and bus operators
    When I search for a lorry and bus operator by "business","","","",""
    Then search results page should display operator names containing our "businessName"
    And I am able to view the applicants licence number

  Scenario: Licence number external search for lorry and bus operators
    When I search for a lorry and bus operator by "licence","","","",""
    Then search results page should only display our "licenceNumber"
    And I am able to view the licence number

  Scenario: Person's name external search for lorry and bus operators
    When I search for a lorry and bus operator by "person","","","",""
    Then search results page should display names containing our operator name