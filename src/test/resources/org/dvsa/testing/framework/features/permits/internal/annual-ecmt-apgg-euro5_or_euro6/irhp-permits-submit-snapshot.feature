@internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Internal Case Worker submits an ECMT APGG Euro5 or Euro 6 with cross trade Permit application

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    And I am viewing a licences IRHP section
    And I apply for an ECMT APGG Euro5 or Euro 6 application

  @INTERNAL  @ECMTAPGGINTERNAL @OLCS-25288
  Scenario: To verify that Application status goes to UC and snapshot is generated when case worker successfully submits an Application
    Then In application details page, I should see Submit button
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see application is in UC status
    And I am in Docs and attachments page, I should see the snapshot generate successfully