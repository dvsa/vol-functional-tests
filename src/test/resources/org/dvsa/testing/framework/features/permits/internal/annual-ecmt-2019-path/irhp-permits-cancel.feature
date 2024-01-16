@INTERNAL @OLCS-20960 @olcs-27682 @internal_annual_ecmt_apgg_euro5_or_euro6
Feature: Internal IRHP permits Cancel Application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL internal site
    And i create an admin and url search for my licence
    And I am viewing a licences IRHP section
    And I apply for an ECMT APGG Euro5 or Euro 6 application

  Scenario: View Cancel button on Application details page
    When I am on application details page, I should see cancel button

  Scenario: Cancel Permit Application
    When I cancel Permit Application on the Application details page
    And On the application details page, I should see application as cancelled