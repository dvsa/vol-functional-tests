@lgv
Feature: LGV only tests

  Scenario Outline: Type of Licence LGV only no option selected error check
    Given I am applying for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" licence
    When I click save and continue
    Then A LGV only error message should be displayed

  Examples:
    | licenceWhere      | operatorType | licenceType            | vehicleType  |
    | great_britain     | goods        | standard_international | no_selection |
    | northern_ireland  | no_selection | standard_international | no_selection |

  Scenario Outline: Type of Licence LGV Undertakings no option selected error check
    Given I am applying for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" licence
    When I click save and continue
    Then A LGV undertakings error message should be displayed

    Examples:
      | licenceWhere      | operatorType | licenceType            | vehicleType    |
      | great_britain     | goods        | standard_international | lgv_only_fleet |
      | northern_ireland  | no_selection | standard_international | lgv_only_fleet |

  Scenario Outline: Switch Standard Internation licence type warning message
    Given I am applying for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" licence
    And I "<selection>" the LGV undertaking declaration checkbox
    And I click save and continue
    And I update the vehicle type on the licence to "<newVehicleType>"
    When I click save and continue
    Then A change licence type warning message should be displayed

    Examples:
      | licenceWhere      | operatorType | licenceType            | vehicleType    | selection   | newVehicleType |
      | great_britain     | goods        | standard_international | lgv_only_fleet | select      | mixed_fleet    |
      | northern_ireland  | no_selection | standard_international | lgv_only_fleet | select      | mixed_fleet    |
      | great_britain     | goods        | standard_international | mixed_fleet    | dont_select | lgv_only_fleet |
      | northern_ireland  | no_selection | standard_international | mixed_fleet    | dont_select | lgv_only_fleet |
