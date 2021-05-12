@smoketestint
Feature: Caseworker creates an ECMT removal application with existing licence

  @OLCS-28298
  Scenario Outline: Case Worker submits and pays for ECMT removal Application
    Given I have logged on internal site in int environment
    And  I input username and passwords with excel row "<row_index>" dataset
    And the case worker apply for an ECMT Removal application
    When I'm viewing my submitted ECMT Removal application
    And I pay fee for the ECMT removal application
    Then the application goes to valid status

Examples:
    |row_index|
    |0        |
