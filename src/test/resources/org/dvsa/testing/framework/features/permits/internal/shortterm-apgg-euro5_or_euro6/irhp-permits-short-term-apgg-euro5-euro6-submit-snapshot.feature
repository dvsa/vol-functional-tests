@internal_short_term_apgg_euro5_or_euro6 @eupa_regression
Feature: Internal Case Worker submits an ECMT APGG Euro5 or Euro 6 Permit application

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    And I am viewing a licences IRHP section
    And I apply for a short term APGG Euro5 or Euro 6 application

  @INTERNAL @OLCS-25288 @OLCS-28230 @perm-pay
  Scenario: To verify that Application status goes to UC and snapshot is generated when case worker successfully submits an Application
    Then In application details page, I should see Submit button
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see application is in UC status
    And I am in Docs and attachments page, I should see the snapshot generate successfully