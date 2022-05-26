@lgv
@financial-evidence
Feature: 2 - Financial evidence calculations for hgv and lgv permutations.

  Scenario Outline: MLH variations financial evidence check
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    And i have a "<operatorType2>" "<licenceType2>" licence with a hgv authorisation of "<vehicleAuth2>" in traffic area "<trafficArea2>"
    When i create an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    Then the financial evidence value should be as expected for "<HGVs>" hgvs and "<LGVs>" lgvs
    And the same financial evidence value is displayed on internal

    Examples:
      | operatorType | licenceType            | vehicleAuth | trafficArea | operatorType2 | licenceType2                | vehicleAuth2 | trafficArea2 | HGVs | LGVs |
      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   6  |   0  |
      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   3  |   2  |
      | goods        | restricted             |     5       |      1      | goods         | standard_national           |     5        |     2        |   7  |   0  |
      | goods        | standard_national      |     5       |      1      | goods         | restricted                  |     5        |     2        |   6  |   0  |
      | goods        | restricted             |     5       |      1      | goods         | restricted                  |     5        |     2        |   6  |   0  |
      | public       | standard_national      |     5       |      1      | public        | standard_international      |     5        |     2        |   9  |   0  |
      | public       | standard_national      |     5       |      1      | goods         | standard_national           |     5        |     2        |   8  |   0  |
      | public       | restricted             |     2       |      1      | goods         | standard_national           |     5        |     2        |   8  |   0  |
