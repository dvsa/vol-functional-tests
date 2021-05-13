@internal_short_term_apgg_euro5_or_euro6
Feature: Internal admin Cancels Short term APGG Euro5 or Euro 6 permit Application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section
    And I apply for a short term APGG Euro5 or Euro 6 application

  @INTERNAL @OLCS-25288 @OLCS-28230
  Scenario: Cancel Permit Application
    When I am on application details page, I should see cancel button
    When I cancel Permit Application on the Application details page
    Then I should not see cancel button on application details page
    And On the application details page, I should see application as cancelled