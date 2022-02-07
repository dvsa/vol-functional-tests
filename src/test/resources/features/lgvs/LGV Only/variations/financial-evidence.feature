@lgv @financial-evidence
Feature: Financial evidence calculations for lgv only variations

  Background:
    Given I create a new external user

  Scenario: Check financial evidence for lgv only variations
    And I have a valid "GB" lgv only licence in traffic area "1"
    When I create an lgv only authorisation variation with "8"
    Then the financial evidence value should be as expected for "0" hgvs and "8" lgvs
    And the same financial evidence value is displayed on internal

  Scenario: Check financial evidence for lgv only variations MLH
    And i have a "goods" "standard_national" licence with a hgv authorisation of "5" in traffic area "1"
    And I have a valid "GB" lgv only licence in traffic area "2"
    When I create an lgv only authorisation variation with "8"
    Then the financial evidence value should be as expected for "0" hgvs and "8" lgvs
    And the same financial evidence value is displayed on internal

  Scenario: When increasing authorisation I am prompted for financial evidence
    And I have a valid "GB" lgv only licence in traffic area "1"
    When I create an lgv only authorisation variation with "8"
    Then i should be prompted to enter financial evidence information

  Scenario: When decreasing  authorisation I am not prompted for financial evidence
    And I have a valid "GB" lgv only licence in traffic area "1"
    When I create an lgv only authorisation variation with "2"
    Then i should not be prompted to enter financial evidence information