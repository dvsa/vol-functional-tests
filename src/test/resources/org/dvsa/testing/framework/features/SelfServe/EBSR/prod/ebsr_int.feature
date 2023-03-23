@INT-SMOKE
Feature: import EBSR for English Areas

@ebsrintsmoke
  Scenario: import EBSR in self-serve smoke test
    Given I have an existing licence "PM0000306"
    When I upload an ebsr file with "42" days notice
    Then A short notice flag should not be displayed in selfserve
    And Documents are generated