@financial-evidence
Feature: Financial evidence calculations for hgv and lgv permutations.

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

  Scenario Outline: Financial Evidence Page should display the valid financial standing rates on self serve matching those on internal
    Given i have a valid "<operatorType>" "<licenceType>" licence
    And i create an operating centre variation with "6" hgvs and "6" lgvs
    When i am on the "variation" financial evidence page and click on the How Did We Calculate This Link
    Then the valid financial standing rate values should be present

    Examples:
      | operatorType | licenceType            |
      | goods        | standard_national      |
      | goods        | standard_international |
      | goods        | restricted             |
      | public       | standard_national      |
      | public       | standard_international |

  Scenario Outline: When increasing HGV and LGV authorisations I am prompted for financial evidence
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" lgv authorisation of "<lgvAuth>" in traffic area "<trafficArea>"
    When i create an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    Then i should be prompted to enter financial evidence information

    Examples:
      | operatorType | licenceType            | vehicleAuth | lgvAuth | trafficArea | HGVs | LGVs |
      | goods        | standard_international |     5       |    0    |      1      |   7  |   0  |
      | goods        | standard_international |     5       |    0    |      1      |   5  |   2  |
      | goods        | standard_international |     5       |    2    |      1      |   6  |   1  |
      | goods        | standard_national      |     5       |    0    |      1      |   6  |   0  |
      | public       | standard_international |     5       |    0    |      1      |   6  |   0  |

  Scenario Outline: When decreasing HGV and LGV authorisations I am not prompted for financial evidence
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" lgv authorisation of "<lgvAuth>" in traffic area "<trafficArea>"
    When i create an operating centre variation with "<HGVs>" hgvs and "<LGVs>" lgvs
    Then i should not be prompted to enter financial evidence information

    Examples:
      | operatorType | licenceType            | vehicleAuth | lgvAuth | trafficArea | HGVs | LGVs |
      | goods        | standard_international |     5       |    0    |      1      |   4  |   0  |
      | goods        | standard_international |     5       |    5    |      1      |   5  |   2  |
      | goods        | standard_international |     5       |    5    |      1      |   2  |   7  |
      | goods        | standard_national      |     5       |    0    |      1      |   4  |   0  |
      | public       | standard_international |     5       |    0    |      1      |   4  |   0  |