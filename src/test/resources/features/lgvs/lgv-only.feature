@lgv
Feature: LGV only tests

  Scenario Outline: Type of Licence LGV only no option selected error check
    Given I am applying for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" "<lgvUndertaking>" licence
    When I click save and continue
    Then A LGV only error message should be displayed

  Examples:
    | licenceWhere      | operatorType | licenceType            | vehicleType  | lgvUndertaking |
    | great_britain     | goods        | standard_international | no_selection | unchecked      |
    | northern_ireland  | no_selection | standard_international | no_selection | unchecked      |

  Scenario Outline: Type of Licence LGV Undertakings no option selected error check
    Given I am applying for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" "<lgvUndertaking>" licence
    When I click save and continue
    Then A LGV undertakings error message should be displayed

    Examples:
      | licenceWhere      | operatorType | licenceType            | vehicleType    | lgvUndertaking |
      | great_britain     | goods        | standard_international | lgv_only_fleet | unchecked      |
      | northern_ireland  | no_selection | standard_international | lgv_only_fleet | unchecked      |

  Scenario Outline: Switch Standard Internation licence type warning message
    Given I am applying for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" "<lgvUndertaking>" licence
    And I click save and continue
    And I update the vehicle type on the licence to "<newType>"
    And A change licence type warning message is displayed
    When I confirm the warning message
    Then each section on the application overview page has the correct status for the "<newType>" licence

    Examples:
      | licenceWhere      | operatorType | licenceType            | vehicleType    | lgvUndertaking | newType           |
      | great_britain     | goods        | standard_international | lgv_only_fleet | checked        | standard_national |
      | great_britain     | goods        | restricted             |                | unchecked      | mixed_fleet       |
      | northern_ireland  | no_selection | standard_international | lgv_only_fleet | checked        | mixed_fleet       |
      | great_britain     | goods        | standard_international | mixed_fleet    | unchecked      | lgv_only_fleet    |
      | northern_ireland  | no_selection | standard_international | mixed_fleet    | unchecked      | lgv_only_fleet    |
