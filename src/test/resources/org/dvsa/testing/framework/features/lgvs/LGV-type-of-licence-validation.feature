@lgv
Feature: Error Validation for Type of Licence LGV Only Declaration

  Background:
    Given I create a new external user

  Scenario Outline: Type of Licence LGV only no option selected error check
    When I apply for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" "<lgvUndertaking>" licence
    Then A LGV only error message should be displayed

  Examples:
    | licenceWhere | operatorType | licenceType            | vehicleType  | lgvUndertaking |
    | GB           | goods        | standard_international | no_selection | unchecked      |
    | NI           | no_selection | standard_international | no_selection | unchecked      |

  Scenario Outline: Type of Licence LGV Undertakings no option selected error check
    When I apply for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" "<lgvUndertaking>" licence
    Then A LGV undertakings error message should be displayed

    Examples:
      | licenceWhere | operatorType | licenceType            | vehicleType    | lgvUndertaking |
      | GB           | goods        | standard_international | lgv_only_fleet | unchecked      |
      | NI           | no_selection | standard_international | lgv_only_fleet | unchecked      |

  @lgv-smoke
  Scenario Outline: Switch Standard Internation licence type warning message and deletion of data
    And I apply for a "<licenceWhere>" "<operatorType>" "<licenceType>" "<vehicleType>" "<lgvUndertaking>" licence
    And I go to update the vehicle type on the licence to "<newLicenceType>" "<newVehicleType>" "<newLgvUndertaking>"
    And A change licence type warning message is displayed
    When I confirm the warning message
    Then each section on the application overview page has the correct status for the "<newVehicleType>" licence

    Examples:
      | licenceWhere | operatorType | licenceType            | vehicleType    | lgvUndertaking | newLicenceType         | newVehicleType | newLgvUndertaking |
      | GB           | goods        | standard_international | lgv_only_fleet | checked        | standard_national      |                |                   |
      | GB           | goods        | restricted             |                |                | standard_international | mixed_fleet    | unchecked         |
      | NI           | no_selection | standard_international | lgv_only_fleet | checked        | standard_international | mixed_fleet    | unchecked         |
      | GB           | goods        | standard_international | mixed_fleet    | unchecked      | standard_international | lgv_only_fleet | checked           |
      | NI           | no_selection | standard_international | mixed_fleet    | unchecked      | standard_international | lgv_only_fleet | checked           |

  Scenario: Cancel switch from new Standard Internation licence type and data shouldn't change
    And I apply for a "GB" "goods" "standard_international" "lgv_only_fleet" "checked" licence
    And I go to update the vehicle type on the licence to "standard_national" "N/A" "N/A"
    When I cancel the warning message and click cancel on the type of licence page
    Then each section on the application overview page has the correct status for the "lgv_only_fleet" licence