@lgv
Feature: Validation is present on the operating centres and authorisations page

  Scenario: HGV, LGV and Trailer authorisation value is required
    Given i have a valid "goods" "standard_international" licence
    And i begin an operating centre and authorisation variation
    When i clear the authorisation fields and click save
    Then hgv, lgv and trailer missing authorisation value errors should display

  Scenario Outline: Saving a total vehicle authorisation value above the overall number of vehicle at all OCs triggers an error
    Given i have a valid "<operatorType>" "<licenceType>" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "6" hgvs and "7" trailers
    When i save a hgv authorisation greater that the overall number of vehicles across the licence
    Then i am prompted with the total hgv authorisation exceeds number of vehicles on licence error

    Examples:
    | operatorType | licenceType            |
    | goods        | standard_international |
    | public       | standard_international |

  Scenario Outline: Saving a total vehicle authorisation value under the number of vehicle at the lowest OCs triggers an error
    Given i have a valid "<operatorType>" "<licenceType>" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "6" hgvs and "7" trailers
    When i save a hgv authorisation fewer that the overall number of vehicles across the licence
    Then i am prompted with the total hgv authorisation is fewer than the largest OC on licence error

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_international |
      | public       | standard_international |

  Scenario: Saving a total trailer authorisation value above the overall number of vehicle at all OCs triggers an error
    Given i have a valid "goods" "standard_international" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "6" hgvs and "7" trailers
    When i save a trailer authorisation greater that the overall number of vehicles across the licence
    Then i am prompted with the total trailer authorisation exceeds number of vehicles on licence error

  Scenario: Saving a total trailer authorisation value under the number of vehicle at the lowest OCs triggers an error
    Given i have a valid "goods" "standard_international" licence
    And i begin an operating centre and authorisation variation
    And i create a new operating centre with "6" hgvs and "7" trailers
    When i save a trailer authorisation fewer that the overall number of vehicles across the licence
    Then i am prompted with the total trailer authorisation is fewer than the largest OC on licence error