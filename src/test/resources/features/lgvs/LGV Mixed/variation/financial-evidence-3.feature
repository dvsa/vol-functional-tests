@lgv
@financial-evidence
Feature: 3 - Financial evidence calculations for hgv and lgv permutations.

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