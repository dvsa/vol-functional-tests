@certificateofroadworthiness_trailers_E2E @eupa_regression
Feature: Certificate of roadworthiness for Trailers end to end happy path journey

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And I select Certificate of Roadworthiness for trailers on the select permit page
    And I select any licence number for Certificate of Roadworthiness for trailers

  @EXTERNAL @OLCS-26817
  Scenario: Verify that Certificate of Roadworthiness for trailers application flow is correct and application is submitted successfully with status of Valid
    Then I am on Certificate of Roadworthiness for trailers Application overview Page
    Then I check content and complete Registration number section for  Certificate of Roadworthiness for trailers and click save and continue
    Then I check content and complete vehicle make and model section Certificate of Roadworthiness for trailers and click save and continue
    Then I check content and complete Vehicle identification number section Certificate of Roadworthiness for trailers and click save and continue
    Then I check content and complete MOT DATE section Certificate of Roadworthiness for trailers and click save and continue
    Then I check content and complete Certificate  of Compliance section Certificate of Roadworthiness for trailers and click save and continue
    Then I check content and click save and continue on the Check Your Answers page for Certificate of Roadworthiness for trailers
    Then I check content and Accept and continue on the Declaration page for Certificate of Roadworthiness for trailers page
    Then I check content of the Submitted page for Certificate of Roadworthiness for trailers
    When I select finish button
    Then I am navigated back to the permits dashboard page for Certificate of Roadworthiness for trailers with my application status shown as Valid