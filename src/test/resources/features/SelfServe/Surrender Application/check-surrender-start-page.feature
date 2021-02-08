@SS
@SURRENDER-START-PAGE
@OLCS-21944
Feature: Checking the start page for surrenders

  Scenario Outline: PSV and Goods licence
    Given i have a valid "<LicenceType>" "standard_national" licence
    When i click on apply to surrender licence
    Then the correct page heading for "<LicenceType>" should be displayed
    And the correct instructions for "<LicenceType>" should be displayed
    And the correct licence number should be displayed

    Examples:
      | LicenceType |
      | public      |
      | goods       |
