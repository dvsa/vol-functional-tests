@OLCS-20956 @ap
@SS-PARTNER-USER-SEARCH
@ss_regression
Feature: Partner user external search by Address, Business name, Licence Number and Person's name

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i login as a partner user

  @smoketest
  Scenario: Business name partner external search for lorry and bus operators
    When I search for a lorry and bus operator by "business"
    Then search results page should display operator names containing our business name
    And I am able to view the applicants licence number

    @search-lorry-by-licence
  Scenario: Licence number partner external search for lorry and bus operators
    When I search for a lorry and bus operator by "licence"
    Then search results page should only display our licence number
    And I am able to view the licence number

  Scenario: Person's name partner external search for lorry and bus operators
    When I search for a lorry and bus operator by "person"
    Then search results page should display names containing our operator name