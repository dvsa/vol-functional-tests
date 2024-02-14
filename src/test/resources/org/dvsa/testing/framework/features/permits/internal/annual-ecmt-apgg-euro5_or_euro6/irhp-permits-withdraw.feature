@INTERNAL
@internal_annual_ecmt_apgg_euro5_or_euro6
@ECMTAPGGINTERNAL
@eupa_regression
Feature: Internal ECMT APGG Euro5 or Euro 6 with cross trade permits Application is Withdrawn

  Background:
    Given I have a "goods" "standard_international" licence
    And i create an admin and url search for my licence
    And I apply for an ECMT APGG Euro5 or Euro 6 application


  @OLCS-25288
  Scenario: I should see withdraw button under the Application details when application is in status of Under Consideration
    When I click on submit button
    And I pay fee for application
    Then I am in application details page, I should see withdraw button
    Then I am in fee details page, I should see withdraw button
    Then I withdraw Permit Application
    And I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn

  @OLCS-21284 @OLCS-25288
  Scenario: withdraw application from fee tab page
    When I am on the fee tab page
    And I select application to pay
    And I pay fee for application
    Then I am in application details page, I should see withdraw button
    When I withdraw Permit Application
    Then I am in application details page, I should not see withdraw button
    And I am in application details page, I should see application as withdrawn