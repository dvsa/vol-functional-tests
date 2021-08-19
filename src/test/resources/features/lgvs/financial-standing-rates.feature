@lgv
Feature: Create, edit and delete financial standing rates on internal

  Scenario Outline: I can add a financial standing rate
    Given i have a valid "<operatorType>" "<licenceType>" licence
    And i am on the financial standing rates page
    When i add a financial standing rate
    Then the table displays the correct financial standing rate information
    And the database contains the correct financial standing rate information

    Examples:
    | operatorType | licenceType            | vehicleType    |
    | goods        | standard_international | lgv            |
    | goods        | standard_international | hgv            |
    | goods        | standard_national      | not applicable |
    | psv          | standard_international | not applicable |


  Scenario: I can edit a financial standing rate


  Scenario: I can delete a financial standing rate


  Scenario: Vehicle type is closed to non goods standard international licences


  Scenario: Validation