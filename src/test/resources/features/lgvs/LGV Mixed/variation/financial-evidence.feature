@lgv
@financial-evidence
@lgv-smoke

Feature: 1 - Financial evidence calculations for hgv and lgv permutations.

  Scenario Outline: Check financial evidence for variations
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    When i create an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    Then the financial evidence value should be as expected for "<HGVs>" hgvs and "<LGVs>" lgvs
    And the same financial evidence value is displayed on internal

    Examples:
      | operatorType | licenceType            | vehicleAuth | trafficArea | HGVs | LGVs |
      | goods        | standard_international |     5       |      1      |   7  |   0  |
      | goods        | standard_international |     5       |      1      |   3  |   2  |
      | goods        | standard_national      |     5       |      1      |   6  |   0  |
      | goods        | restricted             |     5       |      1      |   6  |   0  |
      | public       | standard_national      |     5       |      1      |   6  |   0  |
      | public       | standard_international |     5       |      1      |   6  |   0  |
      | public       | restricted             |     5       |      1      |   2  |   0  |
