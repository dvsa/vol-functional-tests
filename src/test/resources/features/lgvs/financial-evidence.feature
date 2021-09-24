Feature: Financial evidence calculations for hgv and lgv permutations.

  Scenario Outline: Check financial evidence for variations
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    When i create an operating centre variation with "<hgvs>" hgvs and "<lgvs>" lgvs
    Then the financial evidence value should be as expected
    And the same financial evidence value is displayed on internal

    Examples:
      | operatorType | licenceType            | vehicleAuth | trafficArea | hgvs | lgvs |
      | goods        | standard_international |     5       |      1      |   7  |   0  |
      | goods        | standard_international |     5       |      1      |   3  |   2  |
#      | goods        | standard_international |     5       |      1      |   0  |   5  | CURRENTLY 0 HGVs is blocked.
      | goods        | standard_national      |     5       |      1      |   6  |   0  |
      | goods        | restricted             |     5       |      1      |   6  |   0  |
      | public       | standard_national      |     5       |      1      |   6  |   0  |
      | public       | standard_international |     5       |      1      |   6  |   0  |
      | public       | restricted             |     5       |      1      |   2  |   0  |

  Scenario Outline: Check financial evidence for variations MLH
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    And i have a "<operatorType2>" "<licenceType2>" licence with a hgv authorisation of "<vehicleAuth2>" in traffic area "<trafficArea2>"
    When i create an operating centre variation with "<hgvs>" hgvs and "<lgvs>" lgvs
    Then the financial evidence value should be as expected
    And the same financial evidence value is displayed on internal

    Examples:
      | operatorType | licenceType            | vehicleAuth | trafficArea | operatorType2 | licenceType2                | vehicleAuth2 | trafficArea2 | hgvs | lgvs |
      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   6  |   0  |
      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   3  |   2  |
#      | goods        | standard_international |   5 /  5    |      1      | goods         | standard_international      |     5        |     2        |   3  |   2  | Not doable until lgvs can be added on applications
      | goods        | restricted             |     5       |      1      | goods         | standard_national           |     5        |     2        |   7  |   0  |
      | goods        | standard_national      |     5       |      1      | goods         | restricted                  |     5        |     2        |   6  |   0  |
      | goods        | restricted             |     5       |      1      | goods         | restricted                  |     5        |     2        |   6  |   0  |
      | public       | standard_national      |     5       |      1      | public        | standard_international      |     5        |     2        |   9  |   0  |
      | public       | standard_national      |     5       |      1      | goods         | standard_national           |     5        |     2        |   8  |   0  |