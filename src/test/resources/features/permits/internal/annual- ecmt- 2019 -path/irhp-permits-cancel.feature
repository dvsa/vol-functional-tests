@internal_annual_ecmt_apgg_euro5_or_euro6 @eupa_regression
Feature: Internal IRHP permits Cancel Application

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section
    And I apply for an ECMT APGG Euro5 or Euro 6 application

  @INTERNAL @OLCS-20960 @olcs-27682
  Scenario: View Cancel button on Application details page
    When I am on application details page, I should see cancel button

  @INTERNAL @OLCS-20960 @olcs-27682
  Scenario: Cancel Permit Application
    When I cancel Permit Application on the Application details page
    And On the application details page, I should see application as cancelled