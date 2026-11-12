# @ss_regression
@vol763 @ni-variation-traffic-area-validation
Feature: Northern Ireland variation operating centre traffic area

  Scenario: Reject an operating centre outside Northern Ireland
    Given an NI goods licence with operating centre postcode "BT1 1DN" has an operating centre variation
    When the operator attempts to add an operating centre outside NI
      | address line 1 | Unit 1         |
      | town           | Nottingham     |
      | postcode       | NG3 7BL        |
      | country        | United Kingdom |
      | vehicles       | 1              |
      | trailers       | 1              |
    Then postcode "NG3 7BL" is rejected with "Postcode: Your Operating Centre must be located in Northern Ireland" and the operating centres are unchanged
