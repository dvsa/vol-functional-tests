@Deprecated
Feature: Short term ECMT APSG without sectors application flow

  Background:
    Given I have valid Goods standard_international VOL licence
    And  I am on the VOL self-serve site
    And  I apply and pay for a short term APSG without sectors application

   @olcs-26598 @olcs-27581
  Scenario: Can view the application in under consideration status
    Then I am navigated back to the permits dashboard page with my application status shown as Under Consideration