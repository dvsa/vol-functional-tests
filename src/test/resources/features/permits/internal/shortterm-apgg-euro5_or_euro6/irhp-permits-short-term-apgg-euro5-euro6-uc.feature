@internal_short_term_apgg_euro5_or_euro6
Feature: Internal Short Term permits APGG Euro 5 or Euro 6 until UC Status

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I am viewing a licences IRHP section
    And I apply for a short term APGG Euro5 or Euro 6 application

  @INTERNAL @OLCS-26332 @OLCS-28230
  Scenario: Case worker submits and pays for short term APGG Euro 5 or Euro 6 application
    And I am on the fee tab page
    And I select application to pay
    And I pay fee for application
    Then I am in application details page, I should see application is in UC status