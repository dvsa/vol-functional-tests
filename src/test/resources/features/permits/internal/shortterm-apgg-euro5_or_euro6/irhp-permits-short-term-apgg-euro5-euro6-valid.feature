@internal_short_term_apgg_euro5_or_euro6
Feature:  ECMT APGG Euro5 or Euro 6 Permit application is granted permit and goes to Valid status

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section

  @INTERNAL @OLCS-25288 @OLCS-28230
  Scenario: To verify that Application status goes to Valid when Application is granted and issue fee is paid
    And I apply for a short term APGG Euro5 or Euro 6 application
    Then In application details page, I should see Submit button
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see application is in UC status
    When I Grant and pay issue fee on Internal
    Then the ECMT APGG application goes to valid status