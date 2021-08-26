Feature: Financial evidence calculations for hgv and lgv permutations.

#  create licences and store the operator type, licence type, number of hgvs and number of lgvs.
#  create a variation through the ui, make the change, minus current values and add the variation values.
  Scenario Outline: Check financial evidence for variations
    Given I create a new external user
    And i have a "<operatorType>" "<licenceType>" licence with a hgv authorisation of "<vehicleAuth>" in traffic area "<trafficArea>"
    And i create an operating centre variation with "1" hgv and "1" lgvs


    Examples:
    | operatorType | licenceType            | vehicleAuth | trafficArea |
    | goods        | standard_international |5            |1            |


  Scenario: check two variations against each other. Is it only the single variation and the other licences that are considers and the other variation is ignored.