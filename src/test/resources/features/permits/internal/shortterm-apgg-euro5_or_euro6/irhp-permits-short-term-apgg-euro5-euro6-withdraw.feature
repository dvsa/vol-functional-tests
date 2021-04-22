@internal_short_term_apgg_euro5_or_euro6
Feature: Internal ECMT APGG Euro5 or Euro 6 permits Application is Withdrawn

  Background:
    Given I have valid Goods standard_international VOL licence
    And I am on the VOL internal site
    And I am viewing a good operating licence on internal
    And I apply for a short term APGG Euro5 or Euro 6 application


  @INTERNAL @OLCS-25288 @OLCS-28230
  Scenario: I should see withdraw button under the Application details when application is in status of Under Consideration
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see withdraw button
    Then I am in fee details page, I should see withdraw button
    Then In application details page, I withdraw Permit Application
    And I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn

  @INTERNAL @OLCS-21284 @Deprecated
  Scenario: Withdraw application from details page
    When I click on submit button
    And I pay fee for application
    Then In application details page, I withdraw Permit Application
    And I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn

  @INTERNAL @OLCS-25288 @OLCS-28230
  Scenario: withdraw application from fee tab page
    When I am on the fee tab page
    And I select application to pay
    And I pay fee for application
    Then I am in application details page, I should see withdraw button
    When In application details page, I withdraw Permit Application
    Then I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn
