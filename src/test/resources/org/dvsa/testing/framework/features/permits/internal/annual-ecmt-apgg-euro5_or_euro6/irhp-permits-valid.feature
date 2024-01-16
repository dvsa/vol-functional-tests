@internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression @localsmoke
Feature:  ECMT APGG Euro5 or Euro 6 with cross trade Permit application is granted permit and goes to Valid status

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    And I am viewing a licences IRHP section

  @INTERNAL @ECMTAPGGINTERNAL @OLCS-25288
  Scenario: To verify that Application status goes to Valid when Application is granted and issue fee is paid
    And I apply for an ECMT APGG Euro5 or Euro 6 application
    Then In application details page, I should see Submit button
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see application is in UC status
    When I Grant and pay issue fee on Internal
    Then the application goes to valid status