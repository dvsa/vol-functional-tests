@certificateofroadworthiness_vehicles_E2E @eupa_regression
Feature: Certificate of roadworthiness for Vehicles end to end happy path journey

  Background:
    Given I have a "goods" "standard_international" licence
    And  I am on the VOL self-serve site
    And I select Certificate of Roadworthiness for vehicles on the select permit page
    And I select any licence number for Certificate of Roadworthiness for vehicles

  @EXTERNAL @OLCS-26817
  Scenario: Verify that Certificate of Roadworthiness for vehicles application flow is correct and application is submitted successfully with status of Valid
    Then I should be on the overview page
    Then I check content and complete Registration number section and click save and continue
    Then I check content and complete vehicle make and model section and click save and continue
    Then I check content and complete Vehicle identification number section and click save and continue
    Then I check content and complete Vehicle Engine number section and click save and continue
    Then I check content and complete vehicle engine type section and click save and continue
    Then I check content and complete MOT DATE section and click save and continue
    Then I check content and complete Certificate  of Compliance section and click save and continue
    Then I check content and click save and continue on the Check Your Answers page
    Then I check content and Accept and continue on the Declaration page
    Then I check content of the Submitted page
    When I select finish button
    Then I am navigated back to the permits dashboard page with my application status shown as Valid