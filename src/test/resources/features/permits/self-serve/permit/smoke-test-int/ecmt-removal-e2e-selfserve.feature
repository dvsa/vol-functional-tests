@smoketestint
Feature: User creates an ECMT removal application with existing licence

  @OLCS-28298
  Scenario Outline: User submits and pays for ECMT removal Application
    Given I have logged on selfserve site in int environment
    And  username and passwords retrieved from excel row "<row_index>" dataset
    And I apply for an ECMT removal permit

    Examples:
      |row_index|
      |0        |