Feature: Financial evidence calculations for hgv and lgv permutations.

#  create licences and store the operator type, licence type, number of hgvs and number of lgvs.
#  create a variation through the ui, make the change, minus current values and add the variation values.
  Scenario Outline: Check financial evidence for variations
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    When i create an operating centre variation with "<hgvs>" hgv and "<lgvs>" lgvs
    Then the financial evidence value should be as expected
    And the same financial evidence value is displayed on internal

    Examples:
    | operatorType | licenceType            | vehicleAuth | trafficArea | hgvs | lgvs |
    | goods        | standard_international |     5       |      1      |   5  |   5  |
    | goods        | standard_international |     5       |      1      |   7  |   0  |
    | goods        | standard_international |     5       |      1      |   3  |   2  |
#    | goods        | standard_international |     5       |      1      |   0  |   5  | CURRENTLY 0 HGVs is blocked.
#    | goods        | standard_national |     5       |      1      |   6  |   0  | WRONG VALUE. JUST USES ADDITIONAL VALUES.
    | goods        | restricted             |     5       |      1      |   6  |   0  |
#    | public        | standard_national |  /   5       |      1      |   6  |   0  | WRONG VALUE. JUST USES ADDITIONAL VALUES.
#    | public        | standard_international |     5       |      1      |   6  |   0  | WRONG VALUE. JUST USES ADDITIONAL VALUES.
    | public        | restricted            |     5       |      1      |   2  |   0  |

  Scenario Outline: Check financial evidence for variations MLH
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    And i have a "<operatorType2>" "<licenceType2>" licence with a hgv authorisation of "<vehicleAuth2>" in traffic area "<trafficArea2>"
    When i create an operating centre variation with "<hgvs>" hgv and "<lgvs>" lgvs
    Then the financial evidence value should be as expected
    And the same financial evidence value is displayed on internal

    Examples:
      | operatorType | licenceType            | vehicleAuth | trafficArea | operatorType2 | licenceType2                | vehicleAuth2 | trafficArea2 | hgvs | lgvs |
#      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   6  |   0  | /
#      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   5  |   5  | /
#      | goods        | standard_national      |     5       |      1      | goods         | standard_international      |     5        |     2        |   3  |   2  | /
#      | goods        | standard_international |   5 /  5    |      1      | goods         | standard_international      |     5        |     2        |   3  |   2  | Not doable until lgvs can be added on applications
#      | goods        | restricted             |     5       |      1      | goods         | standard_national           |     5        |     2        |   7  |   0  | ADDING THE FIRST VEHICLE OF RESTRICTED AND NOT STANDARD NATIONAL.
#      | goods        | standard_national      |     5       |      1      | goods         | restricted                  |     5        |     2        |   6  |   0  | FAILS, AUTOMATION HAS SUPPOSED RIGHT VALUE BUT NO IDEA WHAT IS HAPPENING HERE
#      | goods        | restricted             |     5       |      1      | goods         | restricted                  |     5        |     2        |   6  |   0  | /
#      | public       | standard_national      |     5       |      1      | public        | standard_international      |     5        |     2        |   9  |   0  | WRONG VALUE. JUST USES ADDITIONAL VALUES.
#      | public       | standard_national      |     5       |      1      | goods         | standard_national           |     5        |     2        |   8  |   0  | FAILS, AUTOMATION HAS SUPPOSED RIGHT VALUE BUT NO IDEA WHAT IS HAPPENING HERE

# Only works for valid entries. I.e. public restricted only accept 2? and LGVs aren't available on non goods standard international.
ALSO CHECK THE CORRECT VALUES FOR ALL OF THE FINANCIAL STANDING RATES. ON THE DOCUMENT TICKET IT SAYS PSV RESTRICED ADDITIONAL IS 1600.
  Scenario: check two variations against each other. Is it only the single variation and the other licences that are considers and the other variation is ignored.


#    AS FAR AS LOGIC, THE AUTOMATION IS COMPLETELY RIGHT.