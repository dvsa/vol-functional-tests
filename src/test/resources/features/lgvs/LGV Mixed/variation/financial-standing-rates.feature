@lgv
Feature: Create, edit and delete financial standing rates on internal

  Background:
    Given i am on the financial standing rates page

  Scenario Outline: I can add, edit and delete a financial standing rate
    And i add a "<operatorType>" "<licenceType>" "<vehicleType>" financial standing rate
    And the table displays the correct financial standing rate information
    And i edit and save a financial standing rate
    And the table displays the correct edited financial standing rate information
    When i delete a financial standing rate
    Then the table no longer displays the deleted financial standing rate

    Examples:
    | operatorType                   | licenceType            | vehicleType         |
    | Goods Vehicle                  | Standard International | Light Goods Vehicle |
    | Goods Vehicle                  | Standard International | Heavy Goods Vehicle |
    | Goods Vehicle                  | Standard National      | Not Applicable      |
    | Public Service Vehicle         | Standard International | Not Applicable      |

  Scenario Outline: Vehicle type is closed to non goods standard international licences
    When i choose to add a "<operatorType>" "<licenceType>" financial standing rate
    Then the HGV and LGV vehicle types are not selectable

    Examples:
      | operatorType                   | licenceType            |
      | Goods Vehicle                  | Standard National      |
      | Public Service Vehicle         | Standard International |

  Scenario: Validation
    When i submit no information on a financial standing rate
    Then i should receive the correct financial standing modal errors