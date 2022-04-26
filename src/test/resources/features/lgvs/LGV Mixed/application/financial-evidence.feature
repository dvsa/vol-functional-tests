@lgv @financial-evidence
Feature: Check financial evidence is displaying the correct information

  Scenario Outline: Financial Evidence Page should display the valid financial standing rates on self serve matching those on internal
    Given I have a "<operatorType>" "<licenceType>" application
    When i am on the "application" financial evidence page and click on the How Did We Calculate This Link
    Then the valid financial standing rate values should be present

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_national      |
      | goods        | standard_international |
      | goods        | restricted             |
      | public       | standard_national      |
      | public       | standard_international |
      | public       | restricted             |