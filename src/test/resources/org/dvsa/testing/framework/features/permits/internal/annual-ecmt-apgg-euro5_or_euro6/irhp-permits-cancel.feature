@internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression  @INTERNAL  @ECMTAPGGINTERNAL @OLCS-25288
Feature: Internal admin Cancels ECMT APGG Euro5 or Euro 6 permit Application

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    And I am viewing a licences IRHP section
    And I apply for an ECMT APGG Euro5 or Euro 6 application

  @reads-and-writes-system-properties
  Scenario: Cancel Permit Application
    When I am on application details page, I should see cancel button
    When I cancel Permit Application on the Application details page
    Then I should not see cancel button on application details page
    And On the application details page, I should see application as cancelled