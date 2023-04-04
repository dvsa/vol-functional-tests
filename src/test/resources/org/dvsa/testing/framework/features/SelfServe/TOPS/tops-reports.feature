@INT-SMOKE
@TOPS

Feature: Self serve user has access to their TOPS Reports

  Background:
    Given i have a self serve account
    And I have navigated and logged into the TOPS report portal

    Scenario: Self Serve user views their current Operator Compliance Risk Score (OCRS)
      Given I have navigated to the Operator Compliance Risk Score Page


